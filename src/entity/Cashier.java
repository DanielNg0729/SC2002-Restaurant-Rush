package entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * The Cashier. Takes at most one payment per turn on their own, starting with whoever has been ready to pay the
 * longest. The game must check payments every turn, including the first turn when
 * nobody is ready, and reset staff before any customer or staff actions. This binds
 * the restaurant before later startTurn snapshots capture eligible customers.
 *
 * @since Stage 1
 */
public class Cashier extends Staff {

    private Restaurant restaurantForPayments;
    private final List<Customer> readyToPayInOrder = new ArrayList<>();

    /**
     * Snapshot before anyone acts. Customers becoming ready later this turn are
     * deliberately excluded until the next startTurn call.
     */
    @Override
    protected void onStartTurn() {
        if (restaurantForPayments != null) {
            refreshReadyToPay();
        }
    }

    private void refreshReadyToPay() {
        readyToPayInOrder.removeIf(customer -> !customer.isReadyToPay()
                || restaurantForPayments.findTableOf(customer).isEmpty());
        restaurantForPayments.getSeatedCustomers().stream()
                .filter(Customer::isReadyToPay)
                .filter(customer -> !readyToPayInOrder.contains(customer))
                .sorted(Comparator.comparingInt(Customer::getArrivalTurn)
                        .thenComparingInt(Customer::getId))
                .forEach(readyToPayInOrder::add);
    }

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
     * Takes payment from the customer who has been READY_TO_PAY the longest (since an earlier turn). Records the locked
     * bill and their satisfaction in the ledger, marks the order and the customer PAID, and frees up the table.
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
            readyToPayInOrder.remove(customer);
            return ActionResult.ok("Customer " + customer.getId() + " paid "
                    + order.getBillCents() + " cents.");
        });
    }

    /**
     * Finds the customer who has been waiting to pay the longest, if there is one.
     *
     * @param restaurant the restaurant to check
     * @return that customer, or empty if nobody is ready to pay
     */
    private Optional<Customer> oldestReadyToPay(Restaurant restaurant) {
        if (restaurantForPayments == null) {
            // Bind on the first payment check (including a check with nobody ready).
            restaurantForPayments = restaurant;
        } else if (restaurantForPayments != restaurant) {
            throw new IllegalArgumentException("A Cashier belongs to one restaurant.");
        }
        return readyToPayInOrder.stream()
                .filter(Customer::isReadyToPay)
                .filter(customer -> restaurant.findTableOf(customer).isPresent())
                .findFirst();
    }
}
