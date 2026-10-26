package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Customers who have arrived and are waiting for a table, in the order they arrived.
 *
 * @since Stage 1
 */
public class WaitingQueue {

    private List<Customer> customers;

    /**
     * Starts empty. Package-private because only Restaurant creates the queue.
     */
    WaitingQueue() {
        this.customers = new ArrayList<>();
    }

    /**
     * Adds a customer who just arrived to the back of the queue.
     *
     * @param customer the customer who arrived
     */
    public void join(Customer customer) {
        customers.add(customer);
    }

    /**
     * Takes {@code customer} out of the queue when they get seated or leave.
     *
     * @param customer the customer to remove
     * @return true if they were actually in the queue
     */
    boolean remove(Customer customer) {
        return customers.remove(customer);
    }

    /**
     * Checks if {@code customer} is waiting in the queue.
     *
     * @param customer the customer to look for
     * @return true if they're in the queue
     */
    public boolean contains(Customer customer) {
        return customers.contains(customer);
    }

    /**
     * Customers in the queue, in arrival order.
     *
     * @return a read-only view of the queue
     */
    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    /**
     * True if nobody is waiting.
     *
     * @return true if the queue is empty
     */
    public boolean isEmpty() {
        return customers.isEmpty();
    }

    /**
     * How many customers are waiting.
     *
     * @return the queue length
     */
    public int size() {
        return customers.size();
    }
}
