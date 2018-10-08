package decorator;

public abstract class Drink {
    protected String name;
    protected float price;
    protected String detail;

    public abstract String getName();

    public abstract float getPrice();

    public abstract String getDetail();
}
