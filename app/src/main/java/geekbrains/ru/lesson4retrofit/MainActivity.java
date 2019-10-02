package geekbrains.ru.lesson4retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import geekbrains.ru.lesson4retrofit.models.RepositoryModel;
import geekbrains.ru.lesson4retrofit.models.RetrofitModel;
import geekbrains.ru.lesson4retrofit.models.SugarModel;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {
    private TextView mInfoTextView;
    private TextView mSugarTextView;
    private TextView mRoomTextView;
    private TextView mReposTextView;
    private EditText editText;

    List<RetrofitModel> modelList = new ArrayList<>();

    private Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build();

    public interface RestApi {
        @GET("users/{path}")
        Single<RetrofitModel> getUser(@Path("path") String user);

        @GET("users/{user}/repos")
        Single<List<RepositoryModel>> getUserRepos(@Path("user") String user);
    }

    class Statistics {
        private Long time;
        private Integer recordsCount;

        Statistics(Date start, Date finish, Integer recordsCount) {
            this.time = finish.getTime() - start.getTime();
            this.recordsCount = recordsCount;
        }

        @SuppressLint("DefaultLocale")
        String getResult() {
            return String.format("количество = %d\nмилисекунд = %d", recordsCount, time);
        }
    }

    RestApi api = retrofit.create(RestApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SugarContext.init(this);
        initButtons();
    }

    private void initButtons() {
        editText = findViewById(R.id.editText);
        mInfoTextView = findViewById(R.id.tvLoad);
        mSugarTextView = findViewById(R.id.sugar_result);
        mRoomTextView = findViewById(R.id.room_result);
        mReposTextView = findViewById(R.id.tvRepos);
        Button btnSaveAllSugar = findViewById(R.id.btnSaveAllSugar);
        Button btnSelectAllSugar = findViewById(R.id.btnSelectAllSugar);
        Button btnDeleteAllSugar = findViewById(R.id.btnDeleteAllSugar);
        Button btnSaveAllRoom = findViewById(R.id.btnSaveAllRoom);
        Button btnSelectAllRoom = findViewById(R.id.btnSelectAllRoom);
        Button btnDeleteAllRoom = findViewById(R.id.btnDeleteAllRoom);

        Button btnLoad = findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener((v) -> loadUserInfoOnClick());
        btnSaveAllSugar.setOnClickListener(view -> saveAllSugar());
        btnSelectAllSugar.setOnClickListener(view -> selectAllSugar());
        btnDeleteAllSugar.setOnClickListener(view -> deleteAllSugar());
        btnSaveAllRoom.setOnClickListener(view -> saveAllRoom());
        btnSelectAllRoom.setOnClickListener(view -> selectAllRoom());
        btnDeleteAllRoom.setOnClickListener(view -> deleteAllRoom());
    }

    private void deleteAllRoom() {

    }

    private void selectAllRoom() {

    }

    private void saveAllRoom() {
        /*io.reactivex.Single<Bundle> singleSaveAllRoom = io.reactivex.Single.create(new SingleOnSubscribe<Bundle>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<Bundle> emitter) throws Exception {
                String curLogin = "";
                String curUserID = "";
                String curAvatarUrl = "";
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

                    OrmApp.get().getDB().productDao().insertAll(roomModelList);
                }
                Date second = new Date();
                Bundle bundle = new Bundle();
                List<RoomModel> tempList = OrmApp.get().getDB().productDao().getAll();
                bundle.putInt("count", tempList.size());
                bundle.putLong("msek", second.getTime() - first.getTime());
                emitter.onSuccess(bundle);
            }
        }).subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        singleSaveAllRoom.subscribeWith(CreateObserverSugar());*/
    }

    private DisposableSingleObserver<Statistics> CreateObserverSugar() {
        return new DisposableSingleObserver<Statistics>() {
            @Override
            protected void onStart() {
                super.onStart();
                mSugarTextView.setText("");
            }

            @Override
            public void onSuccess(@NonNull Statistics s) {
                mSugarTextView.setText(s.getResult());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mSugarTextView.setText(String.format("%s%s", getResources().getString(R.string.error), e.getMessage()));
            }
        };
    }

    @SuppressLint("CheckResult")
    private void deleteAllSugar() {
        io.reactivex.Single<Statistics> singleDeleteAll = io.reactivex.Single.create((SingleOnSubscribe<Statistics>) emitter -> {
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
        singleDeleteAll.subscribeWith(CreateObserverSugar());
    }

    private void selectAllSugar() {
        io.reactivex.Single<Statistics> singleSelectAll = io.reactivex.Single.create((SingleOnSubscribe<Statistics>) emitter -> {
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
        singleSelectAll.subscribeWith(CreateObserverSugar());
    }

    private void saveAllSugar() {
        Single<Statistics> singleSaveAll = Single.create((SingleOnSubscribe<Statistics>) emitter -> {
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
        singleSaveAll.subscribeWith(CreateObserverSugar());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isConnected()) {
            Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void loadUserInfoOnClick() {
        if (checkInternet()) return;

        downloadOneUrl(editText.getText().toString());
    }

    private void downloadOneUrl(String request) {

        api.getUser(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<RetrofitModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mInfoTextView.setText("");
                    }

                    @Override
                    public void onSuccess(RetrofitModel value) {
                        mInfoTextView.setText(value.getAvatarUrl());
                        modelList.add(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });

        api.getUserRepos(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<RepositoryModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mReposTextView.setText("");
                    }

                    @Override
                    public void onSuccess(List<RepositoryModel> value) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < value.size(); i++) {
                            sb.append(value.get(i).getFullName());
                            sb.append("\n");
                        }
                        mReposTextView.setText(sb.toString());
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                    }
                });
    }
}
