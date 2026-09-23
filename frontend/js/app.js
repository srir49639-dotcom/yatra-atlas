/**
 * YATRA ATLAS - HOME PAGE APPLICATION LOGIC
 */

document.addEventListener('DOMContentLoaded', async () => {
    // Mobile navigation toggle
    const mobileToggle = document.getElementById('mobile-toggle');
    const navMenu = document.getElementById('nav-menu');
    if (mobileToggle && navMenu) {
        mobileToggle.addEventListener('click', () => {
            navMenu.classList.toggle('open');
        });
    }

    // Load Popular Destinations
    await loadPopularDestinations();

    // Setup Search & Typo handling
    setupSearch();

    // Setup Surprise Me
    setupSurpriseMe();
});

async function loadPopularDestinations() {
    const grid = document.getElementById('popular-destinations-grid');
    if (!grid) return;

    try {
        const destinations = await API.getDestinations();
        // Featured selection: Hyderabad, Goa, Delhi, Jaipur, Varanasi, Manali, Bengaluru, Kochi
        const featuredIds = ['hyderabad', 'goa', 'delhi', 'jaipur', 'varanasi', 'manali', 'bengaluru', 'kochi'];
        const featured = destinations.filter(d => featuredIds.includes(d.id.toLowerCase()));

        grid.innerHTML = (featured.length ? featured : destinations.slice(0, 8)).map(d => `
            <div class="card" id="card-${d.id}">
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
                        <a href="pages/destination-details.html?id=${d.id}" class="btn-explore" id="explore-${d.id}">Explore →</a>
                    </div>
                </div>
            </div>
        `).join('');
    } catch (err) {
        grid.innerHTML = '<p style="grid-column: 1/-1; text-align: center; color: var(--text-light);">Loading popular destinations...</p>';
    }
}

function setupSearch() {
    const searchInput = document.getElementById('main-search-input');
    const searchBtn = document.getElementById('main-search-btn');
    const didYouMeanBox = document.getElementById('did-you-mean-box');
    const didYouMeanLink = document.getElementById('did-you-mean-link');
    const resultsSection = document.getElementById('search-results-section');
    const resultsGrid = document.getElementById('search-results-grid');
    const resultsTitle = document.getElementById('search-results-title');
    const resultsCount = document.getElementById('search-results-count');

    if (!searchInput || !searchBtn) return;

    let debounceTimer;

    const performSearch = async (query) => {
        if (!query || query.trim().length === 0) {
            if (resultsSection) resultsSection.style.display = 'none';
            if (didYouMeanBox) didYouMeanBox.style.display = 'none';
            return;
        }

        try {
            const resp = await API.search(query.trim());

            // Handle Typo / "Did you mean"
            if (resp.didYouMean) {
                didYouMeanBox.style.display = 'block';
                didYouMeanLink.textContent = resp.didYouMean;
                didYouMeanLink.onclick = () => {
                    searchInput.value = resp.didYouMean;
                    performSearch(resp.didYouMean);
                };
            } else {
                didYouMeanBox.style.display = 'none';
            }

            // Display Results
            if (resultsSection && resultsGrid) {
                resultsSection.style.display = 'block';
                resultsTitle.textContent = `Places Matching "${query}"`;
                resultsCount.textContent = `Found ${resp.results.length} result(s)`;

                if (resp.results.length === 0) {
                    resultsGrid.innerHTML = `
                        <div style="grid-column: 1/-1; text-align: center; padding: 3rem; background: white; border-radius: var(--radius-md);">
                            <p style="font-size: 1.15rem; color: var(--text-main); font-weight: 600;">No destinations found matching "${query}"</p>
                            <p style="color: var(--text-muted); margin-top: 0.5rem;">Try searching for "Fort", "Beach", "Biryani", "Temple", or "Himalayas".</p>
                        </div>
                    `;
                } else {
                    resultsGrid.innerHTML = resp.results.map(r => `
                        <div class="card">
                            <div class="card-image-wrap">
                                <img src="${r.image}" alt="${r.title}" loading="lazy" onerror="API.handleImageError(this, '${r.destinationName}')">
                                <span class="card-badge">${r.itemType}</span>
                            </div>
                            <div class="card-content">
                                <div class="card-city-state">
                                    <h3 class="card-title">${r.title}</h3>
                                    <span class="card-state">${r.destinationName}</span>
                                </div>
                                <p class="card-desc">${r.snippet || r.subtitle}</p>
                                <div class="card-footer">
                                    <span class="badge" style="font-size: 0.75rem; color: var(--primary); font-weight: 700;">${r.subtitle}</span>
                                    <a href="pages/destination-details.html?id=${r.destinationId}" class="btn-explore">View Details →</a>
                                </div>
                            </div>
                        </div>
                    `).join('');
                }

                resultsSection.scrollIntoView({ behavior: 'smooth' });
            }
        } catch (e) {
            console.error('Search error:', e);
        }
    };

    searchBtn.addEventListener('click', () => performSearch(searchInput.value));
    searchInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') performSearch(searchInput.value);
    });

    searchInput.addEventListener('input', () => {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => {
            if (searchInput.value.trim().length >= 3) {
                performSearch(searchInput.value);
            }
        }, 350);
    });
}

function setupSurpriseMe() {
    const surpriseBtn = document.getElementById('btn-surprise-me');
    const surpriseCard = document.getElementById('surprise-card');
    const surpriseImg = document.getElementById('surprise-img');
    const surpriseCat = document.getElementById('surprise-category');
    const surpriseTitle = document.getElementById('surprise-title');
    const surpriseDesc = document.getElementById('surprise-desc');
    const surpriseLink = document.getElementById('surprise-link');

    if (!surpriseBtn || !surpriseCard) return;

    surpriseBtn.addEventListener('click', async () => {
        surpriseBtn.disabled = true;
        surpriseBtn.innerHTML = '<span>⏳</span><span>Discovering...</span>';

        try {
            const data = await API.discoverSurprise();
            surpriseImg.src = data.image;
            surpriseImg.onerror = () => API.handleImageError(surpriseImg, data.name);
            surpriseCat.textContent = data.category;
            surpriseTitle.textContent = `${data.name}, ${data.state}`;
            surpriseDesc.textContent = data.description;
            surpriseLink.href = `pages/destination-details.html?id=${data.id}`;

            surpriseCard.style.display = 'block';
            surpriseCard.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
        } catch (e) {
            console.error('Surprise error:', e);
        } finally {
            surpriseBtn.disabled = false;
            surpriseBtn.innerHTML = '<span>✨</span><span>Surprise Me Again</span>';
        }
    });
}
