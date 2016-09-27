package test.tencent.com.offlib;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

import test.tencent.com.offlib.model.PostModel;
import test.tencent.com.offlib.vo.Post;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    RecyclerView mRecyclerView;
    PostAdapter mPostAdapter;
    PostModel postModel;

    EditText mContent;
    TextView btSender;

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postModel = new PostModel();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mPostAdapter = new PostAdapter();
        mRecyclerView.setAdapter(mPostAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btSender = (TextView) view.findViewById(R.id.bt_send);
        mContent = (EditText) view.findViewById(R.id.et_msg);
        btSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 16/9/27 validation
                String content = mContent.getText().toString().trim();
                Post post = generatePost(content);
                mPostAdapter.addPost(post);
                postModel.save(post);
                mContent.setText("");
            }
        });
    }

    private Post generatePost(String content) {
        Post post = new Post();
        post.setMood(content);
        post.setmLocalUniqId(UUID.randomUUID().toString());
        post.setmPending(true);
        return post;
    }

}
