package app;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import data.TravelData;
import model.*;
import services.*;
import utils.AlgorithmSelector;
import utils.SimpleJson;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Main HTTP Server & REST API Gateway for Yatra Atlas.
 * Integrates all DSA-3 algorithms with the customer travel layer and academic documentation.
 */
public class TravelGuideApp {

    private static final int PORT = 8080;
    private static final String FRONTEND_DIR = "frontend";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());

        // API Endpoints
        server.createContext("/api/destinations", new DestinationsHandler());
        server.createContext("/api/destination", new DestinationDetailHandler());
        server.createContext("/api/search", new SearchHandler());
        server.createContext("/api/fuzzy", new FuzzySearchHandler());
        server.createContext("/api/similar", new SimilarHandler());
        server.createContext("/api/compare", new CompareHandler());
        server.createContext("/api/text-insights", new TextInsightsHandler());
        server.createContext("/api/route", new RouteHandler());
        server.createContext("/api/route/nodes", new RouteNodesHandler());
        server.createContext("/api/plan-trip", new TripPlannerHandler());
        server.createContext("/api/transport-capacity", new TransportCapacityHandler());
        server.createContext("/api/network-optimizer", new NetworkOptimizerHandler());
        server.createContext("/api/discover", new DiscoverHandler());
        server.createContext("/api/analytics", new AnalyticsHandler());
        server.createContext("/api/miller-rabin", new MillerRabinHandler());
        server.createContext("/api/benchmark", new BenchmarkHandler());
        server.createContext("/api/benchmark/suite", new BenchmarkSuiteHandler());
        server.createContext("/api/tests/run", new TestSuiteRunnerHandler());
        server.createContext("/api/docs/algorithm-logs", new AlgorithmLogsHandler());
        server.createContext("/api/docs/test-algorithm", new AlgorithmTesterHandler());

        // Static Web Server Handler
        server.createContext("/", new StaticFileHandler());

        server.start();
        System.out.println("===============================================================");
        System.out.println("  YATRA ATLAS - SMART TRAVEL GUIDE (DSA-3 ENGINE)");
        System.out.println("  Author: Sriram | Academic DSA-3 Full-Stack Implementation");
        System.out.println("===============================================================");
        System.out.println("  Backend Server listening on: http://localhost:" + PORT);
        System.out.println("  Customer Travel Portal:      http://localhost:" + PORT + "/index.html");
        System.out.println("  Project & DSA Viva Docs:     http://localhost:" + PORT + "/pages/docs.html");
        System.out.println("===============================================================");
    }

    // ==========================================
    // HTTP Handlers
    // ==========================================

    private static void appendDestinationJson(StringBuilder sb, Destination d) {
        sb.append("{")
          .append("\"id\":\"").append(d.getId()).append("\",")
          .append("\"name\":\"").append(SimpleJson.escape(d.getName())).append("\",")
          .append("\"state\":\"").append(SimpleJson.escape(d.getState())).append("\",")
          .append("\"category\":\"").append(SimpleJson.escape(d.getCategory())).append("\",")
          .append("\"description\":\"").append(SimpleJson.escape(d.getDescription())).append("\",")
          .append("\"image\":\"").append(SimpleJson.escape(d.getImage())).append("\",")
          .append("\"latitude\":").append(d.getLatitude()).append(",")
          .append("\"longitude\":").append(d.getLongitude()).append(",")
          .append("\"avgDailyBudget\":").append(d.getAvgDailyBudget()).append(",")
          .append("\"bestTimeToVisit\":\"").append(SimpleJson.escape(d.getBestTimeToVisit())).append("\",")
          .append("\"attractionsCount\":").append(d.getAttractions().length).append(",")
          .append("\"hotelsCount\":").append(d.getHotels().length).append(",")
          .append("\"restaurantsCount\":").append(d.getRestaurants().length).append(",");

        // Attractions
        sb.append("\"attractions\":[");
        for (int i = 0; i < d.getAttractions().length; i++) {
            Attraction a = d.getAttractions()[i];
            if (i > 0) sb.append(",");
            sb.append("{")
              .append("\"id\":\"").append(a.getId()).append("\",")
              .append("\"destinationId\":\"").append(d.getId()).append("\",")
              .append("\"name\":\"").append(SimpleJson.escape(a.getName())).append("\",")
              .append("\"category\":\"").append(SimpleJson.escape(a.getCategory())).append("\",")
              .append("\"description\":\"").append(SimpleJson.escape(a.getDescription())).append("\",")
              .append("\"entryFee\":").append(a.getEntryFee()).append(",")
              .append("\"rating\":").append(a.getRating()).append(",")
              .append("\"openingHours\":\"").append(SimpleJson.escape(a.getOpeningHours())).append("\",")
              .append("\"image\":\"").append(SimpleJson.escape(a.getImage())).append("\"")
              .append("}");
        }
        sb.append("],");

        // Hotels
        sb.append("\"hotels\":[");
        for (int i = 0; i < d.getHotels().length; i++) {
            Hotel h = d.getHotels()[i];
            if (i > 0) sb.append(",");
            sb.append("{")
              .append("\"id\":\"").append(h.getId()).append("\",")
              .append("\"destinationId\":\"").append(d.getId()).append("\",")
              .append("\"name\":\"").append(SimpleJson.escape(h.getName())).append("\",")
              .append("\"tier\":\"").append(SimpleJson.escape(h.getTier())).append("\",")
              .append("\"pricePerNight\":").append(h.getPricePerNight()).append(",")
              .append("\"rating\":").append(h.getRating()).append(",")
              .append("\"address\":\"").append(SimpleJson.escape(h.getAddress())).append("\",")
              .append("\"amenities\":[");
            for (int k = 0; k < h.getAmenities().length; k++) {
                if (k > 0) sb.append(",");
                sb.append("\"").append(SimpleJson.escape(h.getAmenities()[k])).append("\"");
            }
            sb.append("]}");
        }
        sb.append("],");

        // Restaurants
        sb.append("\"restaurants\":[");
        for (int i = 0; i < d.getRestaurants().length; i++) {
            Restaurant r = d.getRestaurants()[i];
            if (i > 0) sb.append(",");
            sb.append("{")
              .append("\"id\":\"").append(r.getId()).append("\",")
              .append("\"destinationId\":\"").append(d.getId()).append("\",")
              .append("\"name\":\"").append(SimpleJson.escape(r.getName())).append("\",")
              .append("\"cuisine\":\"").append(SimpleJson.escape(r.getCuisine())).append("\",")
              .append("\"avgCostForTwo\":").append(r.getAvgCostForTwo()).append(",")
              .append("\"rating\":").append(r.getRating()).append(",")
              .append("\"famousDish\":\"").append(SimpleJson.escape(r.getFamousDish())).append("\",")
              .append("\"address\":\"").append(SimpleJson.escape(r.getAddress())).append("\"")
              .append("}");
        }
        sb.append("],");

        // Local Transports
        sb.append("\"transports\":[");
        for (int i = 0; i < d.getTransports().length; i++) {
            Transport t = d.getTransports()[i];
            if (i > 0) sb.append(",");
            sb.append("{")
              .append("\"id\":\"").append(t.getId()).append("\",")
              .append("\"type\":\"").append(SimpleJson.escape(t.getType())).append("\",")
              .append("\"provider\":\"").append(SimpleJson.escape(t.getProvider())).append("\",")
              .append("\"frequency\":\"").append(SimpleJson.escape(t.getFrequency())).append("\",")
              .append("\"avgPrice\":").append(t.getAvgPrice()).append(",")
              .append("\"description\":\"").append(SimpleJson.escape(t.getDescription())).append("\"")
              .append("}");
        }
        sb.append("]");

        sb.append("}");
    }

    private static class DestinationsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            StringBuilder sb = new StringBuilder();
            sb.append("[");
            Destination[] dests = TravelData.DESTINATIONS;
            for (int i = 0; i < dests.length; i++) {
                if (i > 0) sb.append(",");
                appendDestinationJson(sb, dests[i]);
            }
            sb.append("]");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class DestinationDetailHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
            String id = query.get("id");

            Destination d = TravelData.getDestinationById(id);
            if (d == null) {
                sendJsonResponse(exchange, 404, "{\"error\":\"Destination not found\"}");
                return;
            }

            StringBuilder sb = new StringBuilder();
            appendDestinationJson(sb, d);
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class SearchHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
            String q = query.getOrDefault("q", "");

            var resp = SearchService.search(q);
            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"query\":\"").append(SimpleJson.escape(resp.query)).append("\",")
              .append("\"algorithmUsed\":\"").append(SimpleJson.escape(resp.algorithmUsed)).append("\",")
              .append("\"executionTimeMicros\":").append(resp.executionTimeMicros).append(",")
              .append("\"didYouMean\":").append(resp.didYouMean != null ? "\"" + SimpleJson.escape(resp.didYouMean) + "\"" : "null").append(",")
              .append("\"results\":[");

            for (int i = 0; i < resp.results.size(); i++) {
                var r = resp.results.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"destinationId\":\"").append(r.destinationId).append("\",")
                  .append("\"destinationName\":\"").append(SimpleJson.escape(r.destinationName)).append("\",")
                  .append("\"state\":\"").append(SimpleJson.escape(r.state)).append("\",")
                  .append("\"itemType\":\"").append(SimpleJson.escape(r.itemType)).append("\",")
                  .append("\"title\":\"").append(SimpleJson.escape(r.title)).append("\",")
                  .append("\"subtitle\":\"").append(SimpleJson.escape(r.subtitle)).append("\",")
                  .append("\"snippet\":\"").append(SimpleJson.escape(r.snippet)).append("\",")
                  .append("\"image\":\"").append(SimpleJson.escape(r.image)).append("\",")
                  .append("\"category\":\"").append(SimpleJson.escape(r.category)).append("\"")
                  .append("}");
            }
            sb.append("]}");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class FuzzySearchHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
            String q = query.getOrDefault("q", "");

            var matches = FuzzySearchService.getFuzzyMatches(q, 5);
            String suggestion = FuzzySearchService.suggestCorrection(q);

            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"query\":\"").append(SimpleJson.escape(q)).append("\",")
              .append("\"suggestion\":").append(suggestion != null ? "\"" + SimpleJson.escape(suggestion) + "\"" : "null").append(",")
              .append("\"matches\":[");

            for (int i = 0; i < matches.size(); i++) {
                var m = matches.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"term\":\"").append(SimpleJson.escape(m.term)).append("\",")
                  .append("\"distance\":").append(m.distance).append(",")
                  .append("\"similarity\":").append(Math.round(m.similarity * 100.0) / 100.0).append(",")
                  .append("\"algorithm\":\"").append(SimpleJson.escape(m.algorithm)).append("\"")
                  .append("}");
            }
            sb.append("]}");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class SimilarHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
            String id = query.get("id");

            var similar = SimilarityService.findSimilarDestinations(id, 4);
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < similar.size(); i++) {
                var s = similar.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"id\":\"").append(s.destination.getId()).append("\",")
                  .append("\"name\":\"").append(SimpleJson.escape(s.destination.getName())).append("\",")
                  .append("\"state\":\"").append(SimpleJson.escape(s.destination.getState())).append("\",")
                  .append("\"category\":\"").append(SimpleJson.escape(s.destination.getCategory())).append("\",")
                  .append("\"image\":\"").append(SimpleJson.escape(s.destination.getImage())).append("\",")
                  .append("\"score\":").append(Math.round(s.score * 100.0) / 100.0).append(",")
                  .append("\"reason\":\"").append(SimpleJson.escape(s.matchReason)).append("\"")
                  .append("}");
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class CompareHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
            String a = query.containsKey("a") ? query.get("a") : query.get("destA");
            String b = query.containsKey("b") ? query.get("b") : query.get("destB");

            if (a == null || b == null || a.equalsIgnoreCase(b)) {
                sendJsonResponse(exchange, 400, "{\"error\":\"Please specify two distinct destination IDs to compare.\"}");
                return;
            }

            var report = SimilarityService.compareDestinations(a, b);
            if (report == null) {
                sendJsonResponse(exchange, 400, "{\"error\":\"Invalid destination IDs provided.\"}");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"destA\":{")
                .append("\"id\":\"").append(report.destA.getId()).append("\",")
                .append("\"name\":\"").append(SimpleJson.escape(report.destA.getName())).append("\",")
                .append("\"state\":\"").append(SimpleJson.escape(report.destA.getState())).append("\",")
                .append("\"category\":\"").append(SimpleJson.escape(report.destA.getCategory())).append("\",")
                .append("\"image\":\"").append(SimpleJson.escape(report.destA.getImage())).append("\",")
                .append("\"avgDailyBudget\":").append(report.destA.getAvgDailyBudget()).append(",")
                .append("\"bestTimeToVisit\":\"").append(SimpleJson.escape(report.destA.getBestTimeToVisit())).append("\",")
                .append("\"attractionsCount\":").append(report.destA.getAttractions().length)
              .append("},")
              .append("\"destB\":{")
                .append("\"id\":\"").append(report.destB.getId()).append("\",")
                .append("\"name\":\"").append(SimpleJson.escape(report.destB.getName())).append("\",")
                .append("\"state\":\"").append(SimpleJson.escape(report.destB.getState())).append("\",")
                .append("\"category\":\"").append(SimpleJson.escape(report.destB.getCategory())).append("\",")
                .append("\"image\":\"").append(SimpleJson.escape(report.destB.getImage())).append("\",")
                .append("\"avgDailyBudget\":").append(report.destB.getAvgDailyBudget()).append(",")
                .append("\"bestTimeToVisit\":\"").append(SimpleJson.escape(report.destB.getBestTimeToVisit())).append("\",")
                .append("\"attractionsCount\":").append(report.destB.getAttractions().length)
              .append("},")
              .append("\"overallSimilarity\":").append(Math.round(report.overallSimilarity * 100.0) / 100.0).append(",")
              .append("\"sharedThemes\":\"").append(SimpleJson.escape(report.sharedThemes)).append("\",")
              .append("\"needlemanWunsch\":{")
                .append("\"score\":").append(report.globalAlignment.score).append(",")
                .append("\"normalized\":").append(Math.round(report.globalAlignment.normalizedSimilarity * 100.0) / 100.0)
              .append("},")
              .append("\"smithWaterman\":{")
                .append("\"score\":").append(report.localAlignment.maxScore).append(",")
                .append("\"subSeq1\":\"").append(SimpleJson.escape(report.localAlignment.subSeq1)).append("\",")
                .append("\"subSeq2\":\"").append(SimpleJson.escape(report.localAlignment.subSeq2)).append("\"")
              .append("},")
              .append("\"timeTakenMicros\":").append(report.timeTakenMicros)
              .append("}");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class TextInsightsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
            String id = query.get("id");

            var insights = TextAnalysisService.analyzeDestination(id);
            if (insights == null) {
                sendJsonResponse(exchange, 404, "{\"error\":\"Destination not found\"}");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"destinationId\":\"").append(insights.destinationId).append("\",")
              .append("\"destinationName\":\"").append(SimpleJson.escape(insights.destinationName)).append("\",")
              .append("\"longestRepeatedPhrase\":\"").append(SimpleJson.escape(insights.longestRepeatedPhrase)).append("\",")
              .append("\"analysisTimeMicros\":").append(insights.analysisTimeMicros).append(",")
              .append("\"recurringPhrases\":[");

            for (int i = 0; i < insights.recurringPhrases.length; i++) {
                if (i > 0) sb.append(",");
                sb.append("\"").append(SimpleJson.escape(insights.recurringPhrases[i])).append("\"");
            }
            sb.append("]}");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class RouteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
            String from = query.get("from");
            String to = query.get("to");
            String pref = query.getOrDefault("pref", "shortest");

            var resp = RouteService.findRoute(from, to, pref);
            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"reachable\":").append(resp.reachable).append(",")
              .append("\"message\":\"").append(SimpleJson.escape(resp.message)).append("\",")
              .append("\"fromName\":\"").append(SimpleJson.escape(resp.fromName)).append("\",")
              .append("\"toName\":\"").append(SimpleJson.escape(resp.toName)).append("\",")
              .append("\"totalDistanceKm\":").append(resp.totalDistanceKm).append(",")
              .append("\"totalTimeMinutes\":").append(resp.totalTimeMinutes).append(",")
              .append("\"totalCostInr\":").append(resp.totalCostInr).append(",")
              .append("\"calculationTimeMicros\":").append(resp.calculationTimeMicros).append(",")
              .append("\"pathCities\":[");

            for (int i = 0; i < resp.pathCities.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append("\"").append(SimpleJson.escape(resp.pathCities.get(i))).append("\"");
            }
            sb.append("],\"legs\":[");

            for (int i = 0; i < resp.legs.size(); i++) {
                var leg = resp.legs.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"fromCity\":\"").append(SimpleJson.escape(leg.fromCity)).append("\",")
                  .append("\"toCity\":\"").append(SimpleJson.escape(leg.toCity)).append("\",")
                  .append("\"distanceKm\":").append(leg.distanceKm).append(",")
                  .append("\"timeMinutes\":").append(leg.timeMinutes).append(",")
                  .append("\"costInr\":").append(leg.costInr).append(",")
                  .append("\"mode\":\"").append(SimpleJson.escape(leg.mode)).append("\"")
                  .append("}");
            }
            sb.append("]}");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class RouteNodesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            var nodes = RouteService.getAllRoutableNodes();
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < nodes.size(); i++) {
                var n = nodes.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"id\":\"").append(n.id).append("\",")
                  .append("\"name\":\"").append(SimpleJson.escape(n.name)).append("\",")
                  .append("\"type\":\"").append(SimpleJson.escape(n.type)).append("\",")
                  .append("\"parentCityId\":\"").append(SimpleJson.escape(n.parentCityId)).append("\"")
                  .append("}");
            }
            sb.append("]");
            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class TripPlannerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            String body = readBody(exchange);
            Map<String, String> params = SimpleJson.parseSimpleObject(body);

            String origin = params.getOrDefault("origin", "hyderabad");
            String destsStr = params.getOrDefault("destinations", "bengaluru,chennai,goa");
            destsStr = destsStr.replace("[", "").replace("]", "").replace("\"", "");
            String[] dests = destsStr.split(",");
            for (int i = 0; i < dests.length; i++) dests[i] = dests[i].trim();

            int days = 5;
            try { days = Integer.parseInt(params.getOrDefault("days", "5")); } catch (Exception e) {}
            String pref = params.getOrDefault("preference", "comfort");

            var trip = TripPlannerService.planTrip(origin, dests, days, pref);
            if (trip == null) {
                sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"Could not plan trip with provided inputs\"}");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"success\":").append(trip.success).append(",")
              .append("\"message\":\"").append(SimpleJson.escape(trip.message)).append("\",")
              .append("\"originName\":\"").append(SimpleJson.escape(trip.originName)).append("\",")
              .append("\"totalStops\":").append(trip.totalStops).append(",")
              .append("\"totalDistanceKm\":").append(trip.totalDistanceKm).append(",")
              .append("\"totalCostInr\":").append(trip.totalCostInr).append(",")
              .append("\"totalDays\":").append(trip.totalDays).append(",")
              .append("\"calculationTimeMicros\":").append(trip.calculationTimeMicros).append(",")
              .append("\"optimalSequence\":[");

            for (int i = 0; i < trip.optimalSequence.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append("\"").append(SimpleJson.escape(trip.optimalSequence.get(i))).append("\"");
            }
            sb.append("],\"schedule\":[");

            for (int i = 0; i < trip.schedule.size(); i++) {
                var day = trip.schedule.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"dayNumber\":").append(day.dayNumber).append(",")
                  .append("\"destinationId\":\"").append(day.destination.getId()).append("\",")
                  .append("\"destinationName\":\"").append(SimpleJson.escape(day.destination.getName())).append("\",")
                  .append("\"image\":\"").append(SimpleJson.escape(day.destination.getImage())).append("\",")
                  .append("\"action\":\"").append(SimpleJson.escape(day.action)).append("\",")
                  .append("\"dailyTravelKm\":").append(day.dailyTravelKm).append(",")
                  .append("\"cumulativeKm\":").append(day.cumulativeKm).append(",")
                  .append("\"estimatedDailyCost\":").append(day.estimatedDailyCost).append(",")
                  .append("\"cumulativeCost\":").append(day.cumulativeCost).append(",")
                  .append("\"highlight\":\"").append(SimpleJson.escape(day.highlight)).append("\"")
                  .append("}");
            }
            sb.append("]}");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class TransportCapacityHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
            String from = query.getOrDefault("from", "hyderabad");
            String to = query.getOrDefault("to", "goa");

            var result = TransportService.analyzeCapacity(from, to);
            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"sourceCity\":\"").append(SimpleJson.escape(result.sourceCity)).append("\",")
              .append("\"sinkCity\":\"").append(SimpleJson.escape(result.sinkCity)).append("\",")
              .append("\"maxDailySeatCapacity\":").append(result.maxDailySeatCapacity).append(",")
              .append("\"augmentingPathsCount\":").append(result.augmentingPathsCount).append(",")
              .append("\"capacityStatus\":\"").append(SimpleJson.escape(result.capacityStatus)).append("\",")
              .append("\"computationTimeMicros\":").append(result.computationTimeMicros).append(",")
              .append("\"criticalCorridors\":[");

            for (int i = 0; i < result.criticalCorridors.size(); i++) {
                var c = result.criticalCorridors.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"fromCity\":\"").append(SimpleJson.escape(c.fromCity)).append("\",")
                  .append("\"toCity\":\"").append(SimpleJson.escape(c.toCity)).append("\",")
                  .append("\"capacitySeats\":").append(c.capacitySeats).append(",")
                  .append("\"impactNote\":\"").append(SimpleJson.escape(c.impactNote)).append("\"")
                  .append("}");
            }
            sb.append("]}");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class NetworkOptimizerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            var res = TransportService.optimizeNetworkHubs();
            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"totalCoveredConnections\":").append(res.totalCoveredConnections).append(",")
              .append("\"approximationGuarantee\":").append(res.approximationGuarantee).append(",")
              .append("\"description\":\"").append(SimpleJson.escape(res.description)).append("\",")
              .append("\"timeTakenMicros\":").append(res.timeTakenMicros).append(",")
              .append("\"strategicHubs\":[");

            for (int i = 0; i < res.strategicHubs.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append("\"").append(SimpleJson.escape(res.strategicHubs.get(i))).append("\"");
            }
            sb.append("]}");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class DiscoverHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            var rec = RecommendationService.surpriseMe();
            Destination d = rec.destination;

            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"id\":\"").append(d.getId()).append("\",")
              .append("\"name\":\"").append(SimpleJson.escape(d.getName())).append("\",")
              .append("\"state\":\"").append(SimpleJson.escape(d.getState())).append("\",")
              .append("\"category\":\"").append(SimpleJson.escape(d.getCategory())).append("\",")
              .append("\"description\":\"").append(SimpleJson.escape(d.getDescription())).append("\",")
              .append("\"image\":\"").append(SimpleJson.escape(d.getImage())).append("\",")
              .append("\"avgDailyBudget\":").append(d.getAvgDailyBudget()).append(",")
              .append("\"bestTimeToVisit\":\"").append(SimpleJson.escape(d.getBestTimeToVisit())).append("\",")
              .append("\"recommendationReason\":\"").append(SimpleJson.escape(rec.recommendationReason)).append("\",")
              .append("\"selectionTimeMicros\":").append(rec.selectionTimeMicros)
              .append("}");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class AnalyticsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            var a = AnalyticsService.getPlatformAnalytics();
            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"totalDestinations\":").append(a.totalDestinations).append(",")
              .append("\"totalAttractions\":").append(a.totalAttractions).append(",")
              .append("\"totalHotels\":").append(a.totalHotels).append(",")
              .append("\"totalRestaurants\":").append(a.totalRestaurants).append(",")
              .append("\"totalIntercityRoutes\":").append(a.totalIntercityRoutes).append(",")
              .append("\"minDailyBudget\":").append(a.minDailyBudget).append(",")
              .append("\"maxDailyBudget\":").append(a.maxDailyBudget).append(",")
              .append("\"avgDailyBudget\":").append(a.avgDailyBudget).append(",")
              .append("\"computeTimeMicros\":").append(a.computeTimeMicros).append(",")
              .append("\"categoryDistribution\":{");

            int count = 0;
            for (var entry : a.categoryDistribution.entrySet()) {
                if (count > 0) sb.append(",");
                sb.append("\"").append(SimpleJson.escape(entry.getKey())).append("\":").append(entry.getValue());
                count++;
            }
            sb.append("},\"samplePrefixDistances\":[");
            for (int i = 0; i < a.sampleItineraryPrefixDistances.length; i++) {
                if (i > 0) sb.append(",");
                sb.append(a.sampleItineraryPrefixDistances[i]);
            }
            sb.append("]}");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class MillerRabinHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
            long n = 1000000007L;
            int k = 10;
            try {
                if (query.containsKey("n")) n = Long.parseLong(query.get("n"));
                if (query.containsKey("k")) k = Integer.parseInt(query.get("k"));
            } catch (Exception e) {}

            var res = algorithms.randomized.MillerRabin.test(n, k);
            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"number\":").append(res.number).append(",")
              .append("\"isPrime\":").append(res.isPrime).append(",")
              .append("\"classification\":\"").append(SimpleJson.escape(res.classification)).append("\",")
              .append("\"iterationsUsed\":").append(res.iterationsUsed).append(",")
              .append("\"errorProbability\":").append(res.errorProbability).append(",")
              .append("\"witnessDetails\":\"").append(SimpleJson.escape(res.witnessWitnessed)).append("\"")
              .append("}");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class BenchmarkHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            Map<String, String> query = parseQueryParams(exchange.getRequestURI().getQuery());
            int queries = 800;
            try {
                if (query.containsKey("queries")) queries = Integer.parseInt(query.get("queries"));
            } catch (Exception e) {}

            var b = ParallelBenchmarkService.runBenchmark(queries);
            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"totalQueriesRun\":").append(b.totalQueriesRun).append(",")
              .append("\"corpusLengthChars\":").append(b.corpusLengthChars).append(",")
              .append("\"threadCount\":").append(b.threadCount).append(",")
              .append("\"sequentialTimeMillis\":").append(b.sequentialTimeMillis).append(",")
              .append("\"parallelTimeMillis\":").append(b.parallelTimeMillis).append(",")
              .append("\"speedupFactor\":").append(b.speedupFactor).append(",")
              .append("\"sequentialThroughputQps\":").append(b.sequentialThroughputQps).append(",")
              .append("\"parallelThroughputQps\":").append(b.parallelThroughputQps)
              .append("}");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class BenchmarkSuiteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            var suite = PerformanceBenchmarkService.runFullSuite();
            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"totalSuiteTimeMillis\":").append(suite.totalSuiteTimeMillis).append(",")
              .append("\"searchScaling\":[");

            for (int i = 0; i < suite.searchScaling.size(); i++) {
                var r = suite.searchScaling.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"algorithm\":\"").append(SimpleJson.escape(r.algorithm)).append("\",")
                  .append("\"inputSize\":").append(r.inputSize).append(",")
                  .append("\"executionTimeMicros\":").append(r.executionTimeMicros).append(",")
                  .append("\"matchCount\":").append(r.matchCount).append(",")
                  .append("\"correct\":").append(r.correct)
                  .append("}");
            }

            sb.append("],\"familyBenchmarks\":[");
            for (int i = 0; i < suite.familyBenchmarks.size(); i++) {
                var r = suite.familyBenchmarks.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"category\":\"").append(SimpleJson.escape(r.category)).append("\",")
                  .append("\"algorithm\":\"").append(SimpleJson.escape(r.algorithm)).append("\",")
                  .append("\"inputDescription\":\"").append(SimpleJson.escape(r.inputDescription)).append("\",")
                  .append("\"executionTimeMicros\":").append(r.executionTimeMicros).append(",")
                  .append("\"resultSummary\":\"").append(SimpleJson.escape(r.resultSummary)).append("\"")
                  .append("}");
            }
            sb.append("]}");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class TestSuiteRunnerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            var suiteResult = tests.DSATestSuite.runAllTests();
            StringBuilder sb = new StringBuilder();
            sb.append("{")
              .append("\"totalTests\":").append(suiteResult.totalTests).append(",")
              .append("\"passed\":").append(suiteResult.passed).append(",")
              .append("\"failed\":").append(suiteResult.failed).append(",")
              .append("\"passPercentage\":").append(suiteResult.passPercentage).append(",")
              .append("\"totalTimeMillis\":").append(suiteResult.totalTimeMillis).append(",")
              .append("\"testCases\":[");

            for (int i = 0; i < suiteResult.testCases.size(); i++) {
                var tc = suiteResult.testCases.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"category\":\"").append(SimpleJson.escape(tc.category)).append("\",")
                  .append("\"name\":\"").append(SimpleJson.escape(tc.testName)).append("\",")
                  .append("\"passed\":").append(tc.passed).append(",")
                  .append("\"timeMicros\":").append(tc.timeMicros).append(",")
                  .append("\"details\":\"").append(SimpleJson.escape(tc.details)).append("\"")
                  .append("}");
            }
            sb.append("]}");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class AlgorithmLogsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            var logs = AlgorithmSelector.getRecentDecisions();
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < logs.size(); i++) {
                var l = logs.get(i);
                if (i > 0) sb.append(",");
                sb.append("{")
                  .append("\"timestamp\":").append(l.timestamp).append(",")
                  .append("\"feature\":\"").append(SimpleJson.escape(l.feature)).append("\",")
                  .append("\"selectedAlgorithm\":\"").append(SimpleJson.escape(l.selectedAlgorithm)).append("\",")
                  .append("\"rationale\":\"").append(SimpleJson.escape(l.rationale)).append("\",")
                  .append("\"complexity\":\"").append(SimpleJson.escape(l.complexity)).append("\",")
                  .append("\"timeTakenMicros\":").append(l.timeTakenMicros)
                  .append("}");
            }
            sb.append("]");

            sendJsonResponse(exchange, 200, sb.toString());
        }
    }

    private static class AlgorithmTesterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            String body = readBody(exchange);
            Map<String, String> map = SimpleJson.parseSimpleObject(body);

            String algo = map.getOrDefault("algorithm", "KMP").toUpperCase();
            String text = map.getOrDefault("text", "Hyderabad Charminar Heritage Fort");
            String pattern = map.getOrDefault("pattern", "Charminar");

            long start = System.nanoTime();
            String output = "";

            switch (algo) {
                case "NAIVE":
                    int[] naiveMatches = algorithms.string.NaiveSearch.search(text, pattern);
                    output = "Found at indices: " + Arrays.toString(naiveMatches);
                    break;
                case "KMP":
                    int[] kmpMatches = algorithms.string.KMP.search(text, pattern);
                    output = "Found at indices: " + Arrays.toString(kmpMatches) + " | LPS Table: " + Arrays.toString(algorithms.string.KMP.computeLPS(pattern));
                    break;
                case "Z_ALGORITHM":
                    int[] zMatches = algorithms.string.ZAlgorithm.search(text, pattern);
                    output = "Found at indices: " + Arrays.toString(zMatches);
                    break;
                case "RABIN_KARP":
                    int[] rkMatches = algorithms.string.RabinKarp.search(text, pattern);
                    output = "Found at indices: " + Arrays.toString(rkMatches);
                    break;
                case "SUFFIX_ARRAY":
                    int[] sa = algorithms.string.SuffixArray.buildSuffixArray(text);
                    int[] lcp = algorithms.string.LCP.buildLCPArray(text, sa);
                    output = "Suffix Array: " + Arrays.toString(sa) + " | LCP: " + Arrays.toString(lcp);
                    break;
                case "LEVENSHTEIN":
                    int lDist = algorithms.dp.Levenshtein.distance(text, pattern);
                    output = "Levenshtein Distance: " + lDist + " | Normalized Similarity: " + (Math.round(algorithms.dp.Levenshtein.similarity(text, pattern)*100.0)/100.0);
                    break;
                case "DAMERAU_LEVENSHTEIN":
                    int dlDist = algorithms.dp.DamerauLevenshtein.distance(text, pattern);
                    output = "Damerau-Levenshtein Distance: " + dlDist + " (Transpositions counted as 1 edit)";
                    break;
                case "NEEDLEMAN_WUNSCH":
                    var nw = algorithms.dp.NeedlemanWunsch.align(text, pattern);
                    output = "Global Alignment Score: " + nw.score + "\nSeq1: " + nw.alignedSeq1 + "\nSeq2: " + nw.alignedSeq2;
                    break;
                case "SMITH_WATERMAN":
                    var sw = algorithms.dp.SmithWaterman.align(text, pattern);
                    output = "Local Alignment Score: " + sw.maxScore + "\nMatched 1: " + sw.subSeq1 + "\nMatched 2: " + sw.subSeq2;
                    break;
                default:
                    output = "Unknown algorithm: " + algo;
                    break;
            }

            long elapsedNano = System.nanoTime() - start;
            double micros = elapsedNano / 1000.0;

            String json = "{\"algorithm\":\"" + SimpleJson.escape(algo) + "\",\"output\":\"" + SimpleJson.escape(output) + "\",\"executionTimeMicros\":" + micros + "}";
            sendJsonResponse(exchange, 200, json);
        }
    }

    // ==========================================
    // Static File Server
    // ==========================================

    private static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;

            String path = exchange.getRequestURI().getPath();
            if (path == null || path.equals("/") || path.isEmpty()) {
                path = "/index.html";
            }

            // Secure path traversal check
            Path filePath = Paths.get(FRONTEND_DIR, path.replace('/', File.separatorChar)).normalize();
            if (!filePath.startsWith(Paths.get(FRONTEND_DIR))) {
                sendJsonResponse(exchange, 403, "Forbidden");
                return;
            }

            File file = filePath.toFile();
            if (!file.exists() || file.isDirectory()) {
                // Try looking in pages/ directory
                File pageFile = Paths.get(FRONTEND_DIR, "pages", file.getName()).toFile();
                if (pageFile.exists() && !pageFile.isDirectory()) {
                    file = pageFile;
                } else {
                    sendJsonResponse(exchange, 404, "404 Not Found");
                    return;
                }
            }

            String mime = getMimeType(file.getName());
            byte[] bytes = Files.readAllBytes(file.toPath());

            exchange.getResponseHeaders().set("Content-Type", mime);
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().set("Cache-Control", "no-cache, no-store, must-revalidate");
            exchange.sendResponseHeaders(200, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    // ==========================================
    // Utilities
    // ==========================================

    private static boolean handleOptions(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return true;
        }
        return false;
    }

    private static void sendJsonResponse(HttpExchange exchange, int statusCode, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) return params;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            try {
                if (idx > 0) {
                    String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
                    String val = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                    params.put(key, val);
                } else {
                    params.put(URLDecoder.decode(pair, StandardCharsets.UTF_8), "");
                }
            } catch (Exception e) {}
        }
        return params;
    }

    private static String readBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private static String getMimeType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".html")) return "text/html; charset=UTF-8";
        if (lower.endsWith(".css")) return "text/css; charset=UTF-8";
        if (lower.endsWith(".js")) return "application/javascript; charset=UTF-8";
        if (lower.endsWith(".json")) return "application/json; charset=UTF-8";
        if (lower.endsWith(".svg")) return "image/svg+xml";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".ico")) return "image/x-icon";
        return "text/plain; charset=UTF-8";
    }
}
