package geekbrains.ru.github.dagger;

import dagger.Module;
import dagger.Provides;
import geekbrains.ru.github.databases.room.RoomHelper;
import geekbrains.ru.github.databases.statistics.Statistics;

@Module
class DaggerRoomModule {
    @Provides
    RoomHelper getRoomHelper(){
        return new RoomHelper();
    }

    @Provides
    Statistics getStatistics() {
        return new Statistics();
    }
}
