package rxjava2;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * 这个操作符的作用时在发生异常的时候，不触发onError函数
 * 而是调用备用的被观察者触发这个观察者的事件发送，上一个被观察者的未发送的事件全部丢弃
 * 当这个备用被观察者发送完毕之后则可以完成complete事件出去
 * 当然此操作符也可以传入一个function 来处理对应的throwable并且返回一个被观察者
 * todo 已验证
 */
public class ErrorResumeNextCase extends BaseCase {
    @Override
    public void preTest() {

    }

    @Override
    public void doTest() {
        Observable.just("123", "dsd", "111").map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                return Integer.valueOf(s);
            }
        }).onErrorResumeNext(Observable.just(1991, 0, 1993).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) throws Exception {
                return 1000 / integer;
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> apply(Throwable throwable) throws Exception {
                return Observable.just(2020);
            }
        }))
                .subscribe(new Observer<Integer>() {
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
