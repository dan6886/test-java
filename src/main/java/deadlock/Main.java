package deadlock;

/**
 * 死锁的例子
 */
public class Main {
    public static void main(String[] arg) {
        Object obj1 = new Object();
        Object obj2 = new Object();
        LockRunable runable1 = new LockRunable(obj1, obj2);
        LockRunable runable2 = new LockRunable(obj2, obj1);
        new Thread(runable1, "r1").start();
        new Thread(runable2, "r2").start();
    }

    private static class LockRunable implements Runnable {
        private Object obj1;
        private Object obj2;

        public LockRunable(Object obj1, Object obj2) {
            this.obj1 = obj1;
            this.obj2 = obj2;
        }

        @Override
        public void run() {
            synchronized (obj1) {
                try {
                    System.out.println(Thread.currentThread().getName() + "wait");
                    obj1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (obj2) {
                    obj2.notifyAll();
                }
            }
        }
    }
}



