package test.tencent.com.offdemo;

import java.util.List;

import test.tencent.com.offdemo.vo.Post;

/**
 * Created by hoollyzhang on 16/10/8.
 * Description :
 */

public interface PostContract {

    interface View extends BaseView<Presenter>{
        void showLoading();
        void stopLoading();
        void showPosts(List<Post> posts);
    }

    interface Presenter extends BasePresenter {
        void sendNewPost(String content);
        void loadPosts(boolean forceUpdate);
    }
}
