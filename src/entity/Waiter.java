package entity;

import java.util.ArrayList;
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
        if (!isAvailable()) {
            return List.of();
        }
        List<StaffTask> tasks = new ArrayList<>(seatingTasks(restaurant));
        for (Table table : restaurant.getTables()) {
            table.getOccupant()
                    .filter(customer -> customer.getState() == CustomerState.SEATED
                            && customer.getOrder().isEmpty())
                    .ifPresent(customer -> tasks.add(new StaffTask(
                            "Take order from customer " + customer.getId(),
                            () -> takeOrder(table, restaurant.getMenu(), restaurant.getPricing()))));
        }
        for (Order order : restaurant.getActiveOrders()) {
            if (order.isReady() && order.getCustomer().getState() == CustomerState.ORDERED) {
                tasks.add(new StaffTask("Serve customer " + order.getCustomer().getId(),
                        () -> serve(order)));
            }
        }
        return List.copyOf(tasks);
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
        return performTask(() -> {
            if (table == null || menu == null || pricing == null) {
                return ActionResult.fail("A table, menu and pricing policy are required.");
            }
            Customer customer = table.getOccupant().orElse(null);
            if (customer == null || customer.getState() != CustomerState.SEATED
                    || customer.getOrder().isPresent()) {
                return ActionResult.fail("The table needs a seated customer without an order.");
            }
            if (menu.getItems().isEmpty()) {
                return ActionResult.fail("The menu is empty.");
            }
            MenuItem item = customer.chooseItem(menu);
            int billCents = pricing.priceFor(item, customer);
            customer.placeOrder(new Order(customer, item, billCents));
            return ActionResult.ok("Customer " + customer.getId() + " ordered " + item.getName()
                    + " for " + billCents + " cents.");
        });
    }

    /**
     * Serves an order that was already READY when the Waiter acts, and marks both the order and the customer SERVED.
     *
     * @param order the order to serve
     * @return the result of trying to serve it
     */
    public ActionResult serve(Order order) {
        return performTask(() -> {
            if (order == null || !order.isReady()
                    || order.getCustomer().getState() != CustomerState.ORDERED
                    || order.getCustomer().getOrder().orElse(null) != order) {
                return ActionResult.fail("Only a customer's own READY order can be served.");
            }
            order.markServed();
            order.getCustomer().markServed();
            return ActionResult.ok("Served customer " + order.getCustomer().getId() + ".");
        });
    }
}
