package rxjava2;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import java.util.concurrent.TimeUnit;

/**
 * 这个操作符和之前理解的一样，都是把多个数据源发送的数据合并压缩处理，必须等到多个数据源都发送一个数据值才可以进行触发，否则就会等待。
 */
public class ZipCase extends BaseCase {
    @Override
    public void preTest() {

    }

    @Override
    public void doTest() {
                print(":apply:");
        Observable<Long> interval1 = Observable.interval(2, TimeUnit.MILLISECONDS);
        Observable<String> interval2 = Observable.interval(10, TimeUnit.SECONDS).map((Function<Long, String>) aLong -> String.valueOf(aLong));
        Observable.zip(interval1, interval2, new BiFunction<Long, String, String>() {
            @Override
            public String apply(Long aLong, String s) throws Exception {
                print(aLong + ":apply:" + s);
                return aLong + ":apply:" + s;
            }
        }).blockingSubscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                print(s);
            }
        });
    }

    @Override
    public void finishTest() {

    }
}
