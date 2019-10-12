package geekbrains.ru.github.dagger;

import dagger.Module;
import dagger.Provides;
import geekbrains.ru.github.databases.room.RoomHelper;

@Module
public class DaggerRoomModule {
    @Provides
    RoomHelper getRoomHelper(){
        return new RoomHelper();
    }
}
