package test.tencent.com.offlib;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import test.tencent.com.offlib.util.MyRealmMigration;

/**
 * Created by hoollyzhang on 16/9/27.
 * Description :
 */

public class App extends Application {
    private static Context mApplicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = getApplicationContext();
    }

    public static Context ApplicationContext(){
        return mApplicationContext;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


}
