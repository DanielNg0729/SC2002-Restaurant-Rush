package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * The Chef. Each turn they can move one order forward by one prep unit.
 *
 * @since Stage 1
 */
public class Chef extends Staff {

    /**
     * Package-private because only Restaurant creates staff.
     *
     * @param name the Chef's name
     */
    Chef(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "Chef";
    }

    /**
     * One prep task for every order that still needs cooking, including orders the Waiter took earlier this same turn.
     *
     * @param restaurant the restaurant to check
     * @return the available tasks
     */
    @Override
    public List<StaffTask> listAvailableTasks(Restaurant restaurant) {
        if (!isAvailable()) {
            return List.of();
        }
        List<StaffTask> tasks = new ArrayList<>();
        for (Order order : restaurant.getActiveOrders()) {
            if (order.needsPreparation() && order.getCustomer().getState() == CustomerState.ORDERED) {
                tasks.add(new StaffTask("Prepare " + order.getItem().getName()
                        + " for customer " + order.getCustomer().getId(), () -> prepare(order)));
            }
        }
        return List.copyOf(tasks);
    }

    /**
     * Works on {@code order} for one prep unit (this is the Chef's task for the turn). Once the last unit is done the
     * order becomes READY.
     *
     * @param order the order to cook
     * @return the result of trying to prepare it
     */
    public ActionResult prepare(Order order) {
        return performTask(() -> {
            if (order == null || !order.needsPreparation()
                    || order.getCustomer().getState() != CustomerState.ORDERED
                    || order.getCustomer().getOrder().orElse(null) != order) {
                return ActionResult.fail("This order does not need preparation.");
            }
            order.advancePreparation();
            return ActionResult.ok("Prepared one unit for customer " + order.getCustomer().getId()
                    + "; " + order.getRemainingUnits() + " unit(s) remaining.");
        });
    }
}
