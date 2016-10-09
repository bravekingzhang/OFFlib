package test.tencent.com.offlib;

import java.util.List;

import rx.Observable;
import test.tencent.com.offlib.vo.Post;

/**
 * Created by hoollyzhang on 16/9/27.
 * Description : 一个具备offline能力的model接口
 */

public interface OfflineAble<T> {
    Observable<T> loadFromLocal();
    Observable<Post> save(T t);
}
