package entity;

/**
 * Anything a customer can order. It has a name, a price in cents and a prep cost.
 *
 * @since Stage 1
 */
public interface MenuItem {

    /**
     * Name shown on the menu and in order messages.
     *
     * @return the item's name
     */
    String getName();

    /**
     * Full price before any customer or pricing discount.
     *
     * @return the price in cents
     */
    int getPriceCents();

    /**
     * How many Chef prep units this item needs before it's READY.
     *
     * @return number of prep units
     */
    int getPrepUnits();
}
