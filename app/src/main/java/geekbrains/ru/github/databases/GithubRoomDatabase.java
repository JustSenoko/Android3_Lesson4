package geekbrains.ru.github.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import geekbrains.ru.github.models.room.RoomModel;
import geekbrains.ru.github.models.room.RoomModelDao;

@Database(entities = {RoomModel.class}, version = 1, exportSchema = false)
public abstract class GithubRoomDatabase extends RoomDatabase {
    public abstract RoomModelDao productDao();
}
