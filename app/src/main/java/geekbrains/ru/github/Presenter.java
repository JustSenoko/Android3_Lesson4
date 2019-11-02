package geekbrains.ru.github;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import geekbrains.ru.github.dagger.AppComponent;
import geekbrains.ru.github.dagger.DBTestComponent;
import geekbrains.ru.github.dagger.DBTestModule;
import geekbrains.ru.github.dagger.NetworkComponent;
import geekbrains.ru.github.databases.statistics.StatisticsRecord;
import geekbrains.ru.github.retrofit.RepositoryModel;
import geekbrains.ru.github.retrofit.RetrofitHelper;
import geekbrains.ru.github.retrofit.RetrofitModel;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
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
    }

    void unbindView() {
        showUserInfo.onComplete();
        showUserReposInfo.onComplete();
        showSugarInfo.onComplete();
        showRoomInfo.onComplete();
    }

    private boolean checkInternet() {
        NetworkComponent networkComponent = OrmApp.getNetworkComponent();
        return networkComponent.checkConnection();
    }

    void loadUserInfo(String userName) {
        if (!checkInternet()) {
            showUserInfo.onNext("");
            showUserReposInfo.onNext("");
            return;
        }

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

    private void addObserver(Single<StatisticsRecord> test, DisposableSingleObserver<String> info){
        test.map((result)-> result.getResult())
                .onErrorResumeNext((e)->Single.just("ошибка БД: " + e.getMessage())).subscribeWith(info);
    }

    void saveAllRoom(DisposableSingleObserver<String> info) {
        if(testComponent == null){
            info.onSuccess("");
            return;
        }
        addObserver(testComponent.runRoomSave(), info);
    }

    void selectAllRoom(DisposableSingleObserver<String> info){
        if(testComponent == null){
            info.onSuccess("");
            return;
        }
        addObserver(testComponent.runRoomGet(), info);
    }

    void deleteAllRoom(DisposableSingleObserver<String> info){
        if(testComponent == null){
            info.onSuccess("");
            return;
        }
        addObserver(testComponent.runRoomDelete(), info);
    }

    void saveAllSugar(DisposableSingleObserver<String> info) {
        if(testComponent == null){
            info.onSuccess("");
            return;
        }
        addObserver(testComponent.runSugarSave(), info);
    }

    void deleteAllSugar(DisposableSingleObserver<String> info){
        if(testComponent == null){
            info.onSuccess("");
            return;
        }
        addObserver(testComponent.runSugarDelete(), info);
    }

    void selectAllSugar(DisposableSingleObserver<String> info){
        if(testComponent == null){
            info.onSuccess("");
            return;
        }
        addObserver(testComponent.runSugarGet(), info);
    }

}
