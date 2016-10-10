package test.tencent.com.offlineDemo.job;

import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;


import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.util.UUID;

import test.tencent.com.offlineDemo.Injection;
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

    private static final String TAG = "PostJob";
    private static final String GROUP = "POSTJOB";

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;//模拟网路延时

    private Post mPost;//这个对象需要是可序列化的。


    public PostJob(@Priority int priority, String content) {
        super(new Params(priority).addTags(GROUP).requireNetwork().persist().groupBy(GROUP));
        this.mPost = generatePost(content);
    }

    @Override
    public void onAdded() {
        PostModel postModel = new PostModel(Injection.daoSessionProvider(getApplicationContext()));
        postModel.save(mPost);
        RxBus.getRxBusSingleton().send(new NewPostEvent(mPost));

        // onAdded() ------ >  onRun();
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, "onRun() called with: " + "");
        // TODO: 16/9/28 发送到网络上,变更状态
        //这里简单的模拟一下,发送成功
        SystemClock.sleep(SERVICE_LATENCY_IN_MILLIS);
        mPost.setmPending(false); //发送成功
        mPost.setCtime(System.currentTimeMillis() / 1000);//服务器返回的时间,id之类的更新
        PostModel postModel = new PostModel(Injection.daoSessionProvider(getApplicationContext()));
        postModel.save(mPost);
        RxBus.getRxBusSingleton().send(new UpdatePostEvent(mPost));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        RxBus.getRxBusSingleton().send(new DeletePostEvent(mPost));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        if (shouldRetry(throwable)){
            return RetryConstraint.createExponentialBackoff(runCount, 1000);
        }else{
            return RetryConstraint.CANCEL;
        }

    }

    /**
     * demo,发送ugc
     *
     * @param content
     * @return
     */
    private Post generatePost(String content) {
        Post post = new Post();
        post.setMood(content);
        post.setmLocalUniqId(UUID.randomUUID().toString());
        post.setCtime(System.currentTimeMillis() / 1000);
        post.setmPending(true);
        return post;
    }
}
