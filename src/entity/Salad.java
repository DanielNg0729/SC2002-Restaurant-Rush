package entity;

/**
 * Salad on the menu. Costs $9.00 and takes 1 prep unit.
 *
 * @since Stage 1
 */
public class Salad extends Dish {

    private static final int PRICE_CENTS = 900;
    private static final int PREP_UNITS = 1;

    /**
     * Makes a Salad with its fixed price and prep units.
     */
    public Salad() {
        super("Salad", PRICE_CENTS, PREP_UNITS);
    }
}
