package decorator.drinks;

import decorator.Drink;

public class Coffee extends Drink {
    public Coffee() {
        this.name = "咖啡";
        this.price = 0.8f;
        this.detail = "";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public String getDetail() {
        return detail;
    }

}
