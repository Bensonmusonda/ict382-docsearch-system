<hr style="border: 2px solid black;">

## 🌐 Interface - README.md 🌐

<hr style="border-top: 1px dashed black;">

**📌 Overview:** The Interface provides the user-facing part of the DocSearch System. It is a web-based application built using standard web technologies, allowing users to interact with the backend subsystems to upload documents and perform searches.

<hr style="border-top: 1px dashed black;">

**🧰 Tech Used:**

* HTML (for structuring the web page)
* CSS (for styling and visual presentation)
* JavaScript (for client-side logic and communication with the backend APIs)

<hr style="border-top: 1px dashed black;">

**🎯 Functionality:**

The Interface is designed to provide the following core functionalities:

* **Document Upload:** Allows users to select and upload documents (text, PDF, etc.). Upon submission, the Interface communicates with either Subsystem 1 or Subsystem 2 (depending on the intended storage location as configured in the Interface's logic) to handle the file upload and metadata storage.
* **Search Query Input:** Provides a user-friendly input field where users can type their search queries.
* **Search Results Display:** Presents the search results returned by Subsystem 3 in a clear and organized manner. This will likely include the title of the document and potentially a snippet or other relevant metadata.

<hr style="border-top: 1px dashed black;">

**🔌 API Connections:**

The Interface communicates with the backend subsystems via their RESTful APIs:

* **Document Upload:**
    * Likely connects to the document upload endpoint of either Subsystem 1 (Java/MySQL) or Subsystem 2 (Python/MySQL). The specific endpoint (e.g., `/api/documents/`) and the expected request format (e.g., multipart/form-data) will be detailed in the API documentation for the chosen subsystem.
* **Search Queries:**
    * Interacts with the `/search` (POST) endpoint of Subsystem 3 (Python). The Interface will send the user's search query in the expected JSON format. The response will be a list of document identifiers and potentially ranking scores or metadata.

<hr style="border-top: 1px dashed black;">

**🏁 How to Run Locally:**

As the Interface is primarily client-side, you can typically run it by simply opening the `index.html` file in your web browser. However, for development or if you have client-side routing or more complex JavaScript interactions, you might need to serve the files using a local web server. Common options include:

* **Python's SimpleHTTPServer:** Navigate to the `interface` directory in your terminal and run `python -m http.server 8000` (or a different port). Then, access the interface by navigating to `http://localhost:8000` in your browser. For Python 3, use `python3 -m http.server 8000`.
* **Node.js `http-server`:** If you have Node.js and npm installed, you can install `http-server` globally (`npm install -g http-server`) and then run `http-server` from the `interface` directory.

Ensure that the backend subsystems (Subsystem 1, Subsystem 2, and Subsystem 3) are running and accessible at their respective addresses for the Interface to communicate with them. You might need to configure the API endpoint URLs in your JavaScript code to match your local setup.

<hr style="border-top: 1px dashed black;">

<hr style="border-top: 1px dashed black;">

**💅 UI Libraries/Custom Fonts:**


<hr style="border-top: 1px dashed black;">

**🖼️ Screenshots/Wireframes:**

<hr style="border-top: 1px dashed black;">

**⏭️ Limitations/Future Improvements:**



