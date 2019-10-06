package geekbrains.ru.github.databases.room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import geekbrains.ru.github.OrmApp;
import geekbrains.ru.github.databases.Statistics;
import geekbrains.ru.github.retrofit.RetrofitModel;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RoomHelper {
    public RoomHelper(){}

    public Single<Statistics> saveAll(List<RetrofitModel> modelList){
        return Single.create((SingleOnSubscribe<Statistics>) emitter -> {
            String curLogin;
            String curUserID;
            String curAvatarUrl;
            Date first = new Date();
            List<RoomModel> roomModelList = new ArrayList<>();
            for (RetrofitModel curItem : modelList) {
                RoomModel roomModel = new RoomModel();
                curLogin = curItem.getLogin();
                curUserID = curItem.getId();
                curAvatarUrl = curItem.getAvatarUrl();
                roomModel.setLogin(curLogin);
                roomModel.setAvatarUrl(curAvatarUrl);
                roomModel.setUserId(curUserID);
                roomModelList.add(roomModel);
            }
            OrmApp.get().getDB().productDao().insertAll(roomModelList);
            Date second = new Date();
            List<RoomModel> tempList = OrmApp.get().getDB().productDao().getAll();
            emitter.onSuccess(new Statistics(first, second, tempList.size()));
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Statistics> selectAll(){
        return Single.create((SingleOnSubscribe<Statistics>) emitter -> {
            try {
                Date first = new Date();
                List<RoomModel> products = OrmApp.get().getDB().productDao().getAll();
                emitter.onSuccess(new Statistics(first, new Date(), products.size()));
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Statistics> deleteAll(){
        return Single.create((SingleOnSubscribe<Statistics>) emitter -> {
            try {
                List<RoomModel> products = OrmApp.get().getDB().productDao().getAll();
                Date first = new Date();
                OrmApp.get().getDB().productDao().deleteAll();

                emitter.onSuccess(new Statistics(first, new Date(), products.size()));
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
