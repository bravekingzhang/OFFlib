package test.tencent.com.offlib.job;

import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;


import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.util.UUID;

import test.tencent.com.offlib.RxBus;
import test.tencent.com.offlib.model.PostModel;
import test.tencent.com.offlib.rxevent.DeletePostEvent;
import test.tencent.com.offlib.rxevent.NewPostEvent;
import test.tencent.com.offlib.rxevent.UpdatePostEvent;
import test.tencent.com.offlib.vo.Post;

/**
 * Created by hoollyzhang on 16/9/28.
 * Description :
 */

public class PostJob extends BaseJob {

    private static final String TAG = "PostJob";
    private static final String GROUP = "POSTJOB";

    private Post post;//这个对象需要是可序列化的。


    public PostJob(@Priority int priority, String content) {
        super(new Params(priority).addTags(GROUP).requireNetwork().persist().groupBy(GROUP));
        this.post = generatePost(content);
    }

    @Override
    public void onAdded() {
        PostModel postModel = new PostModel();
        postModel.save(post);
        RxBus.getRxBusSingleton().send(new NewPostEvent(post));
        Log.e(TAG, "onAdded() called with: " + "");
    }

    @Override
    public void onRun() throws Throwable {
        Log.e(TAG, "onRun() called with: " + "");
        // TODO: 16/9/28 发送到网络上,变更状态
        SystemClock.sleep(5000);
        //这里简单的模拟一下,发送成功
        post.setmPending(false); //发送成功
        post.setCtime(System.currentTimeMillis() / 1000);//服务器返回的时间,id之类的更新
        RxBus.getRxBusSingleton().send(new UpdatePostEvent(post));
        PostModel postModel = new PostModel();
        postModel.save(post);
        //这里的更新的关键字暂时使用 uuid来,当然真是项目中不可能如此简单
    }


    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        RxBus.getRxBusSingleton().send(new DeletePostEvent(post));
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
