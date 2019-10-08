package geekbrains.ru.github.retrofit;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RetrofitHelper {
    private RestApi api;
    public RetrofitHelper(RestApi api){
        this.api = api;
    }

    public Single<RetrofitModel> getUserInfo(String userName){
        return api.getUser(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<RepositoryModel>> getUserRepos(String userName) {
        return api.getUserRepos(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
