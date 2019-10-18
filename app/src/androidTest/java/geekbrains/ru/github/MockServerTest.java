package geekbrains.ru.github;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import geekbrains.ru.github.dagger.DaggerNetModule;
import geekbrains.ru.github.retrofit.RetrofitModel;
import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subscribers.TestSubscriber;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;


@RunWith(AndroidJUnit4.class)
public class MockServerTest {
    private MockWebServer mockWebServer;

    @Inject
    Single<List<RetrofitModel>> request;

    private static final String login = "test_user";
    private static final String avatarUrl = "test_avatar_url";
    private static final String id = "1";

    @Before
    public void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        DaggerRetrofitModelTestComponent.builder()
                .daggerNetModule(new DaggerNetModule(appContext){
                    @Override
                    public String provideEndpoint() {
                        return mockWebServer.url("/").toString();
                    }})
                .build()
                .inject(this);
    }

    @Test
    public void getUsersSuccess(){
        mockWebServer.enqueue(createMockResponse(login, avatarUrl, id));
            TestSubscriber<Boolean> subscriber = TestSubscriber.create();

        request.subscribeWith(new DisposableSingleObserver<List<RetrofitModel>>() {
                @Override
                public void onSuccess(List<RetrofitModel> retrofitModels) {
                    if(retrofitModels.size() == 0){
                        subscriber.onNext(false);
                        subscriber.onComplete();
                        return;
                    }
                    RetrofitModel model = retrofitModels.get(0);
                    boolean equal = model.getLogin().equals(login) &&
                            model.getAvatarUrl().equals(avatarUrl) &&
                            model.getId().equals(id);
                    subscriber.onNext(equal);
                    subscriber.onComplete();
                }

                @Override
                public void onError(Throwable e) {
                    subscriber.onError(e);
                }
        });
        subscriber.awaitTerminalEvent();
        subscriber.assertValue(true);
        subscriber.dispose();
    }

    @Test
    public void getUsersWithErrorCode(){
        mockWebServer.enqueue(errorResponse(404, "page not found"));
        TestSubscriber<Integer> subscriber = TestSubscriber.create();

        subscriber.onSubscribe(createSubscription());

        request.subscribeWith(new DisposableSingleObserver<List<RetrofitModel>>() {
            @Override
            public void onSuccess(List<RetrofitModel> retrofitModels) {
                subscriber.onNext(200);
                subscriber.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                subscriber.onError(e);
            }
        });
        subscriber.awaitTerminalEvent();
        subscriber.assertError(this::isNotFoundException);
        subscriber.dispose();
    }

    private boolean isNotFoundException(Throwable e) {
        return (e instanceof HttpException) && ((HttpException)e).code() == 404;
    }

    @Test
    public void getUsersWithWrongJson(){
        mockWebServer.enqueue(errorResponse(200, "{ \"dsad\" : }"));
        TestSubscriber<Integer> subscriber = TestSubscriber.create();

        subscriber.onSubscribe(createSubscription());

        request.subscribeWith(new DisposableSingleObserver<List<RetrofitModel>>() {
            @Override
            public void onSuccess(List<RetrofitModel> retrofitModels) {
                subscriber.onNext(200);
                subscriber.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                subscriber.onError(e);
            }
        });
        subscriber.awaitTerminalEvent();
        subscriber.assertError((e)->e instanceof IllegalStateException);
        subscriber.dispose();
    }

    private MockResponse createMockResponse(String login, String avatarUrl, String id) {
        return new MockResponse().setBody("[{\n" +
                "\"login\": \"" + login + "\",\n" +
                "\"avatar_url\": \"" + avatarUrl + "\",\n" +
                "\"id\": \"" + id + "\"\n" +
                "}]");
    }

    private MockResponse errorResponse(int responseCode, String message) {
        return new MockResponse().setResponseCode(responseCode).setBody(message);
    }

    private Subscription createSubscription(){
        return new Subscription() {
            @Override
            public void request(long n) {}
            @Override
            public void cancel() {}
        };
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }
}