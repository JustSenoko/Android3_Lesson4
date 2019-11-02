package geekbrains.ru.github.dagger;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import geekbrains.ru.github.OrmApp;
import geekbrains.ru.github.databases.room.RoomModel;
import geekbrains.ru.github.databases.statistics.StatisticsRecord;
import geekbrains.ru.github.databases.sugar.SugarModel;
import geekbrains.ru.github.retrofit.RetrofitModel;
import geekbrains.ru.github.tests.DBOperations;
import geekbrains.ru.github.tests.TestHandler;
import io.reactivex.Single;

@Module
public class DBTestModule {
    private List<RetrofitModel> models;

    public DBTestModule(List<RetrofitModel> modelList){
        this.models = modelList;
    }

    @Provides
    DBOperations<SugarModel> getSugarOperations(){
        return new DBOperations<SugarModel>() {
            @Override
            public List<SugarModel> getAll() {
                return SugarModel.listAll(SugarModel.class);
            }

            @Override
            public void saveAll(List<RetrofitModel> modelList) {
                for (RetrofitModel curItem : modelList) {
                    new SugarModel(curItem.getLogin(), curItem.getId(), curItem.getAvatarUrl()).save();
                }
            }

            @Override
            public void deleteAll() {
                SugarModel.deleteAll(SugarModel.class);
            }
        };
    }

    @Provides
    DBOperations<RoomModel> getRoomOperations(){
        return new DBOperations<RoomModel>() {
            @Override
            public List<RoomModel> getAll() {
                return OrmApp.get().getDB().productDao().getAll();
            }

            @Override
            public void saveAll(List<RetrofitModel> modelList) {
                List<RoomModel> roomModelList = new ArrayList<>();
                RoomModel roomModel = new RoomModel();
                for (RetrofitModel curItem : modelList) {
                    roomModel.setLogin(curItem.getLogin());
                    roomModel.setAvatarUrl(curItem.getAvatarUrl());
                    roomModel.setUserId(curItem.getId());
                    roomModelList.add(roomModel);
                }
                OrmApp.get().getDB().productDao().insertAll(roomModelList);
            }

            @Override
            public void deleteAll() {
                OrmApp.get().getDB().productDao().deleteAll();
            }
        };
    }

    @Provides
    TestHandler<SugarModel> getSugarTestHandler(){
        return new TestHandler<SugarModel>();
    }

    @Provides
    TestHandler<RoomModel> getRoomTestHandler(){
        return new TestHandler<RoomModel>();
    }

    @Provides
    @GetSugar
    Single<StatisticsRecord> runSugarGet(DBOperations<SugarModel> modelDBOperations, TestHandler<SugarModel> handler){
        return handler.run(handler.makeGetTest(modelDBOperations));
    }

    @Provides
    @GetRoom
    Single<StatisticsRecord> runRoomGetTest(DBOperations<RoomModel> modelDBOperations, TestHandler<RoomModel> handler){
        return handler.run(handler.makeGetTest(modelDBOperations));
    }

    @Provides
    @DeleteSugar
    Single<StatisticsRecord> runSugarDeleteTest(DBOperations<SugarModel> modelDBOperations, TestHandler<SugarModel> handler){
        return handler.run(handler.makeDeleteTest(modelDBOperations));
    }

    @Provides
    @DeleteRoom
    Single<StatisticsRecord> runRoomDeleteTest(DBOperations<RoomModel> modelDBOperations, TestHandler<RoomModel> handler){
        return handler.run(handler.makeDeleteTest(modelDBOperations));
    }

    @Provides
    @SaveSugar
    Single<StatisticsRecord> runSugarSaveTest(DBOperations<SugarModel> modelDBOperations, TestHandler<SugarModel> handler){
        return handler.run(handler.makeSaveTest(modelDBOperations, models));
    }

    @Provides
    @SaveRoom
    Single<StatisticsRecord> runRoomSaveTest(DBOperations<RoomModel> modelDBOperations, TestHandler<RoomModel> handler){
        return handler.run(handler.makeSaveTest(modelDBOperations, models));
    }
}
