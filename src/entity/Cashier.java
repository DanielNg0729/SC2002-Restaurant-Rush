package entity;

import java.util.List;
import java.util.Optional;

/**
 * The Cashier. Takes at most one payment per turn on their own, starting with whoever has been ready to pay the
 * longest.
 *
 * @since Stage 1
 */
public class Cashier extends Staff {

    /**
     * Package-private because only Restaurant creates staff.
     *
     * @param name the Cashier's name
     */
    Cashier(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "Cashier";
    }

    /**
     * Gives back the payment task if someone can pay this turn. The Cashier does this automatically, the player doesn't
     * pick it.
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
     * Takes payment from the customer who has been READY_TO_PAY the longest (since an earlier turn). Records the locked
     * bill and their satisfaction in the ledger, marks the order and the customer PAID, and frees up the table.
     *
     * @param restaurant the restaurant whose ledger and tables get updated
     * @return the result, with the amount paid if it worked
     */
    public ActionResult processNextPayment(Restaurant restaurant) {
        // TODO
        return null;
    }

    /**
     * Finds the customer who has been waiting to pay the longest, if there is one.
     *
     * @param restaurant the restaurant to check
     * @return that customer, or empty if nobody is ready to pay
     */
    private Optional<Customer> oldestReadyToPay(Restaurant restaurant) {
        // TODO
        return Optional.empty();
    }
}
