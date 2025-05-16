from pydantic import BaseModel

class AggregatedDocument(BaseModel):
    id: str
    content: str
    source: str  # 's1' or 's2'