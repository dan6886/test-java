package deadlock;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

public class CxEvent extends BaseEvent {
    private String name = "";

    public CxEvent(String name) {
        super(name);
    }

    public void happen() {
        this.run();
    }

    public Runnable setFuture(CompletableFuture future) {
        super.setFuture(future);
        return this;
    }

    @Override
    public void run() {
        System.out.println(getName() + "开始" + Thread.currentThread().getId());

        try {
            Thread.sleep(1000 + new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 30; i++) {
            waitFor(new DDEvent("DDevent" + i));
        }
        System.out.println(getName() + "结束");
        complete(getName());
    }
}
