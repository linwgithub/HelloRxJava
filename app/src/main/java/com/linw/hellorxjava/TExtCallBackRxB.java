package com.linw.hellorxjava;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import rx.Observable;
import rx.Subscriber;

/**
 *
 */
public class TExtCallBackRxB {

    public interface MyCallBack{
        void done(String str);
    }

    public static void userCallBack(String str,MyCallBack callBack) {
        callBack.done(str);
    }

    public static Subscriber userCallBackSubscribe(final String s, final Subscriber<String> subscriber) {
        userCallBack(s,new MyCallBack() {
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
                userCallBack(str,new MyCallBack() {
                    @Override
                    public void done(String strs) {
                        subscriber.onNext(str);
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                subscriber.onCompleted();
                                //execute the task
                            }
                        }, 1000);
                    }
                });

            }
        });
    }

//    public static Observable<String> userRxCallBackObservable(final String str) {
//
//        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext(str);
//            }
//        });
//
////        observable.subscribe(userCallBackRx(str, new Subscriber<String>() {
////            @Override
////            public void onCompleted() {
////                Log.e("onnext", "get onCompleted");
////            }
////
////            @Override
////            public void onError(Throwable e) {
////
////            }
////
////            @Override
////            public void onNext(String s) {
////                Log.e("onnext", s);
////            }
////        }));
//
//        return observable;
////        Observable.just("one","two","there").subscribe(
////                new TextCallBackRx().userCallBackRx(new Subscriber<String>() {
////                    @Override
////                    public void onCompleted() {
////                        Log.e("onnext", "get onCompleted");
////                    }
////
////                    @Override
////                    public void onError(Throwable e) {
////
////                    }
////
////                    @Override
////                    public void onNext(String s) {
////                        Log.e("onnext", "get Next");
////                    }
////                }));
//
//
//    }
}
