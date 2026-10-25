package entity;

import java.util.List;

/**
 * Happy Hour pricing that the manager can turn on once per game. For two turns, new orders get 20% off (on top of any
 * customer discount) and customers who are still waiting get some satisfaction back.
 *
 * @since Stage 3
 */
public class HappyHour implements PricingPolicy {

    private static final int PRICE_PERCENT = 80;
    private static final int DURATION_TURNS = 2;
    private static final int RECOVERY_POINTS = 15;

    private final PricingPolicy base;
    private boolean used = false;
    private int remainingTurns = 0;

    /**
     * Makes a Happy Hour that starts off and wraps the normal pricing policy.
     *
     * @param base the pricing policy used before the Happy Hour discount
     */
    public HappyHour(PricingPolicy base) {
        this.base = base;
    }

    /**
     * Price from the base policy, with an extra {@code PRICE_PERCENT}% applied while Happy Hour is on. For example a
     * VIP Burger costs 1500 x 90% x 80% = 1080 cents. Orders that were already placed keep the bill they were locked
     * at.
     *
     * @param item     the menu item being ordered
     * @param customer the customer ordering it
     * @return the bill in cents
     */
    @Override
    public int priceFor(MenuItem item, Customer customer) {
        // TODO
        return 0;
    }

    /**
     * Turns Happy Hour on for {@code DURATION_TURNS} turns (this one and the next) if it hasn't been used yet this
     * game. Otherwise it fails and says why.
     *
     * @return the result of trying to turn it on
     */
    public ActionResult activate() {
        // TODO
        return null;
    }

    /**
     * At the start of a Happy Hour turn, gives {@code RECOVERY_POINTS} satisfaction (up to 100) to every customer still
     * waiting for a table or food.
     *
     * @param customers customers currently in the queue or at tables
     */
    public void startTurn(List<Customer> customers) {
        // TODO
    }

    /**
     * Counts down one turn at the end of the turn. Happy Hour stops when there are no turns left.
     */
    public void endTurn() {
        // TODO
    }

    /**
     * True while Happy Hour still has turns left.
     *
     * @return true if Happy Hour is on
     */
    public boolean isActive() {
        // TODO
        return false;
    }

    /**
     * How many Happy Hour turns are left (counting the current one), for the display.
     *
     * @return turns left, or 0 if it's off
     */
    public int getRemainingTurns() {
        return remainingTurns;
    }
}
