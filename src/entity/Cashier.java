package entity;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * The Cashier. Takes at most one payment per turn on their own, oldest customer first. Customers only become
 * READY_TO_PAY in {@link Customer#endTurn()}, which the game runs after payments, so anyone who becomes ready this turn
 * pays next turn at the earliest.
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
        if (!isAvailable() || oldestReadyToPay(restaurant).isEmpty()) {
            return List.of();
        }
        return List.of(new StaffTask("Process the next payment",
                () -> processNextPayment(restaurant)));
    }

    /**
     * Takes payment from the oldest READY_TO_PAY customer. Records the locked bill and their satisfaction in the ledger,
     * marks the order and the customer PAID, and frees up the table.
     *
     * @param restaurant the restaurant whose ledger and tables get updated
     * @return the result, with the amount paid if it worked
     */
    public ActionResult processNextPayment(Restaurant restaurant) {
        return performTask(() -> {
            Customer customer = oldestReadyToPay(restaurant).orElse(null);
            if (customer == null) {
                return ActionResult.fail("No customer is eligible to pay this turn.");
            }
            Table table = restaurant.findTableOf(customer).orElse(null);
            Order order = customer.getOrder().orElse(null);
            if (table == null || order == null || order.getStatus() != OrderStatus.SERVED
                    || order.getCustomer() != customer) {
                return ActionResult.fail("The customer needs a served order and an occupied table.");
            }
            restaurant.getLedger().recordPayment(order);
            order.markPaid();
            customer.markPaid();
            table.release();
            int billCents = order.getBillCents();
            return ActionResult.ok(String.format("Customer %d paid $%d.%02d.",
                    customer.getId(), billCents / 100, billCents % 100));
        });
    }

    /**
     * Finds the oldest seated customer who is ready to pay: earliest arrival turn, then lowest id.
     *
     * @param restaurant the restaurant to check
     * @return that customer, or empty if nobody is ready to pay
     */
    private Optional<Customer> oldestReadyToPay(Restaurant restaurant) {
        return restaurant.getSeatedCustomers().stream()
                .filter(Customer::isReadyToPay)
                .min(Comparator.comparingInt(Customer::getArrivalTurn).thenComparingInt(Customer::getId));
    }
}
