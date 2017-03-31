# RxJavaExample

RxJava Beginner Example

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

