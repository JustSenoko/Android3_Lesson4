package geekbrains.ru.github.databases.sugar;

import java.util.Date;
import java.util.List;

import geekbrains.ru.github.databases.Statistics;
import geekbrains.ru.github.retrofit.RetrofitModel;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SugarHelper {
    public SugarHelper(){}

    public Single<Statistics> saveAll(List<RetrofitModel> modelList){
        return Single.create((SingleOnSubscribe<Statistics>) emitter -> {
            try {
                String curLogin;
                String curUserID;
                String curAvatarUrl;
                Date first = new Date();
                for (RetrofitModel curItem : modelList) {
                    curLogin = curItem.getLogin();
                    curUserID = curItem.getId();
                    curAvatarUrl = curItem.getAvatarUrl();
                    SugarModel sugarModel = new SugarModel(curLogin, curUserID, curAvatarUrl);
                    sugarModel.save();
                }
                List<SugarModel> tempList = SugarModel.listAll(SugarModel.class);
                Statistics statistics = new Statistics(first, new Date(), tempList.size());
                emitter.onSuccess(statistics);
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Statistics> selectAll(){
        return Single.create((SingleOnSubscribe<Statistics>) emitter -> {
            try {
                Date first = new Date();
                List<SugarModel> tempList = SugarModel.listAll(SugarModel.class);
                Statistics statistics = new Statistics(first, new Date(), tempList.size());
                emitter.onSuccess(statistics);
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Statistics> deleteAll(){
        return Single.create((SingleOnSubscribe<Statistics>) emitter -> {
            try {
                List<SugarModel> tempList = SugarModel.listAll(SugarModel.class);
                Date first = new Date();
                SugarModel.deleteAll(SugarModel.class);
                Statistics statistics = new Statistics(first, new Date(), tempList.size());
                emitter.onSuccess(statistics);
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
