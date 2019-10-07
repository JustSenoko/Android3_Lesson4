package geekbrains.ru.github.dagger;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import geekbrains.ru.github.retrofit.RestApi;
import geekbrains.ru.github.retrofit.RetrofitModel;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DaggerNetModule {
    //final Context data;

    public DaggerNetModule() {}

    //public DaggerNetModule(Context data){
//        this.data = data;
//    }

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
}
