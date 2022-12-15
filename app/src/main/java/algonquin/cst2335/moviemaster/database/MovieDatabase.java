package algonquin.cst2335.moviemaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.moviemaster.model.Movie;
import algonquin.cst2335.moviemaster.database.MovieDAO;

@Database(entities = {Movie.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDAO movieDAO();
}
