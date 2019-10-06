package geekbrains.ru.github.dagger;

import dagger.Subcomponent;
import geekbrains.ru.github.databases.sugar.SugarHelper;

@RetrofitScope
@Subcomponent(modules = {DaggerRoomModule.class})
public interface SugarComponent {
    SugarHelper getSugarHelper();
}


