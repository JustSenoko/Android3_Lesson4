package geekbrains.ru.github.dagger;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import geekbrains.ru.github.retrofit.RestApi;
import geekbrains.ru.github.retrofit.RetrofitHelper;
import geekbrains.ru.github.retrofit.RetrofitModel;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DaggerNetModule {
    final Context context;

    //public DaggerNetModule() {}

    public DaggerNetModule(Context context){
        this.context = context;
    }

    @Provides
    Retrofit makeRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    RestApi makeRestApi(Retrofit retrofit){
        return retrofit.create(RestApi.class);
    }
//для загрузки пользователей мы создаем объект запроса
// он в качестве параметра требует RestAPI который автоматически подставляется из метода makeRestApi
// так как в том же компоненте
    @Provides
    Single<List<RetrofitModel>> getCall(RestApi api){
        return api.getUsers().
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Provides
    public NetworkInfo getNetworkInfo(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null) return null;
        return connectivityManager.getActiveNetworkInfo();
    }

    @Provides
    public RetrofitHelper getRetrofitHelper(RestApi api) {
        return new RetrofitHelper(api);
    }
}
