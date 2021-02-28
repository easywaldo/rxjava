import io.reactivex.rxjava3.core.BackpressureOverflowStrategy;
import io.reactivex.rxjava3.core.Flowable;
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
                },error -> System.out.println("# error : " + error));

        Thread.sleep(2000L);
    }
}
