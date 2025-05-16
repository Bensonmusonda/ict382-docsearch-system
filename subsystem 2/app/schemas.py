import uuid
from datetime import datetime
from pydantic import BaseModel, Field

class UserCreate(BaseModel):
    username: str
    email: str

class UserOut(BaseModel):
    user_id: uuid.UUID
    username: str
    email: str
    created_at: datetime

    class Config:
        from_attributes = True

class DocumentCreate(BaseModel):
    title: str
    author_id: uuid.UUID
    user_id: uuid.UUID
    file_path: str = Field(..., description="Path to the document file to be uploaded")
    content: str

class DocumentOut(BaseModel):
    document_id: uuid.UUID
    title: str
    author_id: uuid.UUID
    user_id: uuid.UUID
    upload_date: datetime
    file_path: str
    content: str

    class Config:
        from_attributes = True

class AuthorCreate(BaseModel):
    name: str
    affiliation: str
    email: str

class AuthorOut(BaseModel):
    author_id: uuid.UUID
    name: str
    affiliation: str
    email: str

    class Config:
        from_attributes = True