package geekbrains.ru.github.retrofit;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestApi {
    @GET("users/{path}")
    Single<RetrofitModel> getUser(@Path("path") String user);

    @GET("users/{user}/repos")
    Single<List<RepositoryModel>> getUserRepos(@Path("user") String user);

    @GET("users")
    Single<List<RetrofitModel>> getUsers();
}
