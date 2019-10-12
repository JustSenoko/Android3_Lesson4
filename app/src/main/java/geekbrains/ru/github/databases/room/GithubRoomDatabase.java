package geekbrains.ru.github.databases.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RoomModel.class}, version = 1, exportSchema = false)
public abstract class GithubRoomDatabase extends RoomDatabase {
    public abstract RoomModelDao productDao();
}
