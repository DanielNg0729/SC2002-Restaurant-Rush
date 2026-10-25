package entity;

/**
 * A single dish on the menu. Each subclass fixes its own name, price and prep units.
 *
 * @since Stage 1
 */
public abstract class Dish implements MenuItem {

    private final String name;
    private final int priceCents;
    private final int prepUnits;

    /**
     * Makes a dish with its fixed menu values.
     *
     * @param name       name shown on the menu
     * @param priceCents full price in cents
     * @param prepUnits  how many prep units it needs
     */
    protected Dish(String name, int priceCents, int prepUnits) {
        this.name = name;
        this.priceCents = priceCents;
        this.prepUnits = prepUnits;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriceCents() {
        return priceCents;
    }

    @Override
    public int getPrepUnits() {
        return prepUnits;
    }
}
