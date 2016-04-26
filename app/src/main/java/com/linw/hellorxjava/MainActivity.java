package com.linw.hellorxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = "MainActivity";

    String[] fromStr = {"str1", "str2", "str3", "str4", "str5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void btnclick(View view) {
//        threadLearn();
        callbackLearn();
    }

    //1、创建被观察者
    Observable<String> myObservableNormal = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            for (int i = 0; i < 4; i++) {
                subscriber.onNext(i + "");
            }
            subscriber.onCompleted();
        }
    });

    //2、创建观察者
    Subscriber<String> mySubscriberNormal = new Subscriber<String>() {
        @Override
        public void onCompleted() {
            Log.e("TAG", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("TAG", "onError");
        }

        @Override
        public void onNext(String s) {
            Log.e("TAG", "onNext");
            Log.e("TAG", s);
        }
    };

    //拆分的观察者——出错动作
    Action1<Throwable> onErrorAction = new Action1<Throwable>() {

        @Override
        public void call(Throwable throwable) {

        }
    };

    //拆分的观察者——结束动作
    Action0 onCompletedAction = new Action0() {
        @Override
        public void call() {

        }
    };

    //拆分的观察者——下一个动作
    Action1<String> onNextAction = new Action1<String>() {
        @Override
        public void call(String s) {
            Log.e("TAG", "onNextAction");
            Log.e("TAG", s);
        }
    };

    /**
     * 1普通模式(为被观察者添加观察者)
     */
    private void normalRxSet() {


        //3、为被观察者添加观察者
        myObservableNormal.subscribe(mySubscriberNormal);
    }

    /**
     * 2、简化发送1:送一系列动作
     */
    private void simpleOneNext() {
        //1使用just
        Observable.just("simple_just_1", "simple_just_2", "simple_just_3").subscribe(mySubscriberNormal);
        //2使用from
        Observable.from(fromStr).subscribe(mySubscriberNormal);
    }

    /**
     * 3简化(创建被监听者对象,只关注过程)
     */
    private void simpleOnlyNext() {
        myObservableNormal.subscribe(onNextAction);
    }

    /**
     * 4、拆分的观察者
     */
    private void allSubscriber() {
        myObservableNormal.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }

    /**
     * 5使用Map变换符
     */
    private void useMap() {

        //1变换(String -> String)
        myObservableNormal.map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s + "-lw";
            }
        }).subscribe(mySubscriberNormal);

        //2多层转换(String->Boolean -> String)
        myObservableNormal.map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                if (s.length() > 5) {
                    return false;
                } else {
                    return true;
                }
            }
        }).map(new Func1<Boolean, String>() {
            @Override
            public String call(Boolean aBoolean) {
                return aBoolean ? "长度大于5" : "长度小于5";
            }
        }).subscribe(mySubscriberNormal);
    }

    /**
     * 5使用flatMap变换符
     * flatMap与map的区别:
     * map变换的被观察者和观察者是一对一的关系
     * flatMap变换的被观察着和观察者是一对多的关系
     * 实现原理:flatMap通过变换可以在中间返回多个被观察者,而这个被观察者可以注册之前注册的观察者
     */
    private void useFlatMap() {

        myObservableNormal.flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                ArrayList<String> strs = new ArrayList<>();
                for (char a : s.toCharArray()) {
                    strs.add(String.valueOf(a));
                }
                return Observable.from(strs);
            }
        }).subscribe(mySubscriberNormal);
    }


    /**
     * 6\线程练习
     */
    private void threadLearn() {

        Observable.just("str1", "str2")
                .subscribeOn(Schedulers.newThread())//开启器新线程
                .observeOn(AndroidSchedulers.mainThread())//回到主线程线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {

                    }
                });
    }

    /**
     * 使用RxJava优化回调
     */
    private void callbackLearn() {

//        CallBackByRX.newUserCallBackSubscribe("begin")
////                 .map(new Func1<String, String>() {
////                    @Override
////                    public String call(String s) {
////                        return "map Str";
////                    }
////                })
//                .subscribe(CallBackByRX.userCallBackSubscribe("", new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.e(LOG_TAG, "OK");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        Log.e(LOG_TAG, s);
//                    }
//                }));

        CallBackByRX.newUserCallBackSubscribeList(fromStr)
                .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e(LOG_TAG, s);
            }
        });

    }
}
