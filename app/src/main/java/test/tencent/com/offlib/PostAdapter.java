package test.tencent.com.offlib;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.tencent.com.offlib.vo.Post;

/**
 * Created by hoollyzhang on 16/9/27.
 * Description :
 */

public class PostAdapter extends RecyclerView.Adapter {

    private List<Post>   posts;
    private RecyclerView mRecyclerView;//加这个主要是为了滑动;

    public PostAdapter(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            if (holder.itemView instanceof TextView) {
                if (posts.get(position).ismPending()) {
                    ((TextView) holder.itemView).setTextColor(Color.RED);
                } else {
                    ((TextView) holder.itemView).setTextColor(Color.GREEN);
                }
                ((TextView) holder.itemView).setText(posts.get(position).getMood());
            }
        }
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    public void addPost(final Post post) {
        if (posts == null) {
            posts = new ArrayList<>();
        }
        posts.add(post);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(posts.size());
                //mRecyclerView.setLayoutFrozen(false);
                mRecyclerView.smoothScrollToPosition(posts.size());
            }
        });
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });

    }

    public void updatePost(Post post) {
        if (posts == null || posts.size() == 0) {
            return;
        }
        for (int i = 0; i < posts.size(); i++) {
            if (TextUtils.equals(posts.get(i).getmLocalUniqId(), post.getmLocalUniqId())) {
                posts.set(i, post);//简单的更新
                final int finalI = i;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(finalI);
                        mRecyclerView.setLayoutFrozen(false);
                    }
                });

                break;
            }
        }
    }

    public void deletePost(Post post) {
        if (posts == null || posts.size() == 0) {
            return;
        }
        for (int i = 0; i < posts.size(); i++) {
            if (TextUtils.equals(posts.get(i).getmLocalUniqId(), post.getmLocalUniqId())) {
                posts.remove(i);
                final int finalI = i;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemRemoved(finalI);
                    }
                });
                break;
            }
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
