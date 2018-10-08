package decorator;

import decorator.addthings.Salt;
import decorator.addthings.Sugar;
import decorator.drinks.Beer;

public class Main {
    public static void main(String[] args) {
        Beer beer = new Beer();
        Drink drink = new Salt(new Sugar(beer));
        System.out.println(drink.getName());
        System.out.println(drink.getPrice());
        System.out.println(drink.getDetail());
    }
}
