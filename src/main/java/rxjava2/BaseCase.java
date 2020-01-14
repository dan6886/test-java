package rxjava2;

public abstract class BaseCase {

    final void run() {
        preTest();
        doTest();
        finishTest();
    }

    public abstract void preTest();

    public abstract void doTest();

    public abstract void finishTest();

    public void print(String s) {
        String simpleName = getClass().getSimpleName();
        System.out.println(simpleName + ":" + s);
    }
}
