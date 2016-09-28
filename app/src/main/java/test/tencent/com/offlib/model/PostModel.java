package test.tencent.com.offlib.model;


import io.realm.RealmResults;
import rx.Observable;
import test.tencent.com.offlib.App;
import test.tencent.com.offlib.vo.Post;

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
            App.realmInstance().insertOrUpdate((Post) o);
            App.realmInstance().commitTransaction();
        }
    }
}
