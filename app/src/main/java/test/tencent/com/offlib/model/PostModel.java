package test.tencent.com.offlib.model;

import java.util.List;

import test.tencent.com.offlib.App;
import test.tencent.com.offlib.vo.Post;

/**
 * Created by hoollyzhang on 16/9/27.
 * Description :
 */

public class PostModel extends BaseModel {

    @Override
    public List<Post> loadFromLocal() {
        List<Post> posts = App.realmInstance().where(Post.class).findAll().subList(0, 100);
        return posts;
    }

    @Override
    public void save(Object o) {
        if (o instanceof Post)
        App.realmInstance().insertOrUpdate((Post)o);
    }
}
