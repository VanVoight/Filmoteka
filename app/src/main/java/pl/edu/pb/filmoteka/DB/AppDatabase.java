package pl.edu.pb.filmoteka.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, UserRole.class, Role.class, Film.class, FavouriteFilms.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract UserRoleDao userRoleDao();

    public abstract RoleDao roleDao();

    public abstract FilmDao filmDao();
    public abstract FavouriteFilmsDao favouriteFilmsDao();
}