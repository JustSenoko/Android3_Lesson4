package geekbrains.ru.github.dagger;

import dagger.Component;
import geekbrains.ru.github.Presenter;
import geekbrains.ru.github.databases.statistics.Statistics;
import retrofit2.Retrofit;

@Component(modules = {
        DaggerNetModule.class,
        DaggerRoomModule.class,
        DaggerSugarModule.class})

public interface AppComponent {
    void inject(Presenter presenter);

    Retrofit getRetrofit();

    NetworkComponent getNetworkComponent();

    SugarComponent getSugarComponent();

    RoomComponent getRoomComponent();

    Statistics getStatistics();

    DBTestComponent getDBTestComponent(DBTestModule module);
}
