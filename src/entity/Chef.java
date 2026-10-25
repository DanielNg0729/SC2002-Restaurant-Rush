package entity;

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
        // TODO
        return List.of();
    }

    /**
     * Works on {@code order} for one prep unit (this is the Chef's task for the turn). Once the last unit is done the
     * order becomes READY.
     *
     * @param order the order to cook
     * @return the result of trying to prepare it
     */
    public ActionResult prepare(Order order) {
        // TODO
        return null;
    }
}
