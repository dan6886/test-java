package decorator.addthings;

import decorator.Drink;

public class Sugar extends Addthing {

    public Sugar(Drink drink) {
        super(drink);
        this.name = "糖";
        this.price = 0.5f;
        this.detail = "加糖";
    }

    @Override
    public String getName() {
        return name + "的" + mDrink.getName();
    }

    @Override
    public float getPrice() {
        return price + mDrink.getPrice();
    }

    @Override
    public String getDetail() {
        return this.detail + "," + mDrink.getDetail();
    }
}
