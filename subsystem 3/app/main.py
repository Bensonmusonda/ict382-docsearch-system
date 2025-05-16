from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from typing import List

from . import crud
from .schemas import SearchQuery, SearchResultItem
from . import index_manager
from . import vectorizer

app = FastAPI(
    title="Subsystem 3 - Search and Ranking API",
    description="Aggregates documents and provides search functionality using vector embeddings.",
    version="0.1.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.on_event("startup")
async def startup_event():

    print("Application startup: Loading FAISS index and document map...")
    try:
        index_manager.load_index() 

        print("Successfully loaded FAISS index and document map.")
    except FileNotFoundError:
        print("Index files not found. Please run /admin/reindex to build them.")
    except Exception as e:
        print(f"Error loading index files: {e}")
        


@app.post("/search", response_model=List[SearchResultItem])
async def search_documents_api(search_query: SearchQuery):

    if not index_manager.is_index_ready(): 
        raise HTTPException(status_code=503, detail="Index is not ready or not loaded. Please try again later or trigger reindexing.")
    try:
        results = await crud.search_documents_and_fetch_details(search_query.query)
        return results
    except Exception as e:

        raise HTTPException(status_code=500, detail=f"An error occurred during search: {str(e)}")

@app.post("/admin/reindex", status_code=200)
async def reindex_data_api():
    
    print("Reindexing process started...")
    try:

        print("Step 1/3: Aggregating documents from S1 and S2...")
        aggregated_documents = crud.aggregate_documents() # This is synchronous
        if not aggregated_documents:
            print("No documents aggregated. Indexing aborted.")
            return {"message": "Reindexing attempted: No documents found to aggregate."}
        print(f"Aggregated {len(aggregated_documents)} documents.")

        print("Step 2/3: Building FAISS index...")
        index_manager.build_faiss_index(documents=aggregated_documents, sentence_model=vectorizer.model)
        print("FAISS index built successfully.")


        print("Step 3/3: Saving index and document map to disk...")
        index_manager.save_index()
        print("Index and document map saved successfully.")

        return {"message": f"Reindexing completed successfully. Indexed {len(aggregated_documents)} documents."}
    except Exception as e:

        print(f"Error during reindexing: {e}")
        raise HTTPException(status_code=500, detail=f"An error occurred during reindexing: {str(e)}")


@app.get("/admin/status")
async def get_status():

    return {
        "index_ready": index_manager.is_index_ready(),
        "indexed_documents_count": index_manager.get_indexed_document_count()
    }