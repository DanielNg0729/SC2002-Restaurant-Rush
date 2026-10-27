package entity;

/**
 * One customer's order for one menu item. The bill gets locked in when the order is taken, and it also tracks how far
 * the kitchen has got.
 *
 * @since Stage 1
 */
public class Order {

    private final Customer customer;
    private final MenuItem item;
    private final int billCents;
    private int unitsDone = 0;
    private OrderStatus status = OrderStatus.PLACED;

    /**
     * Makes a PLACED order. The bill is locked at whatever price was quoted when the order was taken.
     *
     * @param customer  who ordered
     * @param item      what they ordered
     * @param billCents the bill in cents, already worked out by the pricing policy
     */
    Order(Customer customer, MenuItem item, int billCents) {
        this.customer = customer;
        this.item = item;
        this.billCents = billCents;
    }

    public Customer getCustomer() {
        return customer;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getBillCents() {
        return billCents;
    }

    public OrderStatus getStatus() {
        return status;
    }

    /**
     * How many prep units are still left (the item's prep units minus the ones already done).
     *
     * @return units left, 0 once the order is READY
     */
    public int getRemainingUnits() {
        return Math.max(0, item.getPrepUnits() - unitsDone);
    }

    /**
     * True if all prep units are done and the order is just waiting to be served.
     *
     * @return true if the order is READY
     */
    public boolean isReady() {
        return status == OrderStatus.READY;
    }

    /**
     * True if the order is PLACED or IN_PREPARATION, meaning the Chef can still work on it.
     *
     * @return true if the order still needs cooking
     */
    public boolean needsPreparation() {
        return status == OrderStatus.PLACED || status == OrderStatus.IN_PREPARATION;
    }

    /**
     * Finishes one prep unit. The order goes to IN_PREPARATION, then READY after the last unit.
     */
    void advancePreparation() {
        if (!needsPreparation()) {
            return;
        }
        unitsDone++;
        status = unitsDone >= item.getPrepUnits() ? OrderStatus.READY : OrderStatus.IN_PREPARATION;
    }

    /**
     * READY order becomes SERVED.
     */
    void markServed() {
        if (status == OrderStatus.READY) {
            status = OrderStatus.SERVED;
        }
    }

    /**
     * SERVED order becomes PAID after the Cashier records the payment.
     */
    void markPaid() {
        if (status == OrderStatus.SERVED) {
            status = OrderStatus.PAID;
        }
    }

    /**
     * Cancels an unfinished order when the customer leaves without paying.
     */
    void cancel() {
        if (status != OrderStatus.PAID && status != OrderStatus.CANCELLED) {
            status = OrderStatus.CANCELLED;
        }
    }
}
