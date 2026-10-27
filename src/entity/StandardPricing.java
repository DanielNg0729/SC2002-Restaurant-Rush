package entity;

/**
 * The normal pricing policy: menu price adjusted by the customer's own price percentage.
 *
 * @since Stage 1
 */
public class StandardPricing implements PricingPolicy {

    /**
     * Makes the standard pricing policy.
     */
    public StandardPricing() {
    }

    /**
     * Item price with the customer's {@link Customer#getPricePercent()} applied using
     * {@link PricingPolicy#applyPercent(int, int)}.
     *
     * @param item     the menu item being ordered
     * @param customer the customer ordering it
     * @return the bill in cents
     */
    @Override
    public int priceFor(MenuItem item, Customer customer) {
        return PricingPolicy.applyPercent(item.getPriceCents(), customer.getPricePercent());
    }
}
