package pl.edu.pb.filmoteka.DB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_films",
        primaryKeys = {"userId", "filmDbId"},
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Film.class,
                        parentColumns = "filmDbId",  // Corrected column name here
                        childColumns = "filmDbId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("userId"),
                @Index("filmDbId")
        }
)
public class FavouriteFilms {
    public long userId;
    @NonNull
    public long filmDbId;

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setFilmDbId(long filmDbId) {
        this.filmDbId = filmDbId;
    }
}