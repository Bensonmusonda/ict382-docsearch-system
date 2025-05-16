import requests
from typing import List
from .models import AggregatedDocument
from .schemas import SearchResultItem
from . import vectorizer
from . import index_manager

SUBsystem1_API_URL = "http://localhost:8080/api"
SUBsystem2_API_URL = "http://localhost:8000/api"

def fetch_all_content_s1() -> List[dict]:
    try:
        response = requests.get(f"{SUBsystem1_API_URL}/documents/allcontent")
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        print(f"Error fetching from Subsystem 1: {e}")
        return []

def fetch_all_content_s2() -> List[dict]:
    try:
        response = requests.get(f"{SUBsystem2_API_URL}/documents/allcontent")
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        print(f"Error fetching from Subsystem 2: {e}")
        return []

def aggregate_documents() -> List[AggregatedDocument]:
    s1_data = fetch_all_content_s1()
    s2_data = fetch_all_content_s2()

    aggregated: List[AggregatedDocument] = []

    for item in s1_data:
        if 'documentId' in item and 'content' in item:
            aggregated.append(
                AggregatedDocument(id=item['documentId'], content=item['content'], source='s1')
            )
        else:
            print(f"Warning: Skipping invalid item from S1: {item}")

    for item in s2_data:
        if 'document_id' in item and 'content' in item:
            aggregated.append(
                AggregatedDocument(id=item['document_id'], content=item['content'], source='s2')
            )
        else:
            print(f"Warning: Skipping invalid item from S2: {item}")

    return aggregated

async def search_documents_and_fetch_details(query: str, top_k: int = 10) -> List[SearchResultItem]:
 
    if not index_manager.is_index_ready():
        print("Warning: FAISS index is not ready.")
        return []

    query_embedding = vectorizer.generate_embedding(query)
    distances, indices = index_manager.search_index(query_embedding.reshape(1, -1), top_k)

    results: List[SearchResultItem] = []
    for i, index in enumerate(indices[0]):
        if index in index_manager.ID_TO_SOURCE_MAP:
            doc_info = index_manager.ID_TO_SOURCE_MAP[index]
            doc_id = doc_info['id']
            source = doc_info['source']
            score = 1 - distances[0][i] / 2.0

            metadata = fetch_document_details(doc_id, source)
            snippet = metadata.get('full_content', '')[:200] + "..." if metadata.get('full_content') else None # Basic snippet

            results.append(SearchResultItem(
                id=doc_id,
                source=source,
                score=score,
                title=metadata.get('title'),
                author=metadata.get('author'),
                upload_date=metadata.get('upload_date'),
                snippet=snippet
            ))
        else:
            print(f"Warning: Index {index} not found in ID map.")

    return results

def fetch_document_details(doc_uuid: str, source: str) -> dict:
    metadata = {"id": doc_uuid, "source": source} 
    url = ""
    try:
        if source == 's1':
            url = f"{SUBsystem1_API_URL}/documents/{doc_uuid}"
            response = requests.get(url)
            response.raise_for_status()
            s1_details = response.json()
            metadata['title'] = s1_details.get('title')
            metadata['author'] = s1_details.get('author', {}).get('name')
            metadata['upload_date'] = s1_details.get('uploadDate')
            metadata['full_content'] = s1_details.get('content')
        elif source == 's2':
            url = f"{SUBsystem2_API_URL}/documents/{doc_uuid}"
            response = requests.get(url)
            response.raise_for_status()
            s2_details = response.json()
            metadata['title'] = s2_details.get('title')
            author_detail = s2_details.get('author')
            if isinstance(author_detail, dict):
                metadata['author'] = author_detail.get('name')
            elif isinstance(author_detail, str):
                metadata['author'] = author_detail
            else:
                metadata['author'] = s2_details.get('author_name')
            metadata['upload_date'] = s2_details.get('upload_date')
            metadata['full_content'] = s2_details.get('content')
        else:
            print(f"Unknown source: {source} for document {doc_uuid}")
            return metadata

    except requests.exceptions.RequestException as e:
        print(f"Error fetching details for {doc_uuid} from {source} ({url}): {e}")
        metadata['error'] = str(e)
    except Exception as e:
        print(f"An unexpected error occurred when fetching details for {doc_uuid} from {source} ({url}): {e}")
        metadata['error'] = str(e)

    return metadata