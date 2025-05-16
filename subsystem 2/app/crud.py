import uuid
import requests
import datetime
import os
from typing import List
from sqlalchemy.orm import Session
from fastapi import HTTPException

from . import models, schemas

UPLOAD_DIRECTORY = "uploaded_documents"
os.makedirs(UPLOAD_DIRECTORY, exist_ok=True)

SUBSYSTEM1_UUID_URL = "http://localhost:8080/api/uuid/generate"

def _get_uuid_from_subsystem1() -> uuid.UUID:
    try:
        response = requests.get(SUBSYSTEM1_UUID_URL)
        response.raise_for_status()
        uuid_str = response.text
        return uuid.UUID(uuid_str)
    except requests.exceptions.RequestException as e:
        raise HTTPException(status_code=500, detail=f"Error getting UUID from Subsystem 1: {e}")

def get_all_document_content(db: Session):
    documents = db.query(models.Document.document_id, models.Document.content).all()
    return [{"document_id": str(uuid.UUID(bytes=doc_id)), "content": content} for doc_id, content in documents]

def get_user(user_id: uuid.UUID, db: Session):
    return db.query(models.User).filter(models.User.user_id == user_id.bytes).first()

def get_all_users(db: Session):
    return db.query(models.User).all()

def create_user(user: schemas.UserCreate, db: Session):
    user_id_uuid = _get_uuid_from_subsystem1()
    db_user = models.User(
        user_id=user_id_uuid.bytes,
        username=user.username,
        email=user.email,
        created_at=datetime.datetime.utcnow()
    )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

def get_document(document_id: uuid.UUID, db: Session):
    return db.query(models.Document).filter(models.Document.document_id == document_id.bytes).first()

def create_document(document: schemas.DocumentCreate, db: Session):
    document_id_uuid = _get_uuid_from_subsystem1()
    file_name = os.path.basename(document.file_path)
    stored_file_path = os.path.join(UPLOAD_DIRECTORY, file_name)

    try:
        with open(document.file_path, 'rb') as source_file:
            with open(stored_file_path, 'wb') as destination_file:
                destination_file.write(source_file.read())
    except FileNotFoundError:
        raise HTTPException(status_code=400, detail=f"File not found at path: {document.file_path}")
    except IOError as e:
        raise HTTPException(status_code=500, detail=f"Error reading file: {e}")

    db_document = models.Document(
        document_id=document_id_uuid.bytes,
        title=document.title,
        author_id=document.author_id.bytes,
        user_id=document.user_id.bytes,
        file_path=stored_file_path,
        content=document.content
    )
    db.add(db_document)
    db.commit()
    db.refresh(db_document)
    return db_document

def get_author(author_id: uuid.UUID, db: Session):
    return db.query(models.Author).filter(models.Author.author_id == author_id.bytes).first()

def get_all_authors(db: Session):
    return db.query(models.Author).all()

def create_author(author: schemas.AuthorCreate, db: Session):
    author_id_uuid = _get_uuid_from_subsystem1()
    db_author = models.Author(
        author_id=author_id_uuid.bytes,
        name=author.name,
        affiliation=author.affiliation,
        email=author.email
    )
    db.add(db_author)
    db.commit()
    db.refresh(db_author)
    return db_author