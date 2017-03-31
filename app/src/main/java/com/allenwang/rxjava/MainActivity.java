package com.allenwang.rxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.subscribers.ResourceSubscriber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // observer pre-work, the <String> can translate to <whatever> you want
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        /** RxJava 1 **/
        /* Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        }); */

        /** RxJava 2, o1 is doing the same stuff as o2 and o3 here**/
        Observable<String> o1 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("o1 next1");
                e.onNext("o1 next2");
                e.onComplete();
            }
        });

        Observable<String> o2 = Observable.just("o2 next1", "o2 next2"); // which is include onComplete()

        String[] words = {"o3 next1", "o3 next2"};
        Observable o3 = Observable.fromArray(words);

        // do the stuff!
        o1.subscribe(observer);
        o2.subscribe(observer);
        o3.subscribe(observer);


        // subscriber example1, general speaking, it do the same work as observer here
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };

        /** RxJava 1 **/
        //o1.subscribe(subscriber);
        /** RxJava 2 **/
        Flowable.just("subscriber next1").subscribe(subscriber); // onError may not work at RxJava2
        Flowable.just("subscriber next2").safeSubscribe(subscriber);

        // subscriber  example1
        ResourceSubscriber<Integer> integerResourceSubscriber = new ResourceSubscriber<Integer>() {
            @Override
            public void onStart() {
                request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Integer t) {
                System.out.println(t);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done");
            }
        };

        CompositeDisposable composite1 = new CompositeDisposable();
        composite1.add(Flowable.range(0, 52).subscribeWith(integerResourceSubscriber));

        // subscriber example2
        ResourceSubscriber<String> stringResourceSubscriber = new ResourceSubscriber<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("Done");
            }
        };

        CompositeDisposable composite2 = new CompositeDisposable();
        composite2.add(Flowable.just("subscriber composite2 next1").subscribeWith(stringResourceSubscriber));


    }
}
