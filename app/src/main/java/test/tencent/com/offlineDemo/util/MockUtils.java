package test.tencent.com.offlineDemo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import test.tencent.com.offlineDemo.vo.Post;

/**
 * Created by hoollyzhang on 16/9/28.
 * Description :
 */

public class MockUtils {


    public static List<Post> mockPosts(int num){
        List<Post> postList = new ArrayList<>();
        for (int i=0;i<num;i++){
            Post post = new Post();
            post.setCtime(System.currentTimeMillis()/1000);
            post.setmPending(false);//从网络拿到的显然不是pending状态了
            post.setMood(strings[new Random().nextInt(strings.length)]);
            //没有UUID。
            postList.add(post);
        }
        return postList;
    }


    public static String[] strings= {"以加上这个就行了是吗.subscribeOn(Schedulers.io())","是个反面例子，你写了一个也改变不了泄漏","提供同步操作，改不了","没有这么觉得，对于这个结论，","没有什么卵用，和上述情况一致","跑完了，在GC一下，在导出hpro"};
}
