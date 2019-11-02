package geekbrains.ru.github;

import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import geekbrains.ru.github.dagger.AppComponent;
import geekbrains.ru.github.dagger.DBTestComponent;
import geekbrains.ru.github.dagger.DBTestModule;
import geekbrains.ru.github.dagger.NetworkComponent;
import geekbrains.ru.github.databases.room.RoomHelper;
import geekbrains.ru.github.databases.statistics.Statistics;
import geekbrains.ru.github.databases.statistics.StatisticsRecord;
import geekbrains.ru.github.databases.sugar.SugarHelper;
import geekbrains.ru.github.retrofit.RepositoryModel;
import geekbrains.ru.github.retrofit.RetrofitHelper;
import geekbrains.ru.github.retrofit.RetrofitModel;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.PublishSubject;

public class Presenter {
    private PublishSubject<String> showUserInfo = PublishSubject.create();
    private PublishSubject<String> showUserReposInfo = PublishSubject.create();
    private PublishSubject<String> showRoomInfo = PublishSubject.create();
    private PublishSubject<String> showSugarInfo = PublishSubject.create();

    private DBTestComponent testComponent;

    private List<RetrofitModel> modelList = new ArrayList<>();

    @Inject
    Statistics statistics;
    @Inject
    RoomHelper roomHelper;
    @Inject
    SugarHelper sugarHelper;
    @Inject
    RetrofitHelper retrofitHelper;

    public Presenter(AppComponent component) {
        component.inject(this);
    }

    void bindView(DisposableObserver<String> showInfo,
                  DisposableObserver<String> showUserReposInfo,
                  DisposableObserver<String> showRoomInfo,
                  DisposableObserver<String> showSugarInfo) {
        this.showRoomInfo.subscribe(showRoomInfo);
        this.showSugarInfo.subscribe(showSugarInfo);
        this.showUserInfo.subscribe(showInfo);
        this.showUserReposInfo.subscribe(showUserReposInfo);

        this.roomHelper = OrmApp.getRoomComponent().getRoomHelper();
        this.sugarHelper = OrmApp.getSugarComponent().getSugarHelper();
    }

    void unbindView() {
        showUserInfo.onComplete();
        showUserReposInfo.onComplete();
        showSugarInfo.onComplete();
        showRoomInfo.onComplete();
    }

    boolean checkInternet() {
        NetworkComponent networkComponent = OrmApp.getNetworkComponent();
        NetworkInfo networkInfo = networkComponent.getNetwork();
        return networkInfo != null || networkInfo.isConnected();
    }

    void loadUserInfo(String userName) {
        retrofitHelper.getUserInfo(userName)
                .subscribe(new SingleObserver<RetrofitModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showUserInfo.onNext("");
                    }

                    @Override
                    public void onSuccess(RetrofitModel value) {
                        showUserInfo.onNext(value.getAvatarUrl());
                        modelList.add(value);
                        testComponent = OrmApp.getDbTestComponent(new DBTestModule(modelList));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });

        retrofitHelper.getUserRepos(userName)
                .subscribe(new SingleObserver<List<RepositoryModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        showUserReposInfo.onNext("");
                    }

                    @Override
                    public void onSuccess(List<RepositoryModel> value) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < value.size(); i++) {
                            sb.append(value.get(i).getFullName());
                            sb.append("\n");
                        }
                        showUserReposInfo.onNext(sb.toString());
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });
    }

    private DisposableSingleObserver<StatisticsRecord> createObserver(String dbName) {
        PublishSubject<String> show;

        if (dbName.equals("sugar")) {
            show = showSugarInfo;
        } else {
            show = showRoomInfo;
        }
        return new DisposableSingleObserver<StatisticsRecord>() {
            @Override
            protected void onStart() {
                super.onStart();
                show.onNext("");
            }

            @Override
            public void onSuccess(@NonNull StatisticsRecord s) {
                statistics.addRecord(dbName, s);
                show.onNext(statistics.getResult(dbName));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                show.onNext("ошибка БД: " + e.getMessage());
            }
        };
    }

    void saveAllRoom() {
        roomHelper.saveAll(modelList).subscribeWith(createObserver("room"));
    }

    void selectAllRoom() {
        roomHelper.selectAll().subscribeWith(createObserver("room"));
    }

    void deleteAllRoom() {
        roomHelper.deleteAll().subscribeWith(createObserver("room"));
    }

    void saveAllSugar() {
        sugarHelper.saveAll(modelList).subscribeWith(createObserver("sugar"));
    }

    void deleteAllSugar() {
        sugarHelper.deleteAll().subscribeWith(createObserver("sugar"));
    }

    void selectAllSugar() {
        sugarHelper.selectAll().subscribeWith(createObserver("sugar"));
    }

}
