package geekbrains.ru.github.dagger;

import dagger.Subcomponent;
import geekbrains.ru.github.databases.room.RoomHelper;

@RetrofitScope
@Subcomponent(modules = {DaggerRoomModule.class})
public interface RoomComponent {
    RoomHelper getRoomHelper();
}


