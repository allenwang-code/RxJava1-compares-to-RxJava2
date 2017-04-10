# RxJava1-compares-to-RxJava2
#### Follow the guide here, http://gank.io/post/560e15be2dca930e00da1083
#### Reference, https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0

| description | RxJava 1.X | RxJava 2.X |
| ----- | ----- | ----- |
|`package`| `rx.xxx` | `io.reactivex.xxx` |
| [Reactive Streams rules](http://www.reactive-streams.org/) | `1.X` is before `Reactive Streams`, so it only support apart  | totally support |
|[Backpressure](https://github.com/ReactiveX/RxJava/wiki/Backpressure)|partly support|`Oberservable` not support<br> add `Flowable` to support backpressure|
|`null`| support | do not support `null`, if send `null` to it, will cause `NullPointerException` |
|`Schedulers`| `Schedulers.immediate()`<br>`Schedulers.trampoline()`<br>`Schedulers.computation()`<br>`Schedulers.newThread()`<br>`Schedulers.io()`<br>`Schedulers.from(executor)`<br>`AndroidSchedulers.mainThread()` | remove`Schedulers.immediate()`<br>add`Schedulers.single()`<br> |
|`Single`| The action is like`Observable`, but only launch a`onSuccess`or`onError` | follow the rule of`Reactive Streams`, `onSubscribe(onSuccess/onError)` |
|`Completable`| The action is like`Observable`, it fail then all fail, sucssess on the contrary | follow the rule of`Reactive Streams`,`onSubscribe (onComplete/onError)` |
|`Maybe`| none | added in`2.X`, like`Observable`, may has a data, an error or nothing. We can use it to send `null` follow the rule of`Reactive Streams`,`onSubscribe (onSuccess/onError/onComplete)` |
|`Flowable`| none | added in`2.X`, like`Observable`, follow the rule of`Reactive Streams`, `onSubscribe(onSuccess/onError)`，support`Backpressure` |
|`Subject`| `AsyncSubject`<br>`BehaviorSubject`<br>`PublishSubject`<br>`ReplaySubject`<br>`UnicastSubject` | `2.X`still suport the function of`Subject`and add following stuff<br>`AsyncProcessor`<br>`BehaviorProcessor`<br>`PublishProcessor`<br>`ReplayProcessor`<br>`UnicastProcessor`<br>support`Backpressure` |
|`Subscriber`| `Subscriber` | naming conflict with `Reactive Streams`, `Subscriber`already rename to`Disposable` |


## Observer
```java
// prework, the <String> can translate to <whatever> you want
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
```

## Subscriber

```java

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


```

## Action0, Action1, Action2
```java
// originally called Action1
Consumer<String> onNext = new Consumer<String>() {
    @Override
    public void accept(@NonNull String s) throws Exception {
        Log.d(TAG, s);
    }
};

Consumer<Throwable> onError = new Consumer<Throwable>() {
    @Override
    public void accept(@NonNull Throwable s) throws Exception {

    }
};

// originally called Action0
Action onCompletedAction = new Action() {
    @Override
    public void run() throws Exception {
        Log.d(TAG, "completed");
    }
};

// o1 is the Observable
o1.subscribe(onNext);
o1.subscribe(onNext, onError);
o1.subscribe(onNext, onError, onCompletedAction);


```
