import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observables.GroupedObservable;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.*;
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

    /***
     * 전체 통지가 되기 전에 딜레이
     * @throws InterruptedException
     */
    @Test
    public void delay_test() throws InterruptedException {
        System.out.println("시작시간 : " + LocalDateTime.now());
        Observable.just(1,2,3,4,5)
            .doOnNext(data -> System.out.println("now is " + LocalDateTime.now() + " : " + data))
            .delay(3000L, TimeUnit.MILLISECONDS)
            .subscribe(data -> System.out.println("now is " + LocalDateTime.now() + " " + "sub data is : " + data));
        Thread.sleep(3000L);
    }

    /***
     * 각각에 대한 딜레이
     * @throws InterruptedException
     */
    @Test
    public void delay_test_v2() throws InterruptedException {
        System.out.println("시작시간 : " + LocalDateTime.now());
        Observable.just(1,2,3,4,5)
            .doOnNext(data -> System.out.println("now is " + LocalDateTime.now() + " : " + data))
            .delay(item -> {
                Thread.sleep(2000L);
                return Observable.just(item);
            })
            .subscribe(data -> System.out.println("now is " + LocalDateTime.now() + " " + "sub data is : " + data));
        Thread.sleep(3000L);
    }

    @Test
    public void delay_test_delay_sub() throws InterruptedException {
        System.out.println("시작시간 : " + LocalDateTime.now());
        Observable.just(1,2,3,4,5)
            .doOnNext(data -> System.out.println("now is " + LocalDateTime.now() + " : " + data))
            .delaySubscription(2000L, TimeUnit.MILLISECONDS)
            .subscribe(data -> System.out.println("now is " + LocalDateTime.now() + " " + "sub data is : " + data));
        Thread.sleep(3000L);
    }

    @Test
    public void delay_test_delay_error() throws InterruptedException {
        Observable.range(1, 5)
            .map(num -> {
                long time = 10000L;
                if (num == 4) {
                    time = 15000L;
                }
                Thread.sleep(time);
                return num;
            })
            .timeout(1200L, TimeUnit.MILLISECONDS)
            .subscribe(data -> System.out.println(data), error -> System.out.println(error));
        Thread.sleep(3000L);
    }

    @Test
    public void delay_test_delay_interval() throws InterruptedException {
        Observable.just(2,4,6,8,10)
            .delay(item -> {
                Thread.sleep(new Random().nextInt(3000));
                return Observable.just(item);
            })
            .timeInterval(TimeUnit.MILLISECONDS)
            .subscribe(timed -> System.out.println("데이터 통지하는데 걸리는 시간: " + timed.time() + " 통지 데이터 :" + timed.value()));
        Thread.sleep(3000L);
    }

    @Test
    public void delay_test_delay_materialize() throws InterruptedException {
        Observable.just(2,4,6,8,10)
            .materialize()
            .subscribe(notification -> {
                String notificationType = notification.isOnNext() ? "onNext()" : (notification.isOnError() ?
                    "onError()" : "onComplete()");
                System.out.println("통지타입 :" + notificationType);
                System.out.println(notification.getValue());
            });

        Thread.sleep(3000L);
    }

    @Test
    public void delay_test_delay_concatEager() throws InterruptedException {
       Observable.concatEager(
           Arrays.asList(Observable.just(1,2,3,4,5), Observable.just(6,7,8,9,10).map(m -> {
               if (m.equals(8)) {
                   throw new IllegalArgumentException();
               }
               return m;
           })))
       .materialize()
       .map(notification -> {
           if (notification.isOnError()) {
               System.out.println("error raised....");
           }
           return notification;
       })
       .filter(notification -> !notification.isOnError())
       .dematerialize(notification -> notification)
       .subscribe(
           data -> System.out.println("onNext : " + data),
           error -> System.out.println("onError : " + error),
           () -> System.out.println("completed")
       );

        Thread.sleep(3000L);
    }

    @Test
    public void amb_test() throws InterruptedException {
        List<Observable<Car>> observables = Arrays.asList(
            Observable.fromIterable(Arrays.asList(new Car("ionic5", "electronic", "hyundai")))
                .delay(200L, TimeUnit.MILLISECONDS)
                .doOnComplete(() -> System.out.println("completed ionic5")),
            Observable.fromIterable(Arrays.asList(new Car("benz", "gasoline", "mercedesz")))
                .delay(300L, TimeUnit.MILLISECONDS)
                .doOnComplete(() -> System.out.println("completed benz")));


        Observable.amb(observables)
            .doOnComplete(() -> System.out.println("모두 완료"))
            .subscribe(data -> System.out.println(data));

        Thread.sleep(10000L);
    }

    @Test
    public void contains_test() {
        Observable.fromIterable(Arrays.asList(1,2,3,4,5,100))
            .contains(7)
            .subscribe(data -> System.out.println(data));

        Observable.fromIterable(Arrays.asList(1,2,3,4,5,100))
            .doOnNext(data -> System.out.println("data is " + data))
            .contains(5)
            .subscribe(data -> System.out.println(data));

    }

    @Test
    public void default_if_empty_test() {
        Observable.just(1,2,4,5,6,7)
            .filter(x -> x > 10)
            .defaultIfEmpty(10)
            .doOnNext(data -> System.out.println("data is " + data))
            .subscribe(data -> System.out.println("subscribe : " + data));

        Observable.fromArray(Arrays.asList(1,100))
            .defaultIfEmpty(Arrays.asList(1,2,3,4,5,6,7,8,9,10))
            .doOnNext(data -> System.out.println("data is " + data))
            .subscribe(data -> System.out.println("subscribe : " + data));

        Observable.just(3,5,6,7)
            .filter(x -> x < 3)
            .doOnNext(data -> System.out.println("data is " + data))
            .defaultIfEmpty(1)
            .subscribe(data -> System.out.println("subscribe : " + data));

    }

    @Test
    public void sequence_equals_test() {
        Observable<Integer> listFirst =  Observable.just(1,2,3,4,5)
            .doOnSubscribe(data -> System.out.println("data is " + data))
            .doOnNext(data -> System.out.println("data is " + data));
        Observable<Integer> listSecond = Observable.just(1,2,3,4,5)
            .doOnSubscribe(data -> System.out.println("data is " + data))
            .doOnNext(data -> System.out.println("data is " + data));
        Observable.sequenceEqual(listFirst, listSecond)
            .subscribe(System.out::println);

        Observable<Integer> listThird =  Observable.just(0,1,2,3,4,5)
            .doOnSubscribe(data -> System.out.println("data is " + data))
            .doOnNext(data -> System.out.println("data is " + data));
        Observable<Integer> listForth = Observable.just(1,2,3,4,5)
            .doOnSubscribe(data -> System.out.println("data is " + data))
            .doOnNext(data -> System.out.println("data is " + data));
        Observable.sequenceEqual(listThird, listForth)
            .subscribe(System.out::println);
    }

    @Test
    public void count_test() {
        Observable.fromIterable(Arrays.asList(1,2,3,4,5,100))
            .count()
            .subscribe(data -> System.out.println("Count is " + data));
    }

    @Test
    public void reduce_test() {
        Observable.fromIterable(Arrays.asList(1,2,3,4,5,100))
            .reduce(Integer::sum)
            .subscribe(data -> System.out.println("Reduce is " + data));
    }
}
