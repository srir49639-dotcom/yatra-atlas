/**
 * YATRA ATLAS - DESTINATION DETAILS PAGE LOGIC
 */

document.addEventListener('DOMContentLoaded', async () => {
    // Mobile navigation
    const mobileToggle = document.getElementById('mobile-toggle');
    const navMenu = document.getElementById('nav-menu');
    if (mobileToggle && navMenu) {
        mobileToggle.addEventListener('click', () => navMenu.classList.toggle('open'));
    }

    const params = new URLSearchParams(window.location.search);
    const destId = params.get('id') || 'hyderabad';

    try {
        const dest = await API.getDestination(destId);
        if (!dest || dest.error) {
            document.body.innerHTML = `
                <div style="text-align: center; padding: 5rem 1rem;">
                    <h2>Destination Not Found</h2>
                    <p>Could not find destination "${destId}".</p>
                    <a href="destinations.html" class="search-btn" style="display: inline-block; margin-top: 1rem;">Back to Explore</a>
                </div>
            `;
            return;
        }

        renderDetails(dest);

        // Load Text Insights (Suffix Array + LCP)
        loadTextInsights(dest.id);

        // Load Similar Destinations
        loadSimilarDestinations(dest.id);

    } catch (e) {
        console.error('Error loading destination:', e);
    }
});

function renderDetails(d) {
    document.title = `${d.name} Travel Guide | Yatra Atlas`;

    // Hero
    const banner = document.getElementById('detail-hero-banner');
    if (banner) {
        banner.style.backgroundImage = `url('${d.image}')`;
    }

    document.getElementById('dest-name').textContent = d.name.toUpperCase();
    document.getElementById('dest-location-text').textContent = `${d.state}, India`;
    document.getElementById('dest-timing-text').textContent = `Best season: ${d.bestTimeToVisit}`;

    const badge = document.getElementById('dest-category-badge');
    badge.textContent = d.category;
    badge.className = `card-badge ${d.category.toLowerCase()}`;

    // Action buttons
    document.getElementById('btn-route-action').href = `route.html?to=${d.id}`;
    document.getElementById('btn-add-trip-action').href = `trip-planner.html?add=${d.id}`;
    document.getElementById('btn-quick-plan').href = `trip-planner.html?origin=${d.id}`;

    // Overview & Facts
    document.getElementById('about-dest-title').textContent = `About ${d.name}`;
    document.getElementById('dest-full-description').textContent = d.description;
    document.getElementById('fact-budget').textContent = `₹${d.avgDailyBudget}`;
    document.getElementById('fact-season').textContent = d.bestTimeToVisit;
    document.getElementById('fact-coords').textContent = `${d.latitude.toFixed(2)}° N, ${d.longitude.toFixed(2)}° E`;

    // Attractions
    const attrGrid = document.getElementById('attractions-grid');
    attrGrid.innerHTML = d.attractions.map(a => `
        <div class="card">
            <div class="card-image-wrap">
                <img src="${a.image}" alt="${a.name}" loading="lazy" onerror="API.handleImageError(this, '${a.name}')">
                <span class="card-badge ${a.category.toLowerCase()}">${a.category}</span>
            </div>
            <div class="card-content">
                <div class="card-city-state">
                    <h3 class="card-title" style="font-size: 1.25rem;">${a.name}</h3>
                    <span style="color: #ea580c; font-weight: 700;">★ ${a.rating}</span>
                </div>
                <p class="card-desc">${a.description}</p>
                <div class="card-footer" style="font-size: 0.82rem; color: var(--text-muted);">
                    <span>🎟️ ${a.entryFee === 0 ? 'Free Entry' : '₹' + a.entryFee + ' entry'}</span>
                    <span>🕒 ${a.openingHours}</span>
                </div>
            </div>
        </div>
    `).join('');

    // Hotels
    const hotelGrid = document.getElementById('hotels-grid');
    hotelGrid.innerHTML = d.hotels.map(h => `
        <div class="card" style="padding: 1.5rem;">
            <div style="display: flex; justify-content: space-between; align-items: start; margin-bottom: 0.75rem;">
                <div>
                    <span class="card-badge" style="position: static; margin-bottom: 0.4rem; display: inline-block;">${h.tier}</span>
                    <h3 style="font-size: 1.2rem; font-family: var(--font-serif);">${h.name}</h3>
                    <p style="font-size: 0.85rem; color: var(--text-light); margin-top: 0.2rem;">📍 ${h.address}</p>
                </div>
                <div style="text-align: right;">
                    <div style="font-size: 1.25rem; font-weight: 800; color: var(--primary);">₹${h.pricePerNight}</div>
                    <div style="font-size: 0.72rem; color: var(--text-light);">per night</div>
                </div>
            </div>
            <div style="margin-top: 1rem; display: flex; flex-wrap: wrap; gap: 0.4rem;">
                ${h.amenities.map(am => `<span style="background: var(--bg-alt); font-size: 0.75rem; padding: 0.25rem 0.6rem; border-radius: var(--radius-sm); font-weight: 600;">✓ ${am}</span>`).join('')}
            </div>
        </div>
    `).join('');

    // Restaurants
    const restGrid = document.getElementById('restaurants-grid');
    restGrid.innerHTML = d.restaurants.map(r => `
        <div class="card" style="padding: 1.5rem;">
            <div style="display: flex; justify-content: space-between; align-items: start; margin-bottom: 0.75rem;">
                <div>
                    <span class="card-badge" style="position: static; margin-bottom: 0.4rem; display: inline-block; background: #ffedd5; color: #ea580c;">${r.cuisine}</span>
                    <h3 style="font-size: 1.2rem; font-family: var(--font-serif);">${r.name}</h3>
                    <p style="font-size: 0.85rem; color: var(--text-light); margin-top: 0.2rem;">📍 ${r.address}</p>
                </div>
                <div style="text-align: right;">
                    <div style="font-size: 1.15rem; font-weight: 800; color: var(--text-main);">₹${r.avgCostForTwo}</div>
                    <div style="font-size: 0.72rem; color: var(--text-light);">avg for two</div>
                </div>
            </div>
            <div style="margin-top: 0.75rem; background: #fffbf7; padding: 0.75rem; border-radius: var(--radius-sm); border: 1px dashed #fdba74;">
                <span style="font-size: 0.75rem; font-weight: 700; color: var(--primary); text-transform: uppercase;">Signature Dish:</span>
                <p style="font-size: 0.9rem; font-weight: 600; color: var(--text-main); margin-top: 0.2rem;">${r.famousDish}</p>
            </div>
        </div>
    `).join('');

    // Transportation
    const transGrid = document.getElementById('transports-grid');
    transGrid.innerHTML = d.transports.map(t => `
        <div class="card" style="padding: 1.5rem;">
            <div style="display: flex; align-items: center; gap: 0.75rem; margin-bottom: 0.75rem;">
                <div style="width: 40px; height: 40px; border-radius: 50%; background: var(--primary-light); color: var(--primary); display: flex; align-items: center; justify-content: center; font-size: 1.2rem;">
                    ${t.type === 'Flight' ? '✈️' : t.type === 'Train' ? '🚆' : t.type === 'Metro' ? '🚇' : t.type === 'Boat' ? '⛵' : '🚕'}
                </div>
                <div>
                    <h4 style="font-size: 1.05rem;">${t.provider}</h4>
                    <span style="font-size: 0.78rem; color: var(--text-light); font-weight: 600;">${t.type} Service</span>
                </div>
            </div>
            <p style="font-size: 0.88rem; color: var(--text-muted); margin-bottom: 1rem;">${t.description}</p>
            <div style="display: flex; justify-content: space-between; align-items: center; border-top: 1px solid var(--border); padding-top: 0.75rem; font-size: 0.85rem;">
                <span style="color: var(--text-muted);">🕒 ${t.frequency}</span>
                <span style="font-weight: 800; color: var(--primary);">₹${t.avgPrice} avg</span>
            </div>
        </div>
    `).join('');
}

async function loadTextInsights(destId) {
    try {
        const insights = await API.getTextInsights(destId);
        const container = document.getElementById('dest-insights-tags');
        if (container && insights.recurringPhrases) {
            container.innerHTML = insights.recurringPhrases.map(phrase => `
                <span style="background: white; border: 1px solid var(--border); color: var(--text-main); font-size: 0.85rem; font-weight: 600; padding: 0.4rem 0.9rem; border-radius: var(--radius-full); box-shadow: var(--shadow-sm);">
                    ✨ "${phrase}"
                </span>
            `).join('');
        }
    } catch (e) {
        console.error('Insights error:', e);
    }
}

async function loadSimilarDestinations(destId) {
    try {
        const similar = await API.getSimilar(destId);
        const grid = document.getElementById('similar-grid');
        if (grid && similar.length > 0) {
            grid.innerHTML = similar.map(s => `
                <div class="card">
                    <div class="card-image-wrap">
                        <img src="${s.image}" alt="${s.name}" loading="lazy" onerror="API.handleImageError(this, '${s.name}')">
                        <span class="card-badge ${s.category.toLowerCase()}">${s.category}</span>
                    </div>
                    <div class="card-content">
                        <div class="card-city-state">
                            <h3 class="card-title">${s.name}</h3>
                            <span class="card-state">${s.state}</span>
                        </div>
                        <p class="card-desc">${s.reason}</p>
                        <div class="card-footer">
                            <span style="font-size: 0.8rem; font-weight: 700; color: #059669;">${Math.round(s.score * 100)}% Theme Match</span>
                            <a href="destination-details.html?id=${s.id}" class="btn-explore">Explore →</a>
                        </div>
                    </div>
                </div>
            `).join('');
        }
    } catch (e) {
        console.error('Similar error:', e);
    }
}
