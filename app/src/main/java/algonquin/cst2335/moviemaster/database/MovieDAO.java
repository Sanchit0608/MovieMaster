package algonquin.cst2335.moviemaster.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import algonquin.cst2335.moviemaster.model.Movie;

@Dao
public interface MovieDAO {

    @Insert
    public void insertMovie(Movie movie);

    @Query("select * from Movie")
    public List<Movie> getAllMovies();

    @Delete
    public void deleteMovie(Movie movie);
}