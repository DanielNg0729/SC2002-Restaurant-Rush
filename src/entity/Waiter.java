package entity;

import java.util.List;

/**
 * The Waiter. Each turn they can do one thing: seat a customer, take an order or serve a READY order.
 *
 * @since Stage 1
 */
public class Waiter extends FrontOfHouseStaff {

    /**
     * Package-private because only Restaurant creates staff.
     *
     * @param name the Waiter's name
     */
    Waiter(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "Waiter";
    }

    /**
     * What the Waiter can do this turn: seat someone from the queue, take a seated customer's order, or serve an order
     * that's already READY.
     *
     * @param restaurant the restaurant to check
     * @return the available tasks
     */
    @Override
    public List<StaffTask> listAvailableTasks(Restaurant restaurant) {
        // TODO
        return List.of();
    }

    /**
     * Takes the order of whoever is sitting at {@code table} (this uses up the Waiter's task). The customer picks
     * something from the menu, the pricing policy locks in the bill, and the customer gets a new PLACED order.
     *
     * @param table   the table of the customer who's ordering
     * @param menu    the menu they pick from
     * @param pricing the pricing policy that sets the bill
     * @return the result, with the item and bill if it worked
     */
    public ActionResult takeOrder(Table table, Menu menu, PricingPolicy pricing) {
        // TODO
        return null;
    }

    /**
     * Serves an order that was already READY when the Waiter acts, and marks both the order and the customer SERVED.
     *
     * @param order the order to serve
     * @return the result of trying to serve it
     */
    public ActionResult serve(Order order) {
        // TODO
        return null;
    }
}
