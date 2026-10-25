package entity;

/**
 * Burger on the menu. Costs $15.00 and takes 1 prep unit.
 *
 * @since Stage 1
 */
public class Burger extends Dish {

    private static final int PRICE_CENTS = 1500;
    private static final int PREP_UNITS = 1;

    /**
     * Makes a Burger with its fixed price and prep units.
     */
    public Burger() {
        super("Burger", PRICE_CENTS, PREP_UNITS);
    }
}
