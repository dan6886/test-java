package decorator.drinks;

import decorator.Drink;

public class Beer extends Drink {

    public Beer() {
        this.name = "啤酒";
        this.price = 5.0f;
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
