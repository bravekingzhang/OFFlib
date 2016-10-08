package test.tencent.com.offdemo.model;


import java.util.List;
import java.util.Random;

import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import test.tencent.com.offdemo.App;
import test.tencent.com.offdemo.util.MockUtils;
import test.tencent.com.offdemo.vo.Post;

/**
 * Created by hoollyzhang on 16/9/27.
 * Description :
 */

public class PostModel extends BaseModel {

    @Override
    public Observable<RealmResults<Post>> loadFromLocal() {
        return App.realmInstance().where(Post.class).findAll().asObservable();
    }

    @Override
    public void save(Object o) {
        if (o instanceof Post) {
            App.realmInstance().beginTransaction();
            RealmResults realmResults = App.realmInstance().where(Post.class).equalTo("mLocalUniqId",((Post) o).getmLocalUniqId()).findAll();
            if (realmResults!=null && realmResults.size()>0){
                realmResults.deleteFirstFromRealm();
            }
            App.realmInstance().insertOrUpdate((Post) o);
            App.realmInstance().commitTransaction();
        }
    }

    public Observable<List<Post>> loadFromNetWork(){
        return Observable.create(new Observable.OnSubscribe<List<Post>>() {
            @Override
            public void call(Subscriber<? super List<Post>> subscriber) {
                // TODO: 16/9/28 这里是模拟网络拿数据,真实项目显然不是这回事
                List<Post> postlist = MockUtils.mockPosts(new Random().nextInt(5));
                subscriber.onNext(postlist);
                App.realmInstance().beginTransaction();
                for (int i=0;i<postlist.size();i++){
                    App.realmInstance().insertOrUpdate(postlist.get(i));
                }
                App.realmInstance().commitTransaction();
                subscriber.onCompleted();
            }
        });
    }
}
