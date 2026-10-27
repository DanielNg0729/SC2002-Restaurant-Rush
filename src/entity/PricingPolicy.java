package entity;

/**
 * Works out the bill (in whole cents) for a menu item ordered by a particular customer.
 *
 * @since Stage 1
 */
public interface PricingPolicy {

    /**
     * Bill in cents when {@code customer} orders {@code item}. The Waiter locks this into the order.
     *
     * @param item     the menu item being ordered
     * @param customer the customer ordering it
     * @return the bill in cents
     */
    int priceFor(MenuItem item, Customer customer);

    /**
     * Shared rounding helper: {@code percent}% of {@code cents}, rounded to the nearest cent with
     * {@code (cents * percent + 50) / 100}.
     *
     * @param cents   amount in cents
     * @param percent percentage to apply
     * @return the rounded amount in cents
     */
    static int applyPercent(int cents, int percent) {
        return (cents * percent + 50) / 100;
    }
}
