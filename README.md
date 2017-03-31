# RxJavaExample

| description | RxJava 1.X | RxJava 2.X |
| ----- | ----- | ----- |
|`package`| `rx.xxx` | `io.reactivex.xxx` |
| [Reactive Streams rules](http://www.reactive-streams.org/) | `1.X` is before `Reactive Streams`, so it only support apart  | totally support |
|[Backpressure](https://github.com/ReactiveX/RxJava/wiki/Backpressure)|partly support|`Oberservable` not support<br> add `Flowable`支持背压|
|`null`| support | do not support `null`, if send `null` to it, will cause `NullPointerException` |
|`Schedulers`线程调度器| `Schedulers.immediate()`<br>`Schedulers.trampoline()`<br>`Schedulers.computation()`<br>`Schedulers.newThread()`<br>`Schedulers.io()`<br>`Schedulers.from(executor)`<br>`AndroidSchedulers.mainThread()` | 移除`Schedulers.immediate()`<br>新增`Schedulers.single()`<br>其它未变 |
|`Single`| 行为类似`Observable`，但只会发射一个`onSuccess`或`onError` | 按照`Reactive Streams`规范重新设计，遵循协议`onSubscribe(onSuccess/onError)` |
|`Completable`| 行为类似`Observable`，要么全部成功，要么就失败 | 按照`Reactive Streams`规范重新设计，遵循协议`onSubscribe (onComplete/onError)` |
|`Maybe`| 无 | `2.X`新增，行为类似`Observable`，可能会有一个数据或一个错误，也可能什么都没有。可以将其视为一种返回可空值的方法。这种方法如果不抛出异常的话，将总是会返回一些东西，但是返回值可能为空，也可能不为空。按照`Reactive Streams`规范设计，遵循协议`onSubscribe (onSuccess/onError/onComplete)` |
|`Flowable`| 无 | `2.X`新增，行为类似`Observable`，按照`Reactive Streams`规范设计，支持背压`Backpressure` |
|`Subject`| `AsyncSubject`<br>`BehaviorSubject`<br>`PublishSubject`<br>`ReplaySubject`<br>`UnicastSubject` | `2.X`依然维护这些`Subject`现有的功能，并新增：<br>`AsyncProcessor`<br>`BehaviorProcessor`<br>`PublishProcessor`<br>`ReplayProcessor`<br>`UnicastProcessor`<br>支持背压`Backpressure` |
|`Subscriber`| `Subscriber` | 由于与`Reactive Streams`的命名冲突，`Subscriber`已重命名为`Disposable` |


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

