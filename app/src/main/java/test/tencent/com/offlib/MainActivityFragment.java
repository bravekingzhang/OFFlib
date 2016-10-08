package test.tencent.com.offlib;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import test.tencent.com.offlib.controller.PostController;
import test.tencent.com.offlib.model.PostModel;
import test.tencent.com.offlib.rxevent.DeletePostEvent;
import test.tencent.com.offlib.rxevent.NewPostEvent;
import test.tencent.com.offlib.rxevent.UpdatePostEvent;
import test.tencent.com.offlib.util.DividerItemDecoration;
import test.tencent.com.offlib.vo.Post;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = "MainActivityFragment";

    RecyclerView mRecyclerView;
    PostAdapter  mPostAdapter;
    PostModel    postModel;

    EditText mContent;
    TextView btSender;

    FloatingActionButton btLoadNet;

    private PostController mPostController;

    private CompositeSubscription _compositeSubscription;

    public static MainActivityFragment newInstance() {

        Bundle args = new Bundle();

        MainActivityFragment fragment = new MainActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        _compositeSubscription = new CompositeSubscription();
        RxBus.getRxBusSingleton().subscribe(_compositeSubscription, new RxBus.EventLisener() {
            @Override
            public void dealRxEvent(Object event) {
                if (event instanceof NewPostEvent) {
                    mPostAdapter.addPost(((NewPostEvent) event).getPost());
                } else if (event instanceof UpdatePostEvent) {
                    mPostAdapter.updatePost(((UpdatePostEvent) event).getPost());
                } else if (event instanceof DeletePostEvent) {
                    mPostAdapter.deletePost(((DeletePostEvent) event).getPost());
                }
            }
        });
        mPostController = new PostController();
        return view;
    }

    @Override
    public void onDestroyView() {
        Log.e(TAG, "onDestroyView() called with: " + "");
        super.onDestroyView();
        if (!_compositeSubscription.isUnsubscribed()) {
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
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        itemDecoration.setmDivider(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        mRecyclerView.addItemDecoration(itemDecoration);
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

        btLoadNet = (FloatingActionButton) view.findViewById(R.id.load_net_data);
        btLoadNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPostFromNet();
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

    /**
     * 从网络拉取新的数据
     */
    private void getPostFromNet() {
        // TODO: 16/9/28 这里简单的模拟从网络获取xin的数据
        postModel.loadFromNetWork()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Post>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Toast.makeText(getContext(),"从网络上拉取了"+posts.size()+"条心情",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(List<Post> posts) {
                        Toast.makeText(getContext(), "从网络上拉取了" + posts.size() + "条心情", Toast.LENGTH_LONG).show();
                        mPostAdapter.addposts(posts);
                    }
                });
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
