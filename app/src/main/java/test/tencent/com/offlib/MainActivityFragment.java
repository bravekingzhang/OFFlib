package test.tencent.com.offlib;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;
import test.tencent.com.offlib.controller.PostController;
import test.tencent.com.offlib.model.PostModel;
import test.tencent.com.offlib.rxevent.DeletePostEvent;
import test.tencent.com.offlib.rxevent.NewPostEvent;
import test.tencent.com.offlib.rxevent.UpdatePostEvent;
import test.tencent.com.offlib.vo.Post;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    RecyclerView mRecyclerView;
    PostAdapter  mPostAdapter;
    PostModel    postModel;

    EditText mContent;
    TextView btSender;

    private  PostController mPostController;

    private CompositeSubscription _compositeSubscription = new CompositeSubscription();

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        RxBus.getRxBusSingleton().subscribe(_compositeSubscription, new RxBus.EventLisener() {
            @Override
            public void dealRxEvent(Object event) {
                if (event instanceof NewPostEvent){
                    mPostAdapter.addPost(((NewPostEvent) event).getPost());
                }else if (event instanceof UpdatePostEvent){
                    mPostAdapter.updatePost(((UpdatePostEvent) event).getPost());
                }else if (event instanceof DeletePostEvent){
                    mPostAdapter.deletePost(((DeletePostEvent) event).getPost());
                }
            }
        });
        mPostController= new PostController();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!_compositeSubscription.isUnsubscribed()){
            _compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postModel = new PostModel();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mPostAdapter = new PostAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mPostAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btSender = (TextView) view.findViewById(R.id.bt_send);
        mContent = (EditText) view.findViewById(R.id.et_msg);
        btSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/9/27 validation
                String content = mContent.getText().toString().trim();
                // TODO: 16/9/28 参数过多可以考虑bulid模式
                mPostController.newPost(content);
                mContent.setText("");
            }
        });

        initPosts();

    }

    /**
     * 从本地缓存初始化列表数据
     */
    private void initPosts() {
        postModel.loadFromLocal().subscribeOn(AndroidSchedulers.mainThread())
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
                mPostAdapter.setPosts(posts);
            }
        });
    }

    @NonNull
    private List<Post> realmResult2list(RealmResults<Post> posts) {
        List<Post> postlist = new ArrayList<>();
        for (int i=0;i<posts.size();i++){
            postlist.add(posts.get(i));
        }
        return postlist;
    }

}
