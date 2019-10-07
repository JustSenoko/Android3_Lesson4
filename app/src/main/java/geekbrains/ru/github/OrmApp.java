package geekbrains.ru.github;

import android.app.Activity;
import android.app.Application;

import androidx.room.Room;

import java.util.List;

import geekbrains.ru.github.dagger.AppComponent;
import geekbrains.ru.github.dagger.DaggerAppComponent;
import geekbrains.ru.github.dagger.DaggerNetModule;
import geekbrains.ru.github.dagger.NetworkComponent;
import geekbrains.ru.github.dagger.SugarComponent;
import geekbrains.ru.github.databases.room.GithubRoomDatabase;
import geekbrains.ru.github.retrofit.RetrofitModel;

public class OrmApp extends Application {

    private static final String DATABASE_NAME = "DATABASE_USER_GIT";
    public static GithubRoomDatabase database;
    public static OrmApp INSTANCE;
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(), GithubRoomDatabase.class, DATABASE_NAME).build();
        INSTANCE = this;
        component = DaggerAppComponent.builder().daggerNetModule(new DaggerNetModule(getApplicationContext())).build();
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

    public static NetworkComponent getNetworkComponent(Activity activity) {
        return component.getNetworkComponent();
    }

    public static SugarComponent getSugarComponent(List<RetrofitModel> models) {
        return component.sugarComponent();
    }
}