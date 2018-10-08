package decorator.addthings;

import decorator.Drink;

public abstract class Addthing extends Drink {
    protected Drink mDrink;

    public Addthing(Drink drink) {
        mDrink = drink;
    }
}
