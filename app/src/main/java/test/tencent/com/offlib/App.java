package test.tencent.com.offlib;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by hoollyzhang on 16/9/27.
 * Description :
 */

public class App extends Application {
    private static RealmConfiguration mRealmConfig;
    private static Realm mRealm;
    @Override
    public void onCreate() {
        super.onCreate();
        mRealmConfig = new RealmConfiguration.Builder(getApplicationContext()).name("demo.realm").schemaVersion(1).build();
        Realm.setDefaultConfiguration(mRealmConfig);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    public static  Realm realmInstance(){
        if (mRealm==null){
            synchronized (Realm.class){
                if (mRealm==null){
                    mRealm = Realm.getInstance(mRealmConfig);
                    return mRealm;
                }else{
                    return mRealm;
                }
            }
        }else{
            return mRealm;
        }
    }

}
