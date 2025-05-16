import faiss
import numpy as np
from typing import List, Dict
import os
from sentence_transformers import SentenceTransformer

INDEX_FILE = "document_index.faiss"
ID_MAP_FILE = "document_id_map.pkl"
INDEX = None
ID_TO_SOURCE_MAP: Dict[int, Dict[str, str]] = {}

def build_faiss_index(documents: List[Dict], sentence_model: SentenceTransformer):
    global INDEX
    global ID_TO_SOURCE_MAP
    embeddings_list = []
    ID_TO_SOURCE_MAP = {}

    for i, doc in enumerate(documents):
        embedding = sentence_model.encode(doc.content)
        embeddings_list.append(embedding)
        ID_TO_SOURCE_MAP[i] = {'id': doc.id, 'source': doc.source}

    if embeddings_list:
        embedding_dimension = embeddings_list[0].shape[0]
        INDEX = faiss.IndexFlatL2(embedding_dimension)
        embeddings_array = np.array(embeddings_list).astype('float32')
        INDEX.add(embeddings_array)
        print(f"FAISS index built with {INDEX.ntotal} documents.")
    else:
        print("No documents to index.")
        INDEX = None

def save_index():
    if INDEX:
        faiss.write_index(INDEX, INDEX_FILE)
        import pickle
        with open(ID_MAP_FILE, 'wb') as f:
            pickle.dump(ID_TO_SOURCE_MAP, f)
        print("FAISS index and ID map saved.")
    else:
        print("No index to save.")

def load_index():
    global ID_TO_SOURCE_MAP
    if os.path.exists(INDEX_FILE) and os.path.exists(ID_MAP_FILE):
        INDEX = faiss.read_index(INDEX_FILE)
        import pickle
        with open(ID_MAP_FILE, 'rb') as f:
            ID_TO_SOURCE_MAP = pickle.load(f)
        print("FAISS index and ID map loaded.")
    else:
        INDEX = None
        ID_TO_SOURCE_MAP = {}
        raise FileNotFoundError("Index files not found.")

def is_index_ready() -> bool:
    return INDEX is not None

def get_indexed_document_count() -> int:
    return INDEX.ntotal if INDEX else 0

def search_index(query_embedding: np.ndarray, top_k: int) -> tuple[np.ndarray, np.ndarray]:
    if INDEX is None:
        print("Warning: FAISS index not loaded. Cannot perform search.")
        return np.array([[]]), np.array([[]])
    distances, indices = INDEX.search(query_embedding, top_k)
    return distances, indices

if __name__ == "__main__":
    test_documents = [
        {'id': '1', 'content': 'The quick brown fox jumps over the lazy dog.', 'source': 's1'},
        {'id': '2', 'content': 'A stitch in time saves nine.', 'source': 's2'},
        {'id': '3', 'content': 'To be or not to be, that is the question.', 'source': 's1'}
    ]
    model = SentenceTransformer('all-MiniLM-L6-v2')
    build_faiss_index(test_documents, model)

    save_index()

    INDEX = None
    ID_TO_SOURCE_MAP = {}
    print("Index cleared.")

    load_index()
    print(f"Loaded index size: {get_indexed_document_count()}")
    print(f"Loaded ID map: {ID_TO_SOURCE_MAP}")

    query_vector = model.encode("What is a common saying?").reshape(1, -1).astype('float32')
    distances, indices = search_index(query_vector, 2)
    print(f"Search distances: {distances}")
    print(f"Search indices: {indices}")
    if indices.size > 0:
        for index in indices[0]:
            if index in ID_TO_SOURCE_MAP:
                print(f"Found document: {ID_TO_SOURCE_MAP[index]}")