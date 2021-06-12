import io.reactivex.rxjava3.core.BackpressureOverflowStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class FlowableBackPressureTest {
    @Test
    public void drop_latest_test() throws InterruptedException {
        System.out.println("# Start : " + LocalDateTime.now().toString());
        Flowable.interval(300L, TimeUnit.MILLISECONDS)
                .doOnNext((data -> System.out.println("# interval doOnNext(() : " + data)))
                .onBackpressureBuffer(2,
                        () -> System.out.println("overflow!"),
                        BackpressureOverflowStrategy.DROP_LATEST)
                .doOnNext((data -> System.out.println("# onBackPressureBuffer doOnNext : " + data)))
                .subscribe(data -> {
                    Thread.sleep(1000L);
                    System.out.println("# subscribe : " + data);
                }, error -> System.out.println("# error : " + error));

        Thread.sleep(2000L);
    }

    @Test
    public void drop_oldest_test() throws InterruptedException {
        System.out.println("# Start : " + LocalDateTime.now().toString());
        Flowable.interval(300L, TimeUnit.MILLISECONDS)
                .doOnNext((data -> System.out.println("# interval doOnext() : " + data)))
                .onBackpressureBuffer(2,
                        () -> System.out.println("overflow"),
                        BackpressureOverflowStrategy.DROP_OLDEST)
                .doOnNext((data -> System.out.println("# onBackPressureBuffer doOnNext : " + data)))
                .observeOn(Schedulers.computation(), false, 1)
                .subscribe(data -> {
                    Thread.sleep(1000L);
                    System.out.println("# subscribe : " + data);
                }, error -> System.out.println("# error : " + error));

        Thread.sleep(4500L);
    }

    @Test
    public void drop_test() throws InterruptedException {
        Flowable.interval(300L, TimeUnit.MILLISECONDS)
                .doOnNext((data -> System.out.println("# interval doOnNext() : " + data)))
                .onBackpressureDrop(dropData -> System.out.println("overflow : " + dropData))
                .observeOn(Schedulers.computation(), false, 1)
                .subscribe(
                        data -> {
                            Thread.sleep(1000L);
                            System.out.println("#subscribe : " + data);
                        },
                        error -> System.out.println("# error : " + error)
                );
        Thread.sleep(5500L);
    }


}
