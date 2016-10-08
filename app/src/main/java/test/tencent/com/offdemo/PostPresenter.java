package test.tencent.com.offdemo;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import test.tencent.com.offdemo.controller.PostController;
import test.tencent.com.offdemo.model.PostModel;
import test.tencent.com.offdemo.vo.Post;

/**
 * Created by hoollyzhang on 16/10/8.
 * Description :
 */

public class PostPresenter implements PostContract.Presenter {

    private PostController        mPostController;
    private CompositeSubscription mSubscription;
    private PostModel             postModel;
    private PostContract.View     mView;

    public PostPresenter(PostContract.View view) {
        this.mPostController = new PostController();
        this.mSubscription = new CompositeSubscription();
        this.postModel = new PostModel();
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    @NonNull
    public CompositeSubscription getSubscripton() {
        return this.mSubscription;
    }

    @Override
    public void sendNewPost(String content) {
        mPostController.newPost(content);
    }

    @Override
    public void loadPosts(boolean forceUpdate) {
        /**
         * 从本地缓存初始化列表数据
         */
        if (!forceUpdate) {
            mSubscription.add(postModel.loadFromLocal()
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .map(new Func1<RealmResults<Post>, List<Post>>() {
                        @Override
                        public List<Post> call(RealmResults<Post> posts) {
                            return realmResult2list(posts);
                        }
                    }).subscribe(new Subscriber<List<Post>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<Post> posts) {
                            mView.showPosts(posts);
                        }
                    }));
        } else {
            /**
             * 从网络加载新的数据
             */
            postModel.loadFromNetWork()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            mView.showLoading();
                        }
                    })
                    .subscribe(new Subscriber<List<Post>>() {
                        @Override
                        public void onCompleted() {
                            mView.stopLoading();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.stopLoading();
                        }

                        @Override
                        public void onNext(List<Post> posts) {
                            mView.showPosts(posts);
                        }
                    });
        }

    }

    @Override
    public void subscribe() {
        loadPosts(false);
    }

    @Override
    public void unsubscribe() {
        mSubscription.clear();
    }


    @NonNull
    private List<Post> realmResult2list(RealmResults<Post> posts) {
        List<Post> postlist = new ArrayList<>();
        for (int i = 0; i < posts.size(); i++) {
            postlist.add(posts.get(i));
        }
        return postlist;
    }
}
