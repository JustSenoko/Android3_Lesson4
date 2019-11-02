package geekbrains.ru.github;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.orm.SugarContext;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;

public class MainActivity extends AppCompatActivity {
    private TextView mInfoTextView;
    private TextView mSugarTextView;
    private TextView mRoomTextView;
    private TextView mReposTextView;
    private EditText editText;

    private DisposableSingleObserver<String> showSugar;
    private DisposableSingleObserver<String> showRoom;

    @Inject
    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OrmApp.getPresenterComponent().inject(this);
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

        findViewById(R.id.btnLoad).setOnClickListener((v) -> loadUserInfo());
        findViewById(R.id.btnSaveAllSugar).setOnClickListener(view -> presenter.saveAllSugar(makeObserver(showSugar, mSugarTextView)));
        findViewById(R.id.btnSelectAllSugar).setOnClickListener(view -> presenter.selectAllSugar(makeObserver(showSugar, mSugarTextView)));
        findViewById(R.id.btnDeleteAllSugar).setOnClickListener(view -> presenter.deleteAllSugar(makeObserver(showSugar, mSugarTextView)));
        findViewById(R.id.btnSaveAllRoom).setOnClickListener(view -> presenter.saveAllRoom(makeObserver(showRoom, mRoomTextView)));
        findViewById(R.id.btnSelectAllRoom).setOnClickListener(view -> presenter.selectAllRoom(makeObserver(showRoom, mRoomTextView)));
        findViewById(R.id.btnDeleteAllRoom).setOnClickListener(view -> presenter.deleteAllRoom(makeObserver(showRoom, mRoomTextView)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        DisposableObserver<String> showInfo = createObserver(mInfoTextView);
        DisposableObserver<String> showReposInfo = createObserver(mReposTextView);

        DisposableObserver<String> showRoom = createObserver(mRoomTextView);
        DisposableObserver<String> showSugar = createObserver(mSugarTextView);

        presenter.bindView(showInfo, showReposInfo, showRoom, showSugar);
    }

    DisposableSingleObserver<String> makeObserver(DisposableSingleObserver<String> showSugar, TextView sugarTextView){
        if(showSugar != null && !showSugar.isDisposed()) showSugar.dispose();

        showSugar = new DisposableSingleObserver<String>() {
            @Override
            public void onSuccess(String s) {
                sugarTextView.setText(s);
            }

            @Override
            public void onError(Throwable e) {
                sugarTextView.setText(e.getLocalizedMessage());
            }
        };
        this.showSugar = showSugar;
        return showSugar;
    }

    private DisposableObserver<String> createObserver(TextView textView) {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                textView.setText(s);
            }

            @Override
            public void onError(Throwable e) {
                textView.setText(e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unbindView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarContext.terminate();
    }

    private void loadUserInfo() {
        if (!presenter.checkInternet()) {
            Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();
            return;
        }
        presenter.loadUserInfo(editText.getText().toString());
    }
}
