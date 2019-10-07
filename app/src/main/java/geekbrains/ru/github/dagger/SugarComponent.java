package geekbrains.ru.github.dagger;

import dagger.Subcomponent;
import geekbrains.ru.github.databases.sugar.SugarHelper;

@Subcomponent(modules = {DaggerSugarModule.class})
public interface SugarComponent {
    SugarHelper getSugarHelper();
}


