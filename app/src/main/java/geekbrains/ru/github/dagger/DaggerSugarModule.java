package geekbrains.ru.github.dagger;

import dagger.Module;
import dagger.Provides;
import geekbrains.ru.github.databases.sugar.SugarHelper;

@Module
public class DaggerSugarModule {
    @Provides
    SugarHelper getSugarHelper(){
        return new SugarHelper();
    }
}
