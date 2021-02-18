import hu.akarnokd.rxjava3.math.MathFlowable;
import io.reactivex.rxjava3.core.Flowable;
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
}
