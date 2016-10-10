package test.tencent.com.offlineDemo;

import java.util.List;

import test.tencent.com.offlineDemo.vo.Post;

/**
 * Created by hoollyzhang on 16/10/10.
 * Description :
 */

public interface PostContract {

    interface View extends BaseView{
        void showPosts(List<Post> posts);

        void showToast(String s);
    }

    interface Presenter extends BasePresenter<View> {
        void loadPost(boolean forceUpdate);

        void newPost(String content);
    }

}
