package test.tencent.com.offdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import test.tencent.com.offdemo.rxevent.DeletePostEvent;
import test.tencent.com.offdemo.rxevent.NewPostEvent;
import test.tencent.com.offdemo.rxevent.UpdatePostEvent;
import test.tencent.com.offdemo.util.DividerItemDecoration;
import test.tencent.com.offdemo.vo.Post;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements PostContract.View {

    RecyclerView mRecyclerView;
    PostAdapter  mPostAdapter;
    EditText     mContent;
    TextView     btSender;

    FloatingActionButton btLoadNet;

    private PostContract.Presenter mPresenter;

    public MainActivityFragment() {

    }

    @Override
    public void setPresenter(@NonNull PostContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        new PostPresenter(this);
        RxBus.getRxBusSingleton().subscribe(mPresenter.getSubscripton(), new RxBus.EventLisener() {
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
        initView(view);
        mPresenter.subscribe();
        return view;
    }

    private void initView(View view) {
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
                mPresenter.sendNewPost(content);
                mContent.setText("");
            }
        });

        btLoadNet = (FloatingActionButton) view.findViewById(R.id.load_net_data);
        btLoadNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadPosts(true);//强制刷新
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscribe();
    }

    @Override
    public void showLoading() {
        Toast.makeText(getContext(),"从网络上拉取了数据",Toast.LENGTH_LONG).show();
    }

    @Override
    public void stopLoading() {
        Toast.makeText(getContext(),"拉取结束",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPosts(List<Post> posts) {
        if (mPostAdapter.getItemCount() == 0) {
            mPostAdapter.setPosts(posts);
        }else{
            mPostAdapter.addposts(posts);
        }
    }
}
