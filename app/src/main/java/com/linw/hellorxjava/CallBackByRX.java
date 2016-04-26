package com.linw.hellorxjava;

import android.os.Handler;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 *
 */
public class CallBackByRX {

    public interface MyCallBack {
        void done(String str);
    }

    public static void userCallBack(String str, MyCallBack callBack) {
        callBack.done(str);
    }

    public static Subscriber userCallBackSubscribe(final String s, final Subscriber<String> subscriber) {
        userCallBack(s, new MyCallBack() {
            @Override
            public void done(String str) {
                subscriber.onNext(s);
            }
        });

        return subscriber;
    }

    public static Observable<String> newUserCallBackSubscribe(final String str) {

        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                userCallBack(str, new MyCallBack() {
                    @Override
                    public void done(String strs) {
                        subscriber.onNext(str);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                subscriber.onCompleted();
                            }
                        }, 1000);
                    }
                });

            }
        });
    }

    /**
     * 一对多
     */
    public static Observable<String> newUserCallBackSubscribeList(final String[] strs) {

        return Observable.from(strs)
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(final String s) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(final Subscriber<? super String> subscriber) {

                                userCallBack(s, new MyCallBack() {
                                    @Override
                                    public void done(String strs) {
                                        subscriber.onNext(strs);
                                    }
                                });
                            }
                        });
                    }
                });
    }
}
