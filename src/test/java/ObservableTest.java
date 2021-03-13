import io.reactivex.rxjava3.core.Observable;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ObservableTest {

    @Test
    public void interval_test() throws InterruptedException {
        Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
            .map(num -> num + " count")
            .subscribe(data -> System.out.println(data));

        Thread.sleep(3000L);
    }

    @Test
    public void range_test() {
        Observable.range(0, 5)
            .subscribe(data -> System.out.println(data));

    }

    @Test
    public void timer_test() throws InterruptedException {
        Observable<String> observable = Observable.timer(2000, TimeUnit.MILLISECONDS)
            .map(data -> "do work!!!");
        observable.subscribe(data -> System.out.println(data));
        Thread.sleep(3000);
    }

    @Test
    public void defer_test() throws InterruptedException {
        Observable<LocalDateTime> observable = Observable.defer(() -> {
            return Observable.just(LocalDateTime.now());
        });
        Observable<LocalDateTime> observableJust = Observable.just(LocalDateTime.now());

        observable.subscribe(time -> System.out.println("# 현재시각 : " + LocalDateTime.now().toString() + ", defer 구독시간 : " + time));
        observableJust.subscribe(time -> System.out.println("# 현재시각 : " + LocalDateTime.now().toString() + " ,just 구독시간 : " + time));

        Thread.sleep(3000);

        observable.subscribe(time -> System.out.println("# 현재시각 : " + LocalDateTime.now().toString() + " , defer 구독시간 : " + time));
        observableJust.subscribe(time -> System.out.println("# 현재시각 : " + LocalDateTime.now().toString() + " , just 구독시간 : " + time));
    }

    @Test
    public void from_iterable_test() throws InterruptedException {
        List<String> countries = Arrays.asList("Korea", "USA", "England");
        Observable.fromIterable(countries)
            .subscribe(data -> System.out.println(data));
        Thread.sleep(3000L);
    }

    @Test
    public void future_test() throws InterruptedException {
        System.out.println("# Start Time : " + LocalDateTime.now());

        Future<Double> future = long_time_work();

        short_time_work();

        Observable.fromFuture(future)
                .subscribe(data -> System.out.println("# 긴 처리 작업시간 결과 : " + data));

        System.out.println("# End Time : " + LocalDateTime.now());
    }

    private CompletableFuture<Double> long_time_work() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return calculate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private Double calculate() throws InterruptedException {
        System.out.println("Long Time 작업 진행 중..." + LocalDateTime.now());
        Thread.sleep(6000L);
        System.out.println("Long Time 작업완료..." + LocalDateTime.now());
        return 1000000.0;
    }

    private void short_time_work() throws InterruptedException {
        Thread.sleep(1000L);
        System.out.println("Short Time 작업완료..." + LocalDateTime.now());
    }

    @Test
    public void flatmap_test() throws InterruptedException {
        Observable.range(2, 1)
                .flatMap(num -> Observable.range(1, 9).map(row -> num + " * " + row + " = " + num * row))
                .subscribe(data -> System.out.println(data));
        Thread.sleep(3000L);
    }

    @Test
    public void flatmap_test_2() throws InterruptedException {
        Observable.range(2, 1)
                .flatMap(num -> Observable.range(1, 9),
                        (source, transform) -> source + " * " + transform + " = " + source * transform)
                .subscribe(data -> System.out.println(data));
        Thread.sleep(3000L);
    }
}
