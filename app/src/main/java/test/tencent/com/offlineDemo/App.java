package test.tencent.com.offlineDemo;

import android.app.Application;
import android.content.Context;

import org.greenrobot.greendao.database.Database;

import test.tencent.com.offlineDemo.vo.DaoMaster;
import test.tencent.com.offlineDemo.vo.DaoSession;


/**
 * Created by hoollyzhang on 16/9/27.
 * Description :
 */

public class App extends Application {
    private static Context mApplicationContext;

    public static final boolean ENCRYPTED = false;

    private DaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = getApplicationContext();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,ENCRYPTED ? "notes-db-encrypted" : "test");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static Context ApplicationContext(){
        return mApplicationContext;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


}
