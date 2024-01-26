package pl.edu.pb.filmoteka.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, UserRole.class, Role.class, Movie.class, FavouriteMovies.class, WatchedMovies.class, MyListMovies.class, Review.class, Rating.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract UserRoleDao userRoleDao();

    public abstract RoleDao roleDao();

    // Dodaj nowe DAO dla nowych tabel
    public abstract MovieDao movieDao();

    public abstract FavouriteMoviesDao favouriteMoviesDao();
    public abstract WatchedMoviesDao watchedMoviesDao();
    public abstract MyListMoviesDao myListMoviesDao();
    public abstract ReviewDao reviewDao();
    public abstract RatingDao ratingDao();
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "my-database")
                    .build();
        }
        return instance;
    }

}