package decorator.addthings;

import decorator.Drink;

public class Salt extends Addthing {
    public Salt(Drink drink) {
        super(drink);
        this.name = "盐";
        this.price = 0.2f;
        this.detail = "加盐";
    }

    @Override
    public String getName() {
        return name+"的"+mDrink.getName();
    }

    @Override
    public float getPrice() {
        return price+mDrink.getPrice();
    }

    @Override
    public String getDetail() {
        return this.detail + "," + mDrink.getDetail();
    }
}
