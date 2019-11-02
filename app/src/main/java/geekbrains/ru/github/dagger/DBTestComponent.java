package geekbrains.ru.github.dagger;

import dagger.Subcomponent;
import geekbrains.ru.github.databases.statistics.StatisticsRecord;
import io.reactivex.Single;

@Subcomponent(modules = DBTestModule.class)
public interface DBTestComponent {
    @GetSugar Single<StatisticsRecord> runSugarGet();
    @GetRoom Single<StatisticsRecord> runRoomGet();
    @DeleteSugar Single<StatisticsRecord> runSugarDelete();
    @DeleteRoom Single<StatisticsRecord> runRoomDelete();
    @SaveSugar Single<StatisticsRecord> runSugarSave();
    @SaveRoom Single<StatisticsRecord> runRoomSave();
}
