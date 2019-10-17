package geekbrains.ru.github.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import geekbrains.ru.github.OrmApp;
import geekbrains.ru.github.Presenter;

@Singleton
@Module
class PresenterModule {

    PresenterModule() {}
    @Singleton
    @Provides
    Presenter getPresenter(){
        return new Presenter(OrmApp.getComponent());
    }
}
