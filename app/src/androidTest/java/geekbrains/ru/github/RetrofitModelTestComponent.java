package geekbrains.ru.github;

import dagger.Component;
import geekbrains.ru.github.dagger.DaggerNetModule;

@Component(modules = DaggerNetModule.class)
public interface RetrofitModelTestComponent {
    void inject(MockServerTest test);
}
