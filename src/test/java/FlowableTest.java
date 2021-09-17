import hu.akarnokd.rxjava3.math.MathFlowable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.processors.PublishProcessor;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.schedulers.TestScheduler;
import io.reactivex.rxjava3.subscribers.TestSubscriber;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;


public class FlowableTest {
    @Test
    public void flowable_test() {
        MathFlowable.averageDouble(Flowable.range(1, 10))
                .test()
                .assertResult(5.5);

        Flowable.just(5, 1, 3, 2, 4)
                .to(MathFlowable::min)
                .test()
                .assertResult(1);

        Flowable.fromArray(Arrays.asList("hello", "lee jinam", "my name is rx java", "nice to meet you"))
                .subscribe(s -> System.out.println("subscribe.. " + s + "!"));
    }

    @Test
    public void observable_test() {
        Observable.just(10,20,30,40,50,60,70,80,90,100)
                .subscribe(x -> System.out.println(x));

        var coldPub = Observable.just(1,2,3,4,5);
        coldPub.subscribe(x -> System.out.println("구독자 1 : " + x));
        coldPub.subscribe(x -> System.out.println("구독자 2 : " + x));
    }

    @Test
    public void cold_publisher_test() {
        Flowable<Integer> flowable = Flowable.just(1,3,5,7,9);
        flowable.subscribe(data -> System.out.println("구독자 1 : " + data));
        flowable.subscribe(data -> System.out.println("구독자 2 : " + data));
    }

    @Test
    public void hot_publisher_test() {
        PublishProcessor<Integer> processor = PublishProcessor.create();
        processor.subscribe(data -> System.out.println("구독자 1 : " + data));
        processor.onNext(1);
        processor.onNext(3);

        processor.subscribe(data -> System.out.println("구독자 2 :" + data));
        processor.onNext(5);
        processor.onNext(7);

        processor.onComplete();


    }

    @Test
    public void doSquareTest() {
        var start = System.currentTimeMillis();
        List square = new ArrayList();
        Flowable.range(1, 100)
            .observeOn(Schedulers.computation())
            .map(v -> v * 2)
            .blockingSubscribe(square::add);
        var end = System.currentTimeMillis();
        System.out.println(square.size());
        square.forEach(System.out::println);
        System.out.println(String.format("%s, %s, %s", start , end, end - start));
    }

    @Test
    public void parallelTest() {
        var start = System.currentTimeMillis();
        List square = new ArrayList();
        Flowable.range(1, 64)
            .flatMap(v -> Flowable.just(v)
                .subscribeOn(Schedulers.computation())
                .map(w -> w * 2)
            )
            .doOnError(err -> err.printStackTrace())
            .doOnComplete(() -> System.out.println("Completed"))
            .blockingSubscribe(square::add);
        var end = System.currentTimeMillis();
        square.forEach(System.out::println);
        System.out.println(String.format("%s, %s, %s", start , end, end - start));
    }

    @Test
    public void schedulersTest() {
        Flowable<String> source = Flowable.fromCallable(() -> {
            Thread.sleep(1000);
            return "Done";
        });
        source.doOnComplete(() -> System.out.println("Completed runComputaion"));
        Flowable<String> background = source.subscribeOn(   Schedulers.io());
        Flowable<String> foreground = background.observeOn(Schedulers.single());
        foreground.subscribe(x -> System.out.println(x));
        background.blockingSubscribe(x -> System.out.println(x));

    }

    @Test
    public void testScheduler() {
        TestScheduler schedulersTest = new TestScheduler();
        Observable<Long> tick = Observable.interval(1, TimeUnit.SECONDS, schedulersTest);
        Observable<String> observable = Observable.just("foo", "bar", "biz", "baz")
            .zipWith(tick, (string, index) -> index + "-" + string);

        TestObserver<String> testObserver = observable.subscribeOn(schedulersTest).test();
        schedulersTest.advanceTimeBy(2300, TimeUnit.MILLISECONDS);
        testObserver.assertNoErrors();
        testObserver.assertValues("0-foo", "1-bar");
        testObserver.assertNotComplete();
    }

    @Test
    public void testSubscriber() {
        TestSubscriber<Integer> testSubscriber = Flowable.range(1, 5).test();
        testSubscriber.assertValueCount(5);
    }
}
