package test.tencent.com.offlib;

import java.util.List;

/**
 * Created by hoollyzhang on 16/9/27.
 * Description : 一个具备offline能力的model接口
 */

public interface OfflineAble<T> {
    List<T> loadFromLocal();
    void save(T t);
}
