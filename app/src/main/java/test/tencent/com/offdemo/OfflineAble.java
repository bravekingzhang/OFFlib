package test.tencent.com.offdemo;

import rx.Observable;

/**
 * Created by hoollyzhang on 16/9/27.
 * Description : 一个具备offline能力的model接口
 */

public interface OfflineAble<T> {
    Observable<T> loadFromLocal();
    void save(T t);
}
