/**
 * YATRA ATLAS - EXPLORE & DESTINATIONS LOGIC
 */

document.addEventListener('DOMContentLoaded', async () => {
    let allDestinations = [];

    const grid = document.getElementById('explore-destinations-grid');
    const countHeader = document.getElementById('grid-header-count');
    const filterCategory = document.getElementById('filter-category');
    const filterState = document.getElementById('filter-state');
    const filterBudget = document.getElementById('filter-budget');
    const filterStyle = document.getElementById('filter-style');
    const searchInput = document.getElementById('explore-search-input');
    const searchBtn = document.getElementById('explore-search-btn');
    const resetBtn = document.getElementById('btn-reset-filters');

    // Mobile nav
    const mobileToggle = document.getElementById('mobile-toggle');
    const navMenu = document.getElementById('nav-menu');
    if (mobileToggle && navMenu) {
        mobileToggle.addEventListener('click', () => navMenu.classList.toggle('open'));
    }

    try {
        allDestinations = await API.getDestinations();

        // Populate unique states in dropdown
        const states = [...new Set(allDestinations.map(d => d.state))].sort();
        states.forEach(s => {
            const opt = document.createElement('option');
            opt.value = s;
            opt.textContent = s;
            filterState.appendChild(opt);
        });

        // Check URL query parameters (e.g. ?cat=Heritage)
        const params = new URLSearchParams(window.location.search);
        if (params.has('cat')) {
            filterCategory.value = params.get('cat');
        }

        renderGrid(allDestinations);
    } catch (e) {
        grid.innerHTML = '<p style="text-align: center; color: var(--text-light); grid-column: 1/-1;">Error loading destinations. Ensure backend is running.</p>';
    }

    function renderGrid(list) {
        countHeader.textContent = `Destinations (${list.length})`;

        if (list.length === 0) {
            grid.innerHTML = `
                <div style="grid-column: 1/-1; text-align: center; padding: 4rem 1rem; background: white; border-radius: var(--radius-md);">
                    <h3 style="font-size: 1.5rem; margin-bottom: 0.5rem;">No matching destinations found</h3>
                    <p style="color: var(--text-muted);">Try adjusting your filters or search keywords.</p>
                </div>
            `;
            return;
        }

        grid.innerHTML = list.map(d => `
            <div class="card" id="explore-card-${d.id}">
                <div class="card-image-wrap">
                    <img src="${d.image}" alt="${d.name}" loading="lazy" onerror="API.handleImageError(this, '${d.name}')">
                    <span class="card-badge ${d.category.toLowerCase()}">${d.category}</span>
                </div>
                <div class="card-content">
                    <div class="card-city-state">
                        <h3 class="card-title">${d.name}</h3>
                        <span class="card-state">${d.state}</span>
                    </div>
                    <p class="card-desc">${d.description}</p>
                    <div class="card-footer">
                        <div class="card-budget">
                            <span class="budget-label">Avg Daily Budget</span>
                            <span class="budget-val">₹${d.avgDailyBudget}</span>
                        </div>
                        <a href="destination-details.html?id=${d.id}" class="btn-explore">Explore →</a>
                    </div>
                </div>
            </div>
        `).join('');
    }

    function applyFilters() {
        const cat = filterCategory.value;
        const state = filterState.value;
        const budget = filterBudget.value;
        const query = searchInput.value.trim().toLowerCase();

        let filtered = allDestinations.filter(d => {
            // Category
            if (cat !== 'ALL' && !d.category.equalsIgnoreCase(cat)) return false;
            // State
            if (state !== 'ALL' && !d.state.equalsIgnoreCase(state)) return false;
            // Budget
            if (budget === 'BUDGET' && d.avgDailyBudget >= 3000) return false;
            if (budget === 'MID' && (d.avgDailyBudget < 3000 || d.avgDailyBudget > 4000)) return false;
            if (budget === 'LUXURY' && d.avgDailyBudget <= 4000) return false;
            // Search Text
            if (query.length > 0) {
                const combined = `${d.name} ${d.state} ${d.category} ${d.description}`.toLowerCase();
                if (!combined.includes(query)) return false;
            }
            return true;
        });

        renderGrid(filtered);
    }

    filterCategory.addEventListener('change', applyFilters);
    filterState.addEventListener('change', applyFilters);
    filterBudget.addEventListener('change', applyFilters);
    filterStyle.addEventListener('change', applyFilters);
    searchBtn.addEventListener('click', applyFilters);
    searchInput.addEventListener('keydown', (e) => { if (e.key === 'Enter') applyFilters(); });

    resetBtn.addEventListener('click', () => {
        filterCategory.value = 'ALL';
        filterState.value = 'ALL';
        filterBudget.value = 'ALL';
        filterStyle.value = 'ALL';
        searchInput.value = '';
        renderGrid(allDestinations);
    });
});
