package geekbrains.ru.github.dagger;

import dagger.Component;
import geekbrains.ru.github.MainActivity;
import retrofit2.Retrofit;

@Component(modules = {DaggerNetModule.class, DaggerRoomModule.class, DaggerSugarModule.class})
public interface AppComponent {
    void injectsToMainActivity(MainActivity mainActivity);

    Retrofit getRetrofit();
//    Single<RetrofitModel> getUserInfo(String userName);
//    Single<List<RepositoryModel>> getUserRepos(String userName);
    NetworkComponent getNetworkComponent();
    SugarComponent sugarComponent();
    RoomComponent roomComponent();
}
