package geekbrains.ru.github.dagger;

import dagger.Component;
import geekbrains.ru.github.MainActivity;

@Component(modules = {DaggerNetModule.class})
    public interface AppComponent {
        void injectsToMainActivity(MainActivity mainActivity);
//        Retrofit getRetrofit();
//        SugarComponent sugarComponent();
//        RoomComponent roomComponent();
    }
