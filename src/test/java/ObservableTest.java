import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observables.GroupedObservable;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    @Test
    public void to_list_test() throws InterruptedException {
        Single<List<Integer>> single = Observable.just(1,3,5,7,9).toList();
        single.subscribe(data -> System.out.println(data));

        Thread.sleep(3000L);
    }

    @Test
    public void to_map_test() throws InterruptedException {
        Single<Map<String, String>> single = Observable.just("1-이채은", "2-이지남", "3-장미선", "4-임한규")
                .toMap(s -> s.split("-")[0]);
        single.subscribe(data -> System.out.println(data));
        Thread.sleep(3000L);
    }

    @Test
    public void to_map_test_key_value() throws InterruptedException {
        Single<Map<String, String>> single = Observable.just("1-이채은", "2-이지남", "3-장미선", "4-임한규")
                .toMap(s -> s.split("-")[0], s -> s.split("-")[1]);
        single.subscribe(data -> System.out.println(data));
        Thread.sleep(3000L);
    }

    @Test
    public void group_by_test() throws InterruptedException {

        Car car1 = new Car ("avante","sedan","hyundai");
        Car car2 = new Car("benz", "sedan","mercedez");
        Car car3 = new Car("k9", "sedan", "hyundai");
        List<Car> carList = Arrays.asList(car1, car2, car3);

        Observable<GroupedObservable<String, Car>> observable =
                Observable.fromIterable(carList).groupBy(Car::getCarType);
        observable.subscribe(group -> group.subscribe(car -> System.out.println("Group : " + group.getKey() + ", " + car.getCarName())));
        Thread.sleep(3000L);
    }
}
