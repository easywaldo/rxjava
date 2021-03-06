import hu.akarnokd.rxjava3.math.MathFlowable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.processors.PublishProcessor;
import org.junit.Test;

import java.util.Arrays;

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
}
