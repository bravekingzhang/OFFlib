package test.tencent.com.offlib.model;


import org.greenrobot.greendao.rx.RxDao;

import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import test.tencent.com.offlib.util.MockUtils;
import test.tencent.com.offlib.vo.DaoSession;
import test.tencent.com.offlib.vo.Post;

/**
 * Created by hoollyzhang on 16/9/27.
 * Description :
 */

public class PostModel extends BaseModel {

    private DaoSession        daoSession;
    private RxDao<Post, Long> mPostDao;

    public PostModel(DaoSession daoSession) {
        this.daoSession = daoSession;
        this.mPostDao = this.daoSession.getPostDao().rx();
    }

    @Override
    public Observable<List<Post>> loadFromLocal() {
        return mPostDao.loadAll();
    }

    @Override
    public Observable<Post> save(Object o) {
        if (o instanceof Post) {
           /* RealmResults realmResults = App.realmInstance().where(Post.class).equalTo("mLocalUniqId",((Post) o).getmLocalUniqId()).findAll();
            if (realmResults!=null && realmResults.size()>0){
                realmResults.deleteFirstFromRealm();
            }
            App.realmInstance().insertOrUpdate((Post) o);
            App.realmInstance().commitTransaction();*/
            return mPostDao.save((Post) o);
        }
        return null;
    }
    public Observable<List<Post>> loadFromNetWork(){
        return Observable.create(new Observable.OnSubscribe<List<Post>>() {
            @Override
            public void call(Subscriber<? super List<Post>> subscriber) {
                // TODO: 16/9/28 这里是模拟网络拿数据,真实项目显然不是这回事
                List<Post> postlist = MockUtils.mockPosts(new Random().nextInt(5));
                subscriber.onNext(postlist);
                mPostDao.saveInTx(postlist);
                subscriber.onCompleted();
            }
        });
    }
}
