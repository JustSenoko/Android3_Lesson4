package geekbrains.ru.github;

import android.annotation.SuppressLint;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import geekbrains.ru.github.dagger.AppComponent;
import geekbrains.ru.github.dagger.NetworkComponent;
import geekbrains.ru.github.databases.Statistics;
import geekbrains.ru.github.databases.room.RoomHelper;
import geekbrains.ru.github.databases.sugar.SugarHelper;
import geekbrains.ru.github.retrofit.RestApi;
import geekbrains.ru.github.retrofit.RetrofitModel;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

public class MainActivity extends AppCompatActivity {
    private TextView mInfoTextView;
    private TextView mSugarTextView;
    private TextView mRoomTextView;
    private TextView mReposTextView;
    private EditText editText;
    private List<Statistics> roomStatistics = new ArrayList<>();
    private List<Statistics> sugarStatistics = new ArrayList<>();

    @Inject
    RoomHelper roomHelper;
    @Inject
    SugarHelper sugarHelper;
    private NetworkComponent networkComponent;

    List<RetrofitModel> modelList = new ArrayList<>();

    @Inject
    RestApi api;

    @Inject
    Single<List<RetrofitModel>> request;

    @SuppressLint("DefaultLocale")
    String getResult(List<Statistics> statisticsList) {
        if (statisticsList.size() == 0) {
            return "";
        }
        Statistics lastEntry = statisticsList.get(statisticsList.size() - 1);
        return String.format("количество = %d\nмилисекунд = %d\nсреднее = %d",
                lastEntry.recordsCount, lastEntry.time, getTimeAvg(statisticsList));
    }

    private Long getTimeAvg(List<Statistics> statisticsList) {
        int size = statisticsList.size();
        if (size == 0) {
            return 0L;
        }
        Long sum = 0L;
        for (int i = 0; i < size; i++) {
            sum += statisticsList.get(i).time;
        }
        return sum / size;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppComponent appComponent = OrmApp.getComponent();
        appComponent.injectsToMainActivity(this);//вызывая этот метод мы инициализируем все Inject, которые данный компонент может
        roomHelper = appComponent.roomComponent().getRoomHelper();
        sugarHelper = appComponent.sugarComponent().getSugarHelper();
        networkComponent = OrmApp.getNetworkComponent(this);
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
        btnSaveAllSugar.setOnClickListener(view -> sugarHelper.saveAll(modelList).subscribeWith(createObserver("sugar")));
        btnSelectAllSugar.setOnClickListener(view -> sugarHelper.selectAll().subscribeWith(createObserver("sugar")));
        btnDeleteAllSugar.setOnClickListener(view -> sugarHelper.deleteAll().subscribeWith(createObserver("sugar")));
        btnSaveAllRoom.setOnClickListener(view -> roomHelper.saveAll(modelList).subscribeWith(createObserver("room")));
        btnSelectAllRoom.setOnClickListener(view -> roomHelper.selectAll().subscribeWith(createObserver("room")));
        btnDeleteAllRoom.setOnClickListener(view -> roomHelper.deleteAll().subscribeWith(createObserver("room")));
    }

    private DisposableSingleObserver<Statistics> createObserver(String dbName) {
        TextView textView;
        List<Statistics> stat;
        if (dbName.equals("sugar")) {
            textView = mSugarTextView;
            stat = sugarStatistics;
        } else {
            textView = mRoomTextView;
            stat = roomStatistics;
        }
        return new DisposableSingleObserver<Statistics>() {
            @Override
            protected void onStart() {
                super.onStart();
                textView.setText("");
            }

            @Override
            public void onSuccess(@NonNull Statistics s) {
                stat.add(s);
                textView.setText(getResult(stat));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                textView.setText(String.format("%s%s", getResources().getString(R.string.error), e.getMessage()));
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }

    private boolean checkInternet() {
        NetworkInfo networkInfo = networkComponent.getNetwork();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void loadUserInfoOnClick() {
        if (checkInternet()) return;

        loadUsers();
    }

    private void loadUsers() {
        mInfoTextView.setText("");
        modelList.clear();
        request.subscribe(new SingleObserver<List<RetrofitModel>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mInfoTextView.setText("");
            }

            @Override
            public void onSuccess(List<RetrofitModel> value) {
                for (RetrofitModel curModel : value) {
                    mInfoTextView.append(curModel.getAvatarUrl());
                    modelList.add(curModel);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });

        /*api.getUserRepos(request)
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
                });*/
    }
}
