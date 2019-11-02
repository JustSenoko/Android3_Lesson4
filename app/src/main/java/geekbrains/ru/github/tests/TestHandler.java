package geekbrains.ru.github.tests;

import java.util.Date;
import java.util.List;

import geekbrains.ru.github.databases.statistics.StatisticsRecord;
import geekbrains.ru.github.retrofit.RetrofitModel;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestHandler<T> {
    public SpeedTestDB makeGetTest(DBOperations<T> modelDBOperations){
        return new ShortenedSpeedTestDB() {
            List<T> tempList;
            @Override
            public void test() {
                tempList = modelDBOperations.getAll();
            }

            @Override
            public int getCount() {
                return tempList.size();
            }
        };
    }

    public SpeedTestDB makeDeleteTest(DBOperations<T> modelDBOperations){
        return new ShortenedSpeedTestDB() {
            int count;
            @Override
            public void before() {
                count = modelDBOperations.getAll().size();
            }

            @Override
            public void test() {
                modelDBOperations.deleteAll();
            }

            @Override
            public int getCount() {
                return count;
            }
        };
    }

    public SpeedTestDB makeSaveTest(DBOperations<T> modelDBOperations, List<RetrofitModel> data){
        return new ShortenedSpeedTestDB() {
            int count;
            @Override
            public void test() {
                    modelDBOperations.saveAll(data);
            }

            @Override
            public void after() {
                count = modelDBOperations.getAll().size();
            }

            @Override
            public int getCount() {
                return count;
            }
        };
    }

    public Single<StatisticsRecord> run(SpeedTestDB testDBOperation){
        return Single.create((SingleOnSubscribe<StatisticsRecord>) emitter -> {
            try {
                testDBOperation.before();
                Date first = new Date();
                testDBOperation.test();
                Date second = new Date();
                testDBOperation.after();
                emitter.onSuccess(new StatisticsRecord(first, second, testDBOperation.getCount()));
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
