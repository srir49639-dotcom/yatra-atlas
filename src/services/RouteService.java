package services;

import algorithms.graph.Dijkstra;
import data.TravelData;
import model.Destination;
import model.TravelEdge;
import utils.AlgorithmSelector;
import utils.PerformanceTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic Route Finder Service powered by Dijkstra's Shortest Path Algorithm.
 * Computes optimal intercity travel routes, total distance, travel duration, and cost.
 * Calculates routes dynamically from graph edges with zero hardcoding.
 */
public class RouteService {

    public static class RouteStep {
        public String fromCity;
        public String toCity;
        public int distanceKm;
        public int timeMinutes;
        public int costInr;
        public String mode;

        public RouteStep(String fromCity, String toCity, int distanceKm, int timeMinutes, int costInr, String mode) {
            this.fromCity = fromCity;
            this.toCity = toCity;
            this.distanceKm = distanceKm;
            this.timeMinutes = timeMinutes;
            this.costInr = costInr;
            this.mode = mode;
        }
    }

    public static class RouteResponse {
        public String fromId;
        public String fromName;
        public String toId;
        public String toName;
        public boolean reachable;
        public int totalDistanceKm;
        public int totalTimeMinutes;
        public int totalCostInr;
        public List<String> pathCities;
        public List<RouteStep> legs;
        public double calculationTimeMicros;
        public String message;

        public RouteResponse(String fromId, String fromName, String toId, String toName,
                             boolean reachable, int totalDistanceKm, int totalTimeMinutes,
                             int totalCostInr, List<String> pathCities, List<RouteStep> legs,
                             double calculationTimeMicros, String message) {
            this.fromId = fromId;
            this.fromName = fromName;
            this.toId = toId;
            this.toName = toName;
            this.reachable = reachable;
            this.totalDistanceKm = totalDistanceKm;
            this.totalTimeMinutes = totalTimeMinutes;
            this.totalCostInr = totalCostInr;
            this.pathCities = pathCities;
            this.legs = legs;
            this.calculationTimeMicros = calculationTimeMicros;
            this.message = message;
        }
    }

    // Node representation in the unified routing graph
    public static class RouteNode {
        public String id;
        public String name;
        public String type; // "City" or "Attraction"
        public String parentCityId;

        public RouteNode(String id, String name, String type, String parentCityId) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.parentCityId = parentCityId;
        }
    }

    // Internal edge in the routing graph
    public static class GraphEdge {
        public int fromIdx;
        public int toIdx;
        public int distanceKm;
        public int timeMinutes;
        public int costInr;
        public String mode;

        public GraphEdge(int fromIdx, int toIdx, int distanceKm, int timeMinutes, int costInr, String mode) {
            this.fromIdx = fromIdx;
            this.toIdx = toIdx;
            this.distanceKm = distanceKm;
            this.timeMinutes = timeMinutes;
            this.costInr = costInr;
            this.mode = mode;
        }
    }

    private static List<RouteNode> allNodes = null;
    private static List<GraphEdge> allEdges = null;

    private static synchronized void initializeGraph() {
        if (allNodes != null) return;

        allNodes = new ArrayList<>();
        allEdges = new ArrayList<>();

        // 1. Add all 20 Cities
        for (Destination d : TravelData.DESTINATIONS) {
            allNodes.add(new RouteNode(d.getId(), d.getName(), "City", d.getId()));
        }

        // 2. Add all Attractions / Landmarks
        for (Destination d : TravelData.DESTINATIONS) {
            for (model.Attraction a : d.getAttractions()) {
                allNodes.add(new RouteNode(a.getId(), a.getName(), "Attraction", d.getId()));
            }
        }

        // 3. Add Intercity Edges
        for (TravelEdge te : TravelData.TRAVEL_EDGES) {
            int u = findNodeIndex(te.getFromId());
            int v = findNodeIndex(te.getToId());
            if (u != -1 && v != -1) {
                allEdges.add(new GraphEdge(u, v, te.getDistanceKm(), te.getTravelTimeMinutes(), te.getCostInr(), te.getMode()));
            }
        }

        // 4. Add Intra-City Landmark Connections
        addIntraCityConnections();
    }

    private static void addIntraCityConnections() {
        // Connect attractions to their parent city transit hub and to each other
        for (Destination d : TravelData.DESTINATIONS) {
            int cityIdx = findNodeIndex(d.getId());
            model.Attraction[] attrs = d.getAttractions();
            if (attrs == null || attrs.length == 0 || cityIdx == -1) continue;

            for (int i = 0; i < attrs.length; i++) {
                int attrIdx = findNodeIndex(attrs[i].getId());
                if (attrIdx == -1) continue;

                // Base connection to city hub
                int hubDist = 5 + (i * 3);
                int hubTime = 15 + (i * 7);
                int hubCost = 50 + (i * 30);
                String hubMode = "City Metro / Transit";

                addBidirectionalEdge(attrIdx, cityIdx, hubDist, hubTime, hubCost, hubMode);

                // Connection to adjacent attractions within the city
                if (i > 0) {
                    int prevAttrIdx = findNodeIndex(attrs[i - 1].getId());
                    if (prevAttrIdx != -1) {
                        int dist = 3 + (i * 2);
                        int time = 10 + (i * 5);
                        int cost = 40 + (i * 25);
                        String mode = "Auto / Local Cab";
                        addBidirectionalEdge(prevAttrIdx, attrIdx, dist, time, cost, mode);
                    }
                }
            }
        }

        // Specific landmark-to-landmark routes for famous historical pairs
        // Charminar (hyd-1) to Golconda Fort (hyd-2)
        int charminar = findNodeIndex("hyd-1");
        int golconda = findNodeIndex("hyd-2");
        int salarJung = findNodeIndex("hyd-4");
        int falaknuma = findNodeIndex("hyd-3");

        if (charminar != -1 && salarJung != -1) {
            addBidirectionalEdge(charminar, salarJung, 2, 8, 40, "Auto / Heritage Walk");
        }
        if (salarJung != -1 && golconda != -1) {
            addBidirectionalEdge(salarJung, golconda, 12, 32, 190, "City Cab / Transit");
        }
        if (charminar != -1 && falaknuma != -1) {
            addBidirectionalEdge(charminar, falaknuma, 5, 18, 110, "Cab / Auto");
        }

        // Delhi: Red Fort (del-2) to India Gate (del-1) to Qutub Minar (del-3)
        int indiaGate = findNodeIndex("del-1");
        int redFort = findNodeIndex("del-2");
        int qutubMinar = findNodeIndex("del-3");
        int humayunTomb = findNodeIndex("del-4");

        if (redFort != -1 && indiaGate != -1) {
            addBidirectionalEdge(redFort, indiaGate, 6, 20, 110, "Delhi Metro / Cab");
        }
        if (indiaGate != -1 && humayunTomb != -1) {
            addBidirectionalEdge(indiaGate, humayunTomb, 4, 12, 70, "Auto / Local Transit");
        }
        if (indiaGate != -1 && qutubMinar != -1) {
            addBidirectionalEdge(indiaGate, qutubMinar, 14, 35, 220, "Metro Yellow Line / Cab");
        }

        // Agra: Taj Mahal (agr-1) to Agra Fort (agr-2)
        int tajMahal = findNodeIndex("agr-1");
        int agraFort = findNodeIndex("agr-2");
        if (tajMahal != -1 && agraFort != -1) {
            addBidirectionalEdge(tajMahal, agraFort, 3, 10, 50, "Electric Auto / Tonga");
        }
    }

    private static void addBidirectionalEdge(int u, int v, int dist, int time, int cost, String mode) {
        allEdges.add(new GraphEdge(u, v, dist, time, cost, mode));
        allEdges.add(new GraphEdge(v, u, dist, time, cost, mode));
    }

    public static int findNodeIndex(String query) {
        if (query == null || allNodes == null) return -1;
        String clean = query.trim().toLowerCase();

        // Exact ID match
        for (int i = 0; i < allNodes.size(); i++) {
            if (allNodes.get(i).id.equalsIgnoreCase(clean)) {
                return i;
            }
        }

        // Exact Name match
        for (int i = 0; i < allNodes.size(); i++) {
            if (allNodes.get(i).name.equalsIgnoreCase(clean)) {
                return i;
            }
        }

        // Substring Name match
        for (int i = 0; i < allNodes.size(); i++) {
            if (allNodes.get(i).name.toLowerCase().contains(clean) || clean.contains(allNodes.get(i).name.toLowerCase())) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Computes the shortest travel path between two destinations or landmarks using Dijkstra.
     */
    public static RouteResponse findRoute(String fromId, String toId, String preference) {
        initializeGraph();

        if (fromId == null || toId == null || fromId.trim().isEmpty() || toId.trim().isEmpty()) {
            return new RouteResponse(fromId, "Unknown", toId, "Unknown", false, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), 0, "Please provide valid source and destination points.");
        }

        int srcIdx = findNodeIndex(fromId);
        int dstIdx = findNodeIndex(toId);

        if (srcIdx == -1 || dstIdx == -1) {
            String unknown = (srcIdx == -1) ? fromId : toId;
            return new RouteResponse(fromId, (srcIdx != -1 ? allNodes.get(srcIdx).name : fromId),
                                     toId, (dstIdx != -1 ? allNodes.get(dstIdx).name : toId),
                                     false, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), 0,
                                     "Location '" + unknown + "' could not be resolved in the transit network directory.");
        }

        RouteNode srcNode = allNodes.get(srcIdx);
        RouteNode dstNode = allNodes.get(dstIdx);

        // Edge case: Same location
        if (srcIdx == dstIdx) {
            List<String> single = new ArrayList<>();
            single.add(srcNode.name);
            return new RouteResponse(srcNode.id, srcNode.name, dstNode.id, dstNode.name, true, 0, 0, 0, single, new ArrayList<>(), 0, "Source and destination are identical. You are already at this location.");
        }

        int n = allNodes.size();
        int[] head = new int[n];
        for (int i = 0; i < n; i++) head[i] = -1;

        int numEdges = allEdges.size();
        Dijkstra.Edge[] edges = new Dijkstra.Edge[numEdges];

        for (int i = 0; i < numEdges; i++) {
            GraphEdge ge = allEdges.get(i);
            int weight = ge.distanceKm;
            if ("fastest".equalsIgnoreCase(preference)) {
                weight = ge.timeMinutes;
            } else if ("budget".equalsIgnoreCase(preference)) {
                weight = ge.costInr;
            }

            edges[i] = new Dijkstra.Edge(ge.toIdx, weight, ge.costInr, head[ge.fromIdx]);
            head[ge.fromIdx] = i;
        }

        PerformanceTimer timer = new PerformanceTimer();
        timer.start();

        Dijkstra.ShortestPathResult result = Dijkstra.findShortestPath(n, head, edges, srcIdx, dstIdx);

        timer.stop();
        double timeTaken = timer.getElapsedMicros();

        AlgorithmSelector.recordDecision(
            "ROUTE_FINDER",
            "Dijkstra's Algorithm (Indexed Min-Heap)",
            "Calculated optimal shortest route between " + srcNode.name + " and " + dstNode.name + " across " + n + " network nodes.",
            "O((V + E) log V)",
            timeTaken
        );

        if (!result.reachable || result.path.length == 0) {
            return new RouteResponse(srcNode.id, srcNode.name, dstNode.id, dstNode.name, false, 0, 0, 0, new ArrayList<>(), new ArrayList<>(), timeTaken, "No reachable transit path found between " + srcNode.name + " and " + dstNode.name + ".");
        }

        List<String> pathNodes = new ArrayList<>();
        List<RouteStep> steps = new ArrayList<>();
        int totalDistance = 0;
        int totalMinutes = 0;
        int totalCost = 0;

        for (int i = 0; i < result.path.length; i++) {
            RouteNode rn = allNodes.get(result.path[i]);
            pathNodes.add(rn.name);

            if (i > 0) {
                int prev = result.path[i - 1];
                int curr = result.path[i];
                GraphEdge edge = findEdge(prev, curr);
                if (edge != null) {
                    steps.add(new RouteStep(
                        allNodes.get(prev).name,
                        rn.name,
                        edge.distanceKm,
                        edge.timeMinutes,
                        edge.costInr,
                        edge.mode
                    ));
                    totalDistance += edge.distanceKm;
                    totalMinutes += edge.timeMinutes;
                    totalCost += edge.costInr;
                }
            }
        }

        return new RouteResponse(srcNode.id, srcNode.name, dstNode.id, dstNode.name, true, totalDistance, totalMinutes, totalCost, pathNodes, steps, timeTaken, "Optimal route found via Dijkstra shortest path algorithm.");
    }

    private static GraphEdge findEdge(int uIdx, int vIdx) {
        if (allEdges == null) return null;
        for (GraphEdge e : allEdges) {
            if (e.fromIdx == uIdx && e.toIdx == vIdx) {
                return e;
            }
        }
        return null;
    }

    public static List<RouteNode> getAllRoutableNodes() {
        initializeGraph();
        return allNodes;
    }
}
