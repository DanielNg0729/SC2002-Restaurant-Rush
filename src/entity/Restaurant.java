package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The restaurant floor. It owns the tables, waiting queue, sales ledger and staff for the whole game.
 *
 * @since Stage 1
 */
public class Restaurant {

    private static final int TABLE_COUNT = 2;

    private final Menu menu;
    private final List<Table> tables;
    private final WaitingQueue waitingQueue;
    private final SalesLedger ledger;
    private final Waiter waiter;
    private final Chef chef;
    private final Cashier cashier;
    private int departureCount = 0;

    /**
     * Sets up the restaurant with its own tables, queue, ledger and staff. The menu is built per stage and passed in.
     *
     * @param menu the menu customers order from
     */
    public Restaurant(Menu menu) {
        this.menu = menu;
        List<Table> created = new ArrayList<>();
        for (int number = 1; number <= TABLE_COUNT; number++) {
            created.add(new Table(number));
        }
        this.tables = List.copyOf(created);
        this.waitingQueue = new WaitingQueue();
        this.ledger = new SalesLedger();
        this.waiter = new Waiter("Waiter");
        this.chef = new Chef("Chef");
        this.cashier = new Cashier("Cashier");
    }

    public Menu getMenu() {
        return menu;
    }

    /**
     * All tables, sorted by table number.
     *
     * @return the tables (read-only list)
     */
    public List<Table> getTables() {
        return tables;
    }

    /**
     * Tables nobody is sitting at, sorted by table number.
     *
     * @return the free tables
     */
    public List<Table> getFreeTables() {
        // TODO
        return List.of();
    }

    /**
     * Finds the table {@code customer} is sitting at, if any.
     *
     * @param customer the customer to look for
     * @return their table, or empty if they aren't seated
     */
    public Optional<Table> findTableOf(Customer customer) {
        // TODO
        return Optional.empty();
    }

    /**
     * Customers currently sitting at tables, sorted by table number.
     *
     * @return the seated customers
     */
    public List<Customer> getSeatedCustomers() {
        // TODO
        return List.of();
    }

    /**
     * Orders from seated customers that haven't been paid or cancelled yet.
     *
     * @return the active orders
     */
    public List<Order> getActiveOrders() {
        // TODO
        return List.of();
    }

    public WaitingQueue getWaitingQueue() {
        return waitingQueue;
    }

    public SalesLedger getLedger() {
        return ledger;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public Chef getChef() {
        return chef;
    }

    public Cashier getCashier() {
        return cashier;
    }

    /**
     * Every staff member, in the order they act each turn (Waiter, Chef, Cashier).
     *
     * @return the staff (read-only list)
     */
    public List<Staff> getAllStaff() {
        return List.of(waiter, chef, cashier);
    }

    /**
     * Deals with a customer who leaves unhappy. Cancels their order if it isn't finished, takes them out of the queue
     * or frees their table, and adds one to the departure count.
     *
     * @param customer the customer who's leaving
     */
    public void removeLeavingCustomer(Customer customer) {
        // TODO
    }

    public int getDepartureCount() {
        return departureCount;
    }
}
