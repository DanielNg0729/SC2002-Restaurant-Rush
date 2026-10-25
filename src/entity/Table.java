package entity;

import java.util.Optional;

/**
 * A numbered table. Seats one customer at most, until they pay or leave.
 *
 * @since Stage 1
 */
public class Table {

    private final int number;
    private Customer occupant;

    /**
     * Makes an empty table. Package-private because only Restaurant creates tables.
     *
     * @param number table number, starting from 1
     */
    Table(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    /**
     * True if nobody is sitting here.
     *
     * @return true if the table is free
     */
    public boolean isFree() {
        // TODO
        return false;
    }

    /**
     * The customer sitting at this table, if any.
     *
     * @return the occupant, or empty if the table is free
     */
    public Optional<Customer> getOccupant() {
        return Optional.ofNullable(occupant);
    }

    /**
     * Puts {@code customer} at this table (it has to be free). The table stays taken until they pay or leave.
     *
     * @param customer the customer to seat
     */
    void seat(Customer customer) {
        // TODO
    }

    /**
     * Frees up the table when the customer pays or leaves.
     */
    void release() {
        // TODO
    }
}
