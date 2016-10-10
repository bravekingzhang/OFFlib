package test.tencent.com.offlineDemo.presenter;


import java.util.List;
import java.util.UUID;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import test.tencent.com.offlineDemo.jobmanger.PostJobManger;
import test.tencent.com.offlineDemo.model.PostModel;
import test.tencent.com.offlineDemo.presenter.PostContract;
import test.tencent.com.offlineDemo.vo.DaoSession;
import test.tencent.com.offlineDemo.vo.Post;

/**
 * Created by hoollyzhang on 16/10/10.
 * Description :
 */

public class PostPresenter implements PostContract.Presenter {

    private PostContract.View     view;
    private PostModel             postModel;
    private PostJobManger         mPostController;
    private CompositeSubscription mSubscription;

    public PostPresenter(DaoSession daoSession) {
        postModel = new PostModel(daoSession);
        mPostController = new PostJobManger();
        mSubscription = new CompositeSubscription();
    }

    public CompositeSubscription getmSubscription() {
        return mSubscription;
    }

    @Override
    public void attach(PostContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
        mSubscription.clear();
    }

    @Override
    public void loadPost(boolean forceUpdate) {
        if (!forceUpdate) {
            postModel.loadFromLocal().subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Post>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<Post> posts) {
                            if (view != null) {
                                view.showPosts(posts);
                            }
                        }
                    });
        } else {
            postModel.loadFromNetWork()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Post>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(List<Post> posts) {
                            //Toast.makeText(getContext(), "从网络上拉取了" + posts.size() + "条心情", Toast.LENGTH_LONG).show();
                            if (view!=null){
                                view.showToast("从网络上拉取了" + posts.size() + "条心情");
                                view.showPosts(posts);
                            }
                        }
                    });
        }
    }

    @Override
    public void newPost(String content) {
        mPostController.newPost(content, UUID.randomUUID().toString());
    }
}
