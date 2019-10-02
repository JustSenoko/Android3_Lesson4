package geekbrains.ru.github;

import android.app.Application;

import androidx.room.Room;

import geekbrains.ru.github.databases.GithubRoomDatabase;

public class OrmApp extends Application {

    private static final String DATABASE_NAME = "DATABASE_USER_GIT";
    public static GithubRoomDatabase database;
    public static OrmApp INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(), GithubRoomDatabase.class, DATABASE_NAME).build();
        INSTANCE = this;
    }

    public GithubRoomDatabase getDB() {
        return database;
    }

    public static OrmApp get() {
        return INSTANCE;
    }
}