document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('search-box');
    const submitBtn = document.getElementById('submit');
    const searchResults = document.getElementById('search-results');
    const form = document.querySelector('form');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const searchQuery = searchInput.value;
        const url = `http://localhost:8001/search/`;

        if (searchQuery.trim() !== "") {
            showLoading();

            try {
                const response = await fetch(url, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ query: searchQuery })
                });

                if (!response.ok) {
                    const errorMessage = `HTTP error! status: ${response.status}`;
                    throw new Error(errorMessage);
                }

                const data = await response.json();
                displayResults(data);
            }
            catch (error) {
                console.error('Error during search:', error);
                searchResults.innerHTML = `<p class="error">An error occurred during the search.</p>`;
            }
            finally {
                hideLoading();
            }
        } else {
            searchResults.innerHTML = `<p class="warning">Please enter a search query.</p>`;
        }
    });

    function displayResults(results) {
        searchResults.innerHTML = ''; // Clear previous results
        if (results && results.length > 0) {
            results.forEach(result => {
                const resultDiv = document.createElement('div');
                resultDiv.classList.add('search-result'); // Add a class for styling (ensure you have CSS for this)

                const title = result.title ? `<h3 class="title">${result.title}</h3>` : '<h3 class="title">No Title</h3>';
                const source = `<p class="source">Source: ${result.source}</p>`;
                const snippet = `<p class="snippet">${result.snippet}...</p>`;
                const score = `<p class="score">Score: ${result.score ? result.score.toFixed(2) : 'N/A'}</p>`;

                resultDiv.innerHTML = `${title}${source}${snippet}${score}`;
                searchResults.appendChild(resultDiv);
            });
        } else {
            searchResults.innerHTML = `<p>No results found.</p>`;
        }
    }

    function showLoading() {

        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });

        const loadingBar = document.querySelector('.loading-bar');
        loadingBar.style.display = 'block';
    }

    function hideLoading() {
        const loadingBar = document.querySelector('.loading-bar');
        loadingBar.style.display = 'none';
    }
});