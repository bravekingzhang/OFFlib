package test.tencent.com.offlineDemo.model;


import android.support.annotation.Nullable;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.WhereCondition;
import org.greenrobot.greendao.rx.RxDao;

import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Subscriber;
import test.tencent.com.offlineDemo.util.MockUtils;
import test.tencent.com.offlineDemo.vo.DaoSession;
import test.tencent.com.offlineDemo.vo.Post;
import test.tencent.com.offlineDemo.vo.PostDao;

/**
 * Created by hoollyzhang on 16/9/27.
 * Description :
 */

public class PostModel extends BaseModel {

    private DaoSession        daoSession;
    private RxDao<Post, Long> mPostRxDao;//
    private PostDao mPostDao;

    public PostModel(DaoSession daoSession) {
        this.daoSession = daoSession;
        this.mPostRxDao = this.daoSession.getPostDao().rx();
        this.mPostDao = this.daoSession.getPostDao();
    }

    @Override
    public Observable<List<Post>> loadFromLocal() {
        return mPostRxDao.loadAll();
    }

    @Override
    public void save(Object o) {
        mPostDao.save((Post) o);
    }

    @Nullable
    public Post getPostByLocalId(String localid){
        List<Post> posts  = mPostDao.queryBuilder().where(new WhereCondition.PropertyCondition(PostDao.Properties.MLocalUniqId, "=?",localid)).build().list();
        if (posts!=null && posts.size()>0){
            return posts.get(0);
        }else{
            return null;
        }
    }


    public Observable<List<Post>> loadFromNetWork() {
        return Observable.create(new Observable.OnSubscribe<List<Post>>() {
            @Override
            public void call(Subscriber<? super List<Post>> subscriber) {
                // TODO: 16/9/28 这里是模拟网络拿数据,真实项目显然不是这回事,你应该调用你的 RESTFUL API 获取
                final List<Post> postlist = MockUtils.mockPosts(new Random().nextInt(5));
                //this may occur some error,you should
                try {
                    mPostDao.saveInTx(postlist);
                }catch ( Exception e){
                    subscriber.onError(e);
                }
                subscriber.onNext(postlist);
                subscriber.onCompleted();
            }
        });
    }
}
