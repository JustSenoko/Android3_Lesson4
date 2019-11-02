package geekbrains.ru.github.dagger;

import dagger.Subcomponent;
import geekbrains.ru.github.retrofit.RetrofitHelper;

@Subcomponent(modules = {DaggerNetModule.class})
public interface NetworkComponent {
    Boolean checkConnection();
    RetrofitHelper getRetrofitHelper();
}


