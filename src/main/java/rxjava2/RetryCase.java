package rxjava2;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * 重试机制，捕获上边代码发出的异常并且重试，遇到异常抛出的时候会从事件源重新发送，也就是仿佛重新调用了一下订阅一样，但是onSubscribe不会再次调用,
 * retry 期间不会触发onError函数，只有当最后函数重试完了之后，还有异常就乎触发onError
 * 当然也可以传入一个function 来返回一个Boolean值来决定需不需要重试
 * todo 已验证
 */

public class RetryCase extends BaseCase {

    @Override
    public void preTest() {
    }

    @Override
    public void doTest() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("dan");
                emitter.onError(new Throwable("test"));
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return s;
            }
        }).retry(100).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        print("onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        print("onNext:"+s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        print("onError" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        print("onComplete");
                    }
                });
    }

    private void test1() {
        Observable.just("dan").map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return s;
            }
        }).map((Function<String, Integer>) s -> {
            print("map");
            return Integer.valueOf(s);
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return String.valueOf(integer);
            }
        }).retry(21)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        print("onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        print("onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        print("onError" + e.getLocalizedMessage());
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
