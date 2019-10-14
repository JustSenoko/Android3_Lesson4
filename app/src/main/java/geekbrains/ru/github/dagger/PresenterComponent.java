package geekbrains.ru.github.dagger;

import javax.inject.Singleton;

import dagger.Component;
import geekbrains.ru.github.MainActivity;

@Singleton
@Component(modules = {PresenterModule.class})
public interface PresenterComponent {
    void inject(MainActivity mainActivity);
}
