package geekbrains.ru.github.dagger;

import android.net.NetworkInfo;

import dagger.Subcomponent;
import geekbrains.ru.github.retrofit.RetrofitHelper;

@Subcomponent(modules = {DaggerNetModule.class})
public interface NetworkComponent {
    NetworkInfo getNetwork();
    Boolean checkConnection();
    RetrofitHelper getRetrofitHelper();
}


