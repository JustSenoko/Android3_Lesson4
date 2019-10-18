package geekbrains.ru.github;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Test;
import org.mockito.Mockito;

import geekbrains.ru.github.dagger.DaggerNetModule;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void getNetworkInfo_null_activity() {
        DaggerNetModule moduleReal = new DaggerNetModule(Mockito.mock(Activity.class));
//        NetworkModule module = Mockito.mock(NetworkModule.class);
        assertEquals(null, moduleReal.getNetworkInfo());
    }

    @Test
    public void getNetworkInfo_correct() {
        Activity activity = Mockito.mock(Activity.class);

        ConnectivityManager conManager = Mockito.mock(ConnectivityManager.class);

        Mockito.when(activity.getSystemService(Mockito.anyString()))
                .thenReturn(conManager);

        DaggerNetModule moduleReal = new DaggerNetModule(activity);
        //        NetworkModule module = Mockito.mock(NetworkModule.class);
        NetworkInfo info = moduleReal.getNetworkInfo();
        Mockito.verify(conManager).getActiveNetworkInfo();
//        assertEquals(null, moduleReal.getNetworkInfo());
    }
}