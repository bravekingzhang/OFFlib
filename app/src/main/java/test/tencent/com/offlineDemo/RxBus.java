package test.tencent.com.offlineDemo;

import android.support.annotation.NonNull;
import android.util.Log;

import rx.functions.Action1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hoollyzhang on 16/5/25.
 * Description :
 */
public final class RxBus {

    private static RxBus sRxBus;
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());


    /**
     * 消息发送者调用
     *
     * @param o
     */
    public void send(Object o) {
        if (_bus.hasObservers()) {
            _bus.onNext(o);
        }
    }

    /**
     * 拿到事件总线
     *
     * @return
     */
    public static RxBus getRxBusSingleton() {
        if (sRxBus == null) {
            sRxBus = new RxBus();
        }

        return sRxBus;
    }

    /**
     * 订阅
     *
     * @param subscription new CompositeSubscription()
     * @param eventLisener
     */
    public void subscribe(@NonNull CompositeSubscription subscription, @NonNull final EventLisener eventLisener) {
        subscription.add(_bus
                .asObservable()
                ///.debounce(100, TimeUnit.MILLISECONDS)//事件去重，会切换线程，每个卵用，去掉
                //.distinct()
                //.distinctUntilChanged()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {
                        Log.e("Rxbus", "dealRxEvent called with: " + "event = [" + event + "]");
                        eventLisener.dealRxEvent(event);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("Rxbus", "call: "+throwable);
                    }
                }));
    }

    public interface EventLisener {
        /**
         * 处理总线事件
         *
         * @param event
         */
        void dealRxEvent(Object event);
    }

}
