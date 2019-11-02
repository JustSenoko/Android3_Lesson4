package geekbrains.ru.github;

import android.app.Application;

import androidx.room.Room;

import java.util.List;

import geekbrains.ru.github.dagger.AppComponent;
import geekbrains.ru.github.dagger.DBTestComponent;
import geekbrains.ru.github.dagger.DBTestModule;
import geekbrains.ru.github.dagger.DaggerAppComponent;
import geekbrains.ru.github.dagger.DaggerNetModule;
import geekbrains.ru.github.dagger.DaggerPresenterComponent;
import geekbrains.ru.github.dagger.NetworkComponent;
import geekbrains.ru.github.dagger.PresenterComponent;
import geekbrains.ru.github.dagger.RoomComponent;
import geekbrains.ru.github.dagger.SugarComponent;
import geekbrains.ru.github.databases.room.GithubRoomDatabase;
import geekbrains.ru.github.retrofit.RetrofitModel;

public class OrmApp extends Application {

    private static final String DATABASE_NAME = "DATABASE_USER_GIT";
    private static GithubRoomDatabase database;
    private static OrmApp INSTANCE;
    private static AppComponent component;
    private static PresenterComponent presenterComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(), GithubRoomDatabase.class, DATABASE_NAME).build();
        INSTANCE = this;
        component = DaggerAppComponent.builder().daggerNetModule(new DaggerNetModule(getApplicationContext())).build();
        presenterComponent = DaggerPresenterComponent.create();
    }

    public GithubRoomDatabase getDB() {
        return database;
    }

    public static OrmApp get() {
        return INSTANCE;
    }

    public static AppComponent getComponent() {
        return component;
    }

    public static NetworkComponent getNetworkComponent() {
        return component.getNetworkComponent();
    }

    public static SugarComponent getSugarComponent() {
        return component.getSugarComponent();
    }

    public static RoomComponent getRoomComponent() {
        return component.getRoomComponent();
    }

    public static PresenterComponent getPresenterComponent() {
        return presenterComponent;
    }

    public static DBTestComponent getDbTestComponent(DBTestModule module) {
        return component.getDBTestComponent(module);
    }
}