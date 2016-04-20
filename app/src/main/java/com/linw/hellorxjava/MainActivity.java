package com.linw.hellorxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void btnclick(View view) {
        //1普通
//        myObservableNormal.subscribe(mySubscriberNormal);
        //2简化(只发送一个)
//        myObsercableJust.subscribe(mySubscriberNormal);
        //3简化(创建被监听者对象,只关注过程)
//        myObsercableJust.subscribe(onNextAction);
        //4简化(关注所有)
//        myObservableNormal.subscribe(onNextAction, onErrorAction, onCompletedAction);
        //5简化(无需创建被监听者,只关注过程)
//        Observable.just("SimpleJust").subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                Log.e("TAG", s);
//            }
//        });

        //操作符
        //1变换
//        Observable.just("Hello")
//                .map(new Func1<String, String>() {
//                    @Override
//                    public String call(String s) {
//                        return s + "-lw";
//                    }
//                }).subscribe(onNextAction);
        //2多层转换
        Observable.just("Hello").map(new Func1<String, Boolean>() {
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
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("TAG", s);
            }
        });
    }

    Observable<String> myObservableNormal = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            subscriber.onNext("AA");
            subscriber.onNext("BB");
            subscriber.onCompleted();
        }
    });

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

    Observable<String> myObsercableJust = Observable.just("just");

    Action1<Throwable> onErrorAction = new Action1<Throwable>() {

        @Override
        public void call(Throwable throwable) {

        }
    };

    Action0 onCompletedAction = new Action0() {
        @Override
        public void call() {

        }
    };

    Action1<String> onNextAction = new Action1<String>() {
        @Override
        public void call(String s) {
            Log.e("TAG", "onNextAction");
            Log.e("TAG", s);
        }
    };

}
