<hr style="border: 2px solid black;">

## 🔍 Subsystem 3 - Search and Ranking API (Python) - README.md 🔍

<hr style="border-top: 1px dashed black;">

**📌 Its Role:** Subsystem 3 is the central component responsible for providing fast and relevant search capabilities across all stored documents. It aggregates document data from Subsystems 1 and 2, builds an inverted index to enable efficient searching, and implements a ranking function to order search results based on relevance, utilizing data pulled from Subsystems 1 and 2. This subsystem exposes a RESTful API for other parts of the system (like the Interface) to perform searches.

<hr style="border-top: 1px dashed black;">

**🧰 Tech Used:**

* Python
* RESTful API (e.g., using Flask or FastAPI - the specific framework used in your implementation should be stated here)
* Inverted Indexing (e.g., using `collections.Counter`, `nltk`, or Whoosh - the specific library or method used in your implementation should be stated here)

<hr style="border-top: 1px dashed black;">

**🔄 Endpoints:**

* `/search` (POST): Accepts a JSON payload containing a `query` string and returns a list of ranked document identifiers (or potentially document snippets/metadata) based on the relevance to the query. The exact structure of the request and response should be documented in the API documentation.
* `/admin/reindex` (POST): Triggers the process of fetching document data from Subsystems 1 and 2 and building or updating the inverted index based on this data. This endpoint is also responsible for calculating ranking scores.
* `/admin/status` (GET): Returns the current status of the search index (e.g., "ready", "indexing in progress", "last indexed at [timestamp]", number of indexed documents).

<hr style="border-top: 1px dashed black;">

**🏁 How to Run Locally:**

1.  Ensure you have Python 3.7+ installed.
2.  Navigate to the `subsystem 3` directory.
3.  Create a virtual environment (recommended): `python -m venv venv` and activate it.
4.  Install the necessary dependencies. This will include the RESTful API framework (Flask or FastAPI) and the library used for inverted indexing (if any). A `requirements.txt` file should be included in this directory. For example, if you used FastAPI and Whoosh:
    ```bash
    pip install -r requirements.txt
    ```
5.  Run the Python application that starts your RESTful API. If you used FastAPI, it might look like:
    ```bash
    uvicorn app.main:app --reload
    ```
    (adjust `app.main:app` based on your project structure).
6.  Before performing searches, you will likely need to initialize the inverted index by sending a POST request to the `/admin/reindex` endpoint. This assumes that Subsystems 1 and 2 are running and accessible for data retrieval.

<hr style="border-top: 1px dashed black;">

**🧪 Testing Approach:**

Testing for Subsystem 3 will involve:

* **API Endpoint Testing:**
    * Sending POST requests to the `/search` endpoint with various search queries and verifying that the returned list of document identifiers is relevant and ranked appropriately.
    * Testing the `/admin/reindex` endpoint to ensure it successfully fetches data and builds the inverted index.
    * Checking the `/admin/status` endpoint to verify the status of the index.
* **Black Box Tests:** Evaluating the search functionality from an external perspective by providing queries and assessing the relevance of the results.
* **White Box Tests:** If you have implemented specific components for indexing or ranking, unit tests should be written to ensure their correct operation.

<hr style="border-top: 1px dashed black;">

**🚀 Any special algorithms or implementation details:**

Subsystem 3's core functionality relies on building and utilizing an **inverted index**. This data structure maps terms (words) to the documents in which they appear, enabling efficient retrieval of documents containing specific terms. The specific method or library used for creating and managing this inverted index (e.g., manual implementation using Python dictionaries, or using a library like Whoosh) should be detailed in the System Design Document.

The **ranking function** determines the order of search results. This function is based on data pulled from Subsystem 1 and Subsystem 2. The exact implementation of this ranking mechanism should be described in the System Design Document. Common approaches for ranking in information retrieval include considering factors like term frequency (how often the search terms appear in a document) and potentially other metadata retrieved from Subsystems 1 and 2.

The process of **aggregating document data** from Subsystems 1 and 2 involves making API calls to the respective subsystems to retrieve the necessary information for building the inverted index and calculating ranking scores. The details of these API interactions should be documented in the API Documentation.

<hr style="border: 2px solid black;">