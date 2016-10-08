package test.tencent.com.offdemo.rxevent;

import test.tencent.com.offdemo.vo.Post;

/**
 * Created by hoollyzhang on 16/9/28.
 * Description :
 */
public class NewPostEvent {
    private Post post;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public NewPostEvent(Post post) {
        this.post = post;
    }
}
