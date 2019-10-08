package geekbrains.ru.github.dagger;

import dagger.Component;
import geekbrains.ru.github.MainActivity;
import retrofit2.Retrofit;

@Component(modules = {DaggerNetModule.class, DaggerRoomModule.class, DaggerSugarModule.class})
public interface AppComponent {
    void injectsToMainActivity(MainActivity mainActivity);

    Retrofit getRetrofit();
    NetworkComponent getNetworkComponent();
    SugarComponent sugarComponent();
    RoomComponent roomComponent();
}
