package pl.edu.pb.filmoteka.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, UserRole.class, Role.class, Movie.class, FavouriteMovies.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract UserRoleDao userRoleDao();

    public abstract RoleDao roleDao();

    // Dodaj nowe DAO dla nowych tabel
    public abstract MovieDao movieDao();

    public abstract FavouriteMoviesDao favouriteMoviesDao();

}