import uuid, requests
from sqlalchemy import Column, String, ForeignKey, DateTime, LargeBinary 
from sqlalchemy.orm import relationship, Mapped, mapped_column
from datetime import datetime
from .database import Base


class User(Base):
    __tablename__ = "users"
    user_id: Mapped[bytes] = mapped_column(LargeBinary(16), primary_key=True, default=uuid.uuid4().bytes, index=True) 
    username: Mapped[str] = mapped_column(String(100), unique=True, nullable=False)
    email: Mapped[str] = mapped_column(String(255), unique=True, nullable=False)
    created_at: Mapped[datetime] = mapped_column(DateTime, default=datetime.utcnow)

    documents = relationship("Document", back_populates="user")

    def __repr__(self):
        return f"<User(user_id={str(uuid.UUID(bytes=self.user_id))}, username='{self.username}', email='{self.email}')>"

class Author(Base):
    __tablename__ = "authors"
    author_id: Mapped[bytes] = mapped_column(LargeBinary(16), primary_key=True, default=uuid.uuid4().bytes, index=True) 
    name: Mapped[str] = mapped_column(String(100), unique=True, nullable=False)
    affiliation: Mapped[str] = mapped_column(String(255))
    email: Mapped[str] = mapped_column(String(255), unique=True)

    documents = relationship("Document", back_populates="author")

    def __repr__(self):
        return f"<Author(author_id={str(uuid.UUID(bytes=self.author_id))}, name='{self.name}')>"

class Document(Base):
    __tablename__ = "documents"
    document_id: Mapped[bytes] = mapped_column(LargeBinary(16), primary_key=True, default=uuid.uuid4().bytes, index=True)
    title: Mapped[str] = mapped_column(String(255), nullable=False)
    author_id: Mapped[bytes] = mapped_column(LargeBinary(16), ForeignKey("authors.author_id"))
    user_id: Mapped[bytes] = mapped_column(LargeBinary(16), ForeignKey("users.user_id"))
    upload_date: Mapped[datetime] = mapped_column(DateTime, default=datetime.utcnow)
    file_path: Mapped[str] = mapped_column(String(255))
    content: Mapped[str] = mapped_column(String)

    author = relationship("Author", back_populates="documents")
    user = relationship("User", back_populates="documents")

    def __repr__(self):
        return f"<Document(document_id={str(uuid.UUID(bytes=self.document_id))}, title='{self.title}')>"