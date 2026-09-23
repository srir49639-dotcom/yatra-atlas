/**
 * YATRA ATLAS - API CLIENT & CENTRAL HELPER
 */

const API_BASE = window.location.origin.includes('http') ? window.location.origin : 'http://localhost:8080';

const API = {
    async getDestinations() {
        const res = await fetch(`${API_BASE}/api/destinations`);
        return await res.json();
    },

    async getDestination(id) {
        const res = await fetch(`${API_BASE}/api/destination?id=${encodeURIComponent(id)}`);
        return await res.json();
    },

    async search(query) {
        const res = await fetch(`${API_BASE}/api/search?q=${encodeURIComponent(query)}`);
        return await res.json();
    },

    async getFuzzy(query) {
        const res = await fetch(`${API_BASE}/api/fuzzy?q=${encodeURIComponent(query)}`);
        return await res.json();
    },

    async getSimilar(id) {
        const res = await fetch(`${API_BASE}/api/similar?id=${encodeURIComponent(id)}`);
        return await res.json();
    },

    async compare(idA, idB) {
        const res = await fetch(`${API_BASE}/api/compare?a=${encodeURIComponent(idA)}&b=${encodeURIComponent(idB)}`);
        return await res.json();
    },

    async getTextInsights(id) {
        const res = await fetch(`${API_BASE}/api/text-insights?id=${encodeURIComponent(id)}`);
        return await res.json();
    },

    async getRoute(from, to, pref = 'shortest') {
        const res = await fetch(`${API_BASE}/api/route?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}&pref=${encodeURIComponent(pref)}`);
        return await res.json();
    },

    async planTrip(origin, destinations, days = 5, preference = 'comfort') {
        const res = await fetch(`${API_BASE}/api/plan-trip`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                origin: origin,
                destinations: destinations.join(','),
                days: String(days),
                preference: preference
            })
        });
        return await res.json();
    },

    async getTransportCapacity(from, to) {
        const res = await fetch(`${API_BASE}/api/transport-capacity?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}`);
        return await res.json();
    },

    async getNetworkOptimizer() {
        const res = await fetch(`${API_BASE}/api/network-optimizer`);
        return await res.json();
    },

    async discoverSurprise() {
        const res = await fetch(`${API_BASE}/api/discover`);
        return await res.json();
    },

    async getAnalytics() {
        const res = await fetch(`${API_BASE}/api/analytics`);
        return await res.json();
    },

    async testMillerRabin(n, k = 10) {
        const res = await fetch(`${API_BASE}/api/miller-rabin?n=${encodeURIComponent(n)}&k=${encodeURIComponent(k)}`);
        return await res.json();
    },

    async runBenchmark(queries = 800) {
        const res = await fetch(`${API_BASE}/api/benchmark?queries=${encodeURIComponent(queries)}`);
        return await res.json();
    },

    async getAlgorithmLogs() {
        const res = await fetch(`${API_BASE}/api/docs/algorithm-logs`);
        return await res.json();
    },

    async testAlgorithm(algorithm, text, pattern) {
        const res = await fetch(`${API_BASE}/api/docs/test-algorithm`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ algorithm, text, pattern })
        });
        return await res.json();
    },

    /**
     * Fallback placeholder generator ensuring no broken image or grey box ever shows.
     */
    handleImageError(img, cityName = 'India') {
        img.onerror = null;
        const colors = [
            ['#ea580c', '#c2410c'],
            ['#d97706', '#b45309'],
            ['#0284c7', '#0369a1'],
            ['#059669', '#047857'],
            ['#7c3aed', '#6d28d9']
        ];
        const hash = cityName.split('').reduce((acc, c) => acc + c.charCodeAt(0), 0);
        const [c1, c2] = colors[hash % colors.length];

        const svg = `<svg xmlns="http://www.w3.org/2000/svg" width="600" height="400" viewBox="0 0 600 400">
            <defs>
                <linearGradient id="g" x1="0%" y1="0%" x2="100%" y2="100%">
                    <stop offset="0%" stop-color="${c1}" />
                    <stop offset="100%" stop-color="${c2}" />
                </linearGradient>
            </defs>
            <rect width="600" height="400" fill="url(#g)" />
            <circle cx="300" cy="170" r="45" fill="rgba(255,255,255,0.2)" />
            <path d="M285 155 L315 155 L300 135 Z" fill="white" />
            <circle cx="300" cy="180" r="15" fill="white" />
            <text x="300" y="270" font-family="'Plus Jakarta Sans', sans-serif" font-size="28" font-weight="700" fill="#ffffff" text-anchor="middle">${cityName}</text>
            <text x="300" y="305" font-family="'Plus Jakarta Sans', sans-serif" font-size="16" font-weight="500" fill="rgba(255,255,255,0.8)" text-anchor="middle">Yatra Atlas Experience</text>
        </svg>`;

        img.src = 'data:image/svg+xml;utf8,' + encodeURIComponent(svg);
    }
};

window.API = API;
