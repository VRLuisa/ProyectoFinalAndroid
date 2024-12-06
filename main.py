from fastapi import FastAPI, HTTPException, Depends
from pydantic import BaseModel
from typing import List
from sqlalchemy import Column, Integer, String, Text, ForeignKey, create_engine
from sqlalchemy.orm import sessionmaker, relationship, declarative_base, Session

# Configuración de la base de datos
DATABASE_URL = "sqlite:///./movies.db"
Base = declarative_base()
engine = create_engine(DATABASE_URL, connect_args={"check_same_thread": False})
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

app = FastAPI()

# Modelos de la base de datos
class MovieDB(Base):
    __tablename__ = "movies"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True, nullable=False)
    description = Column(Text, nullable=False)
    genre = Column(String, index=True, nullable=False)
    duration = Column(Integer, nullable=False)
    reviews = relationship("ReviewDB", back_populates="movie", cascade="all, delete-orphan")


class ReviewDB(Base):
    __tablename__ = "reviews"
    id = Column(Integer, primary_key=True, index=True)
    movie_id = Column(Integer, ForeignKey("movies.id"), nullable=False)
    rating = Column(Integer, nullable=False)
    comment = Column(Text, nullable=False)
    movie = relationship("MovieDB", back_populates="reviews")


# Modelos de Pydantic
class Movie(BaseModel):
    id: int
    name: str
    description: str
    genre: str
    duration: int

    class Config:
        orm_mode = True


class Review(BaseModel):
    rating: int
    comment: str

    class Config:
        orm_mode = True


# Dependencia de sesión de la base de datos
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


# Crear las tablas en la base de datos
Base.metadata.create_all(bind=engine)

# Endpoints
@app.get("/")
def read_root():
    return {"message": "Bienvenido a la API de Películas"}


@app.post("/movies/")
def add_movie(movie: Movie, db: Session = Depends(get_db)):
    existing_movie = db.query(MovieDB).filter(MovieDB.id == movie.id).first()
    if existing_movie:
        raise HTTPException(status_code=400, detail="La película con este ID ya existe.")
    new_movie = MovieDB(**movie.dict())
    db.add(new_movie)
    db.commit()
    return {"message": "Película agregada exitosamente."}


@app.post("/movies/{movie_id}/reviews/")
def add_review(movie_id: int, review: Review, db: Session = Depends(get_db)):
    movie = db.query(MovieDB).filter(MovieDB.id == movie_id).first()
    if not movie:
        raise HTTPException(status_code=404, detail="Película no encontrada.")
    if not (1 <= review.rating <= 5):
        raise HTTPException(status_code=400, detail="La calificación debe estar entre 1 y 5.")
    new_review = ReviewDB(movie_id=movie_id, **review.dict())
    db.add(new_review)
    db.commit()
    return {"message": "Reseña agregada exitosamente."}


@app.get("/movies/genre/{genre}/")
def get_movies_by_genre(genre: str, db: Session = Depends(get_db)):
    movies = db.query(MovieDB).filter(MovieDB.genre.ilike(f"%{genre}%")).all()
    if not movies:
        raise HTTPException(status_code=404, detail="No se encontraron películas para este género.")
    return movies


@app.get("/movies/{movie_id}/")
def get_movie_by_id(movie_id: int, db: Session = Depends(get_db)):
    movie = db.query(MovieDB).filter(MovieDB.id == movie_id).first()
    if not movie:
        raise HTTPException(status_code=404, detail="Película no encontrada.")
    return {"movie": movie, "reviews": movie.reviews}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
