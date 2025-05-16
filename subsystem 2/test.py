import os
import pytest
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, Session
from sqlalchemy.exc import IntegrityError
from app.models import Base, User, Author, Document
from faker import Faker

# Load test database URL from environment
TEST_DATABASE_URL = os.getenv('TEST_DATABASE_URL', 'mysql+pymysql://root:nosneb1010?@localhost/test_subsystem2')
engine = create_engine(TEST_DATABASE_URL, echo=False)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
fake = Faker()

@pytest.fixture(scope="session")
def db_engine():
    Base.metadata.create_all(bind=engine)
    yield engine
    Base.metadata.drop_all(bind=engine)

@pytest.fixture(scope="function")
def db_session(db_engine):
    connection = db_engine.connect()
    transaction = connection.begin()
    session = Session(bind=connection)
    yield session
    session.close()
    transaction.rollback()
    connection.close()

def test_create_user(db_session):
    user = User(username="testuser", email="testuser@example.com")
    db_session.add(user)
    db_session.commit()
    db_session.refresh(user)
    assert user.user_id is not None
    assert user.username == "testuser"

def test_create_author(db_session):
    author = Author(name="John Doe", affiliation="Test Institute", email="johndoe@example.com")
    db_session.add(author)
    db_session.commit()
    db_session.refresh(author)
    assert author.author_id is not None
    assert author.name == "John Doe"

def test_create_document_with_content(db_session):
    user = User(username="testuser2", email="testuser2@example.com")
    author = Author(name="Jane Doe", affiliation="Test Lab", email="janedoe@example.com")
    db_session.add_all([user, author])
    db_session.commit()
    db_session.refresh(user)
    db_session.refresh(author)

    document = Document(
        title="Test Document",
        content="Sample content in DB.",
        user_id=user.user_id,
        author_id=author.author_id
    )
    db_session.add(document)
    db_session.commit()
    db_session.refresh(document)

    assert document.document_id is not None
    assert document.content.startswith("Sample")
    assert document.user.user_id == user.user_id
    assert document.author.author_id == author.author_id

def test_query_documents_by_user(db_session):
    user = User(username="queryuser", email="queryuser@example.com")
    author = Author(name="Query Author", affiliation="Query Inst", email="queryauthor@example.com")
    db_session.add_all([user, author])
    db_session.commit()

    doc1 = Document(title="Doc 1", content="Content 1", user_id=user.user_id, author_id=author.author_id)
    doc2 = Document(title="Doc 2", content="Content 2", user_id=user.user_id, author_id=author.author_id)
    db_session.add_all([doc1, doc2])
    db_session.commit()

    user_docs = db_session.query(Document).filter(Document.user_id == user.user_id).all()
    assert len(user_docs) == 2

def test_delete_user_cascades_documents(db_session):
    user = User(username="deleteuser", email="deleteuser@example.com")
    author = Author(name="Delete Author", affiliation="Del Inst", email="deleteauthor@example.com")
    db_session.add_all([user, author])
    db_session.commit()

    doc = Document(title="Doc to Delete", content="Orphaned content", user_id=user.user_id, author_id=author.author_id)
    db_session.add(doc)
    db_session.commit()

    db_session.delete(user)
    db_session.commit()

    remaining_docs = db_session.query(Document).filter(Document.title == "Doc to Delete").all()
    assert len(remaining_docs) == 0  # Ensure ON DELETE CASCADE is working

def test_unique_username_constraint(db_session):
    user1 = User(username="uniqueuser", email=fake.email())
    user2 = User(username="uniqueuser", email=fake.email())
    db_session.add(user1)
    db_session.commit()
    with pytest.raises(IntegrityError):
        db_session.add(user2)
        db_session.commit()

def test_document_with_nonexistent_author(db_session):
    user = User(username="ghostuser", email=fake.email())
    db_session.add(user)
    db_session.commit()

    fake_author_id = 99999  # Assuming this ID does not exist
    document = Document(title="Ghost Doc", content="No author here", user_id=user.user_id, author_id=fake_author_id)
    db_session.add(document)
    with pytest.raises(IntegrityError):
        db_session.commit()

def test_delete_author_does_not_cascade_documents(db_session):
    user = User(username="noauthorcascade", email=fake.email())
    author = Author(name="Persist Author", affiliation="Stay Inst", email=fake.email())
    db_session.add_all([user, author])
    db_session.commit()

    document = Document(title="Author Bound Doc", content="Stuck to author", user_id=user.user_id, author_id=author.author_id)
    db_session.add(document)
    db_session.commit()

    db_session.delete(author)
    db_session.commit()

    remaining_docs = db_session.query(Document).filter(Document.title == "Author Bound Doc").all()
    assert len(remaining_docs) == 1  # Documents should remain if no cascade is set
