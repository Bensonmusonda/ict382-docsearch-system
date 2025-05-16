from pydantic import BaseModel, Field
from typing import List

class SearchQuery(BaseModel):
    query: str = Field(..., example="benefits of vector search")

class SearchResultItem(BaseModel):
    id: str = Field(..., example="a1b2c3d4-e5f6-7890-1234-567890abcdef")
    source: str = Field(..., description="Origin subsystem of the document", example="s1")
    score: float = Field(..., description="Relevance score from search (higher is better)", example=0.95)
    title: str | None = Field(default=None, example="The Future of AI")
    author: str | None = Field(default=None, example="Dr. AI Expert")
    upload_date: str | None = Field(default=None, example="2025-01-15")
    snippet: str | None = Field(default=None, description="A relevant excerpt from the document content", example="AI is rapidly evolving...") # To be generated

    class Config:
        from_attributes = True