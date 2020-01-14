package rxjava2;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 *这个操作符是会起到捕捉异常的作用，当发送连续的数据的时候，中间发生异常，不会调用onError函数
 * 而是触发这个操作符，返回一个备用的对象给观察者，然后complete,后续数据就会被丢弃
 * todo 已验证
 */
public class ErrorReturnCase extends BaseCase {

    @Override
    public void preTest() {

    }

    @Override
    public void doTest() {
        Observable.just("22","12d3", "123").map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                return Integer.valueOf(s);
            }
        }).onErrorReturn(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable throwable) throws Exception {
                return 1991;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                print("onNext" + integer);
            }

            @Override
            public void onError(Throwable e) {
                print("onError");
            }

            @Override
            public void onComplete() {
                print("onComplete");
            }
        });
    }

    @Override
    public void finishTest() {

    }
}
