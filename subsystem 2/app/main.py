import uuid
from typing import Union, List
from fastapi import FastAPI, Depends, HTTPException, Form, File, UploadFile
from sqlalchemy.orm import Session
from PyPDF2 import PdfReader
from docx import Document as DocxDocument
import io
import os

from . import crud, schemas, database

def get_db():
    db = database.SessionLocal()
    try:
        yield db
    finally:
        db.close()

app = FastAPI()

async def extract_text_from_upload_file(file: UploadFile) -> str:
    text = ""
    try:
        if file.filename.lower().endswith(".pdf"):
            pdf_content = await file.read()
            pdf_reader = PdfReader(io.BytesIO(pdf_content))
            for page in pdf_reader.pages:
                text += page.extract_text() or ""
        elif file.filename.lower().endswith(".docx"):
            docx_content = await file.read()
            document = DocxDocument(io.BytesIO(docx_content))
            for paragraph in document.paragraphs:
                text += paragraph.text + "\n"
        elif file.filename.lower().endswith(".txt"):
            text = (await file.read()).decode("utf-8")
        await file.seek(0)  # Reset file pointer
    except Exception as e:
        print(f"Error extracting text from {file.filename}: {e}")
    return text

# user endpoints
@app.get("/api/documents/allcontent")
def get_all_document_content_endpoint(db: Session = Depends(get_db)):
    """
    Returns a list of all document IDs and their content.
    """
    return crud.get_all_document_content(db)

@app.get("/api/users/{user_id}", response_model=schemas.UserOut)
def get_user(user_id: uuid.UUID, db: Session = Depends(get_db)):
    return crud.get_user(user_id, db)

@app.get("/api/users/", response_model=List[schemas.UserOut])
def get_all_users(db: Session = Depends(get_db)):
    """Returns a list of all users."""
    return crud.get_all_users(db)

@app.post("/api/users/", response_model=schemas.UserOut)
def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    return crud.create_user(user, db)

# document endpoints
@app.get("/api/documents/{document_id}", response_model=schemas.DocumentOut)
def get_document(document_id: uuid.UUID, db: Session = Depends(get_db)):
    return crud.get_document(document_id, db)

@app.post("/api/documents/", response_model=schemas.DocumentOut)
async def create_document(
    title: str = Form(...),
    author_id: uuid.UUID = Form(...),
    user_id: uuid.UUID = Form(...),
    file: UploadFile = File(...),
    db: Session = Depends(get_db)
):
    extracted_content = await extract_text_from_upload_file(file)

    upload_dir = crud.UPLOAD_DIRECTORY
    file_name = os.path.join(upload_dir, file.filename)
    try:
        with open(file_name, "wb") as buffer:
            while chunk := await file.read(1024):
                buffer.write(chunk)
        await file.seek(0) # Reset file pointer after saving
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error saving uploaded file: {e}")

    document_create = schemas.DocumentCreate(
        title=title,
        author_id=author_id,
        user_id=user_id,
        file_path=file_name,
        content=extracted_content
    )
    return crud.create_document(document=document_create, db=db)

# author endpoints
@app.get("/api/authors/{author_id}", response_model=schemas.AuthorOut)
def get_author(author_id: uuid.UUID, db: Session = Depends(get_db)):
    return crud.get_author(author_id, db)

@app.get("/api/authors/", response_model=List[schemas.AuthorOut])
def get_all_authors(db: Session = Depends(get_db)):
    """Returns a list of all authors."""
    return crud.get_all_authors(db)

@app.post("/api/authors/", response_model=schemas.AuthorOut)
def create_author(author: schemas.AuthorCreate, db: Session = Depends(get_db)):
    return crud.create_author(author, db)