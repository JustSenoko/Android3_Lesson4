package geekbrains.ru.github.dagger;

import android.net.NetworkInfo;

import dagger.Subcomponent;

@Subcomponent(modules = {DaggerNetModule.class})
public interface NetworkComponent {
    NetworkInfo getNetwork();
}


