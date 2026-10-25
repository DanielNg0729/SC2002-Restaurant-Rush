package entity;

/**
 * Pasta on the menu. Costs $18.00 and takes 2 prep units.
 *
 * @since Stage 1
 */
public class Pasta extends Dish {

    private static final int PRICE_CENTS = 1800;
    private static final int PREP_UNITS = 2;

    /**
     * Makes a Pasta with its fixed price and prep units.
     */
    public Pasta() {
        super("Pasta", PRICE_CENTS, PREP_UNITS);
    }
}
