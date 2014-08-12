package ca.legacy.firebasedemo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by matthewlagace on 14-08-05.
 */
public class AppController extends Application {
    private static Firebase firebaseRef = new Firebase("https://vivid-fire-8562.firebaseio.com");
    private static AppController ourInstance = new AppController();
    private static String deviceId;
    private static final int buildSDK = Build.VERSION.SDK_INT;

    public static synchronized AppController getInstance() {
        return ourInstance;
    }
    public static Firebase getFirebaseRef() { return firebaseRef; }
    public static synchronized SharedPreferences getUserPrefs(Context cxt) { return cxt.getSharedPreferences("ca.legacy.firebasedemo.USER", Context.MODE_PRIVATE); }
    public static String getDeviceId() { return deviceId; }

    @Override
    public void onCreate() {
        super.onCreate();
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void disableIcon(Object cxt) {
        if (buildSDK < Build.VERSION_CODES.HONEYCOMB) {
            //disable application icon from ActionBar
            ((ActionBarActivity) cxt).getSupportActionBar().setDisplayShowHomeEnabled(false);
        } else {
            //disable application icon from ActionBar
            ((Activity) cxt).getActionBar().setDisplayShowHomeEnabled(false);
        }
    }
}
