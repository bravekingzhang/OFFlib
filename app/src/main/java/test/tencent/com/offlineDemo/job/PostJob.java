package test.tencent.com.offlineDemo.job;

import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import test.tencent.com.offlineDemo.util.Injection;
import test.tencent.com.offlineDemo.RxBus;
import test.tencent.com.offlineDemo.model.PostModel;
import test.tencent.com.offlineDemo.rxevent.DeletePostEvent;
import test.tencent.com.offlineDemo.rxevent.NewPostEvent;
import test.tencent.com.offlineDemo.rxevent.UpdatePostEvent;
import test.tencent.com.offlineDemo.vo.Post;

/**
 * Created by hoollyzhang on 16/9/28.
 * Description :
 */

public class PostJob extends BaseJob {

    private static final String TAG   = "PostJob";
    private static final String GROUP = "POSTJOB";

    private static final int SERVICE_LATENCY_IN_MILLIS = 2000;//模拟网路延时

    private String content;//字段需要可序列化的
    private String localId;//这个字段同样需要时可序列化的


    public PostJob(@Priority int priority, String content, String localId) {
        super(new Params(priority).addTags(GROUP).requireNetwork().persist().groupBy(GROUP));
        this.content = content;
        this.localId = localId;
    }

    @Override
    public void onAdded() {
        Post post = generatePost(content, localId);
        RxBus.getRxBusSingleton().send(new NewPostEvent(post));
        PostModel postModel = new PostModel(Injection.daoSessionProvider(getApplicationContext()));
        postModel.save(post);
        Log.e(TAG, "onAdded() called with: thread name" + Thread.currentThread().getName() + "mPost address is " + post);
    }

    @Override
    public void onRun() throws Throwable {

        Log.e(TAG, "onRun() called with:  thread name" + Thread.currentThread().getName());
        // TODO: 16/9/28 发送到网络上,变更状态
        //这里简单的模拟一下,发送成功
        // step1: post the new post to network
        SystemClock.sleep(SERVICE_LATENCY_IN_MILLIS);
        // TODO: 16/10/10 这里省略向网络发送的代码
        int state = 200;
        // step2: update db
        if (state == 200) {
            PostModel postModel = new PostModel(Injection.daoSessionProvider(getApplicationContext()));
            Post post = postModel.getPostByLocalId(localId);
            if (post != null){
                post.setmPending(false);
                post.setCtime(System.currentTimeMillis() / 1000);//服务器返回的时间,id之类的更新
                postModel.save(post);
                RxBus.getRxBusSingleton().send(new UpdatePostEvent(post));
            }
        }

    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        PostModel postModel = new PostModel(Injection.daoSessionProvider(getApplicationContext()));
        Post post = postModel.getPostByLocalId(localId);
        if (post != null){
            RxBus.getRxBusSingleton().send(new DeletePostEvent(post));
        }
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        if (shouldRetry(throwable)) {
            return RetryConstraint.createExponentialBackoff(runCount, 1000);
        } else {
            return RetryConstraint.CANCEL;
        }

    }

    /**
     * demo,发送ugc
     *
     * @param content
     * @return
     */
    private Post generatePost(String content, String localId) {
        Post post = new Post();
        post.setMood(content);
        post.setmLocalUniqId(localId);
        post.setCtime(System.currentTimeMillis() / 1000);
        post.setmPending(true);
        return post;
    }
}
