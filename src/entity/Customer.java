package entity;

import java.util.Optional;

/**
 * Base class for every customer type. A customer looks after their own satisfaction and state, and subclasses just
 * override the hooks.
 *
 * @since Stage 1
 */
public abstract class Customer {

    private static final int MAX_SATISFACTION = 100;

    private final int id;
    private final int arrivalTurn;
    private int satisfaction = MAX_SATISFACTION;
    private CustomerState state = CustomerState.WAITING;
    private Order order;

    /**
     * New customers arrive with full satisfaction and join the waiting queue.
     *
     * @param id          arrival number, starting from 1
     * @param arrivalTurn the turn they arrive on
     */
    protected Customer(int id, int arrivalTurn) {
        this.id = id;
        this.arrivalTurn = arrivalTurn;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTurn() {
        return arrivalTurn;
    }

    /**
     * Current satisfaction, somewhere between 0 and {@code MAX_SATISFACTION}.
     *
     * @return current satisfaction
     */
    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerState getState() {
        return state;
    }

    /**
     * The customer's order, once they've placed one.
     *
     * @return the order, or empty if they haven't ordered yet
     */
    public Optional<Order> getOrder() {
        return Optional.ofNullable(order);
    }

    /**
     * Checks if the customer is done eating and waiting for the Cashier.
     *
     * @return true if the customer is READY_TO_PAY
     */
    public boolean isReadyToPay() {
        // TODO
        return false;
    }

    /**
     * Checks if the customer hit 0 satisfaction and walked out without paying.
     *
     * @return true if the customer is LEFT
     */
    public boolean hasLeft() {
        // TODO
        return false;
    }

    /**
     * Default way of picking an order: customer n orders menu item n, going back to item 1 after the last one.
     *
     * @param menu the menu to pick from
     * @return the chosen item
     */
    public MenuItem chooseItem(Menu menu) {
        // TODO
        return null;
    }

    /**
     * How much of the menu price this customer pays, as a percentage. Normal customers pay full price.
     *
     * @return the price percentage (100 by default)
     */
    public int getPricePercent() {
        return 100;
    }

    /**
     * End-of-turn rules, the same for every customer type. If they were served on an earlier turn they finish eating.
     * If they're still waiting for a table or food they lose {@link #getWaitingLoss()} points (never below 0) and leave
     * once they hit 0. Customers who are served, eating or ready to pay don't lose anything.
     */
    public final void endTurn() {
        // TODO
    }

    /**
     * Gives back some satisfaction (up to {@code MAX_SATISFACTION}), but only if the customer is still waiting for a
     * table or food. Otherwise nothing happens.
     *
     * @param points how many points to give back
     */
    final void recoverIfWaiting(int points) {
        // TODO
    }

    /**
     * Waiting customer becomes SEATED after a staff member seats them.
     */
    final void markSeated() {
        // TODO
    }

    /**
     * Saves the customer's order and moves them from SEATED to ORDERED.
     *
     * @param order the order the Waiter just took
     */
    final void placeOrder(Order order) {
        // TODO
    }

    /**
     * Customer becomes SERVED once their READY order is brought out, so they can start eating.
     */
    final void markServed() {
        // TODO
    }

    /**
     * Ready-to-pay customer becomes PAID after the Cashier takes the money.
     */
    final void markPaid() {
        // TODO
    }

    /**
     * Customer type shown on the turn display, like "Regular".
     *
     * @return the type name
     */
    protected abstract String getTypeName();

    /**
     * How many satisfaction points this type of customer loses per turn of waiting.
     *
     * @return points lost per waiting turn
     */
    protected abstract int getWaitingLoss();

    /**
     * Customer who was served on an earlier turn finishes eating and becomes READY_TO_PAY.
     */
    private void advanceEating() {
        // TODO
    }

    /**
     * Takes {@link #getWaitingLoss()} off satisfaction, and if it drops to 0 the customer becomes LEFT.
     */
    private void applyWaitingLoss() {
        // TODO
    }

    /**
     * Changes state, but only if {@link CustomerState#canMoveTo(CustomerState)} allows it. Illegal moves get rejected.
     *
     * @param next the state to move to
     */
    private void moveTo(CustomerState next) {
        // TODO
    }

    /**
     * Adds {@code delta} to satisfaction and keeps it between 0 and {@code MAX_SATISFACTION}.
     *
     * @param delta change in satisfaction (negative means a loss)
     */
    private void adjustSatisfaction(int delta) {
        // TODO
    }
}
