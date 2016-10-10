package test.tencent.com.offlineDemo;

import android.os.Bundle;
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

import java.util.List;

import test.tencent.com.offlineDemo.rxevent.DeletePostEvent;
import test.tencent.com.offlineDemo.rxevent.NewPostEvent;
import test.tencent.com.offlineDemo.rxevent.UpdatePostEvent;
import test.tencent.com.offlineDemo.util.DividerItemDecoration;
import test.tencent.com.offlineDemo.vo.Post;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements PostContract.View {

    private static final String TAG = "MainActivityFragment";

    RecyclerView mRecyclerView;
    PostAdapter  mPostAdapter;

    EditText mContent;
    TextView btSender;

    FloatingActionButton btLoadNet;



    private PostPresenter mPresenter;

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
        mPresenter = new PostPresenter(Injection.daoSessionProvider(App.ApplicationContext()));
        mPresenter.attach(this);
        RxBus.getRxBusSingleton().subscribe(mPresenter.getmSubscription(), new RxBus.EventLisener() {
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
        return view;
    }

    @Override
    public void onDestroyView() {
        Log.e(TAG, "onDestroyView() called with: " + "");
        super.onDestroyView();
        mPresenter.detach();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                mPresenter.newPost(content);
                mContent.setText("");
            }
        });

        btLoadNet = (FloatingActionButton) view.findViewById(R.id.load_net_data);
        btLoadNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadPost(true);
            }
        });
        mPresenter.loadPost(false);
    }


    @Override
    public void showPosts(List<Post> posts) {
        if (mPostAdapter.getItemCount()==0){
            mPostAdapter.setPosts(posts);
        }else{
            mPostAdapter.addposts(posts);
        }
    }

    @Override
    public void showToast(String s) {
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
    }
}
