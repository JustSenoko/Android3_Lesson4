package geekbrains.ru.lesson4retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Single;
import rx.SingleSubscriber;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private TextView mInfoTextView;
    private TextView mReposTextView;
    private ProgressBar progressBar;
    private EditText editText;
    private Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build();

    public interface RestApi{
        @GET("users/{path}")
        Single<RetrofitModel> getUser(@Path("path") String user);

        @GET("users/{user}/repos")
        Single<List<RepositoryModel>> getUserRepos(@Path("user") String user);
    }

    RestApi api = retrofit.create(RestApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        mInfoTextView = findViewById(R.id.tvLoad);
        mReposTextView = findViewById(R.id.tvRepos);
        progressBar = findViewById(R.id.progressBar);
        Button btnLoad = findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener((v)->onClick());
    }

    private boolean checkInternet() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isConnected()) {
            Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void onClick() {
        if (checkInternet()) return;

        downloadOneUrl(editText.getText().toString());
    }

    private void downloadOneUrl(String request) {

        api.getUser(request)
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<RetrofitModel>() {
            @Override
            public void onSuccess(RetrofitModel value) {
                mInfoTextView.setText(value.getUrl());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);

            }
        });

        api.getUserRepos(request)
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<List<RepositoryModel>>() {
            @Override
            public void onSuccess(List<RepositoryModel> value) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < value.size(); i++) {
                    sb.append(value.get(i).getFullName());
                    sb.append(", ");
                }
                mReposTextView.setText(sb.toString());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);

            }
        });
    }
}
