package test.tencent.com.offlib;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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

    private List<Post> posts;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            if (holder.itemView instanceof TextView){
                if (posts.get(position).ismPending()){
                    ((TextView) holder.itemView).setTextColor(Color.RED);
                }else{
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

    public void addPost(Post post) {
        if (posts == null){
            posts = new ArrayList<>();
        }
        posts.add(post);
        notifyItemInserted(posts.size());
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
