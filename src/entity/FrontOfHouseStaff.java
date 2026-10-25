package entity;

import java.util.List;

/**
 * Staff who work out front and can seat customers from the queue at free tables.
 *
 * @since Stage 1
 */
public abstract class FrontOfHouseStaff extends Staff {

    /**
     * Makes a front-of-house staff member.
     *
     * @param name the staff member's name
     */
    protected FrontOfHouseStaff(String name) {
        super(name);
    }

    /**
     * Seats a queued customer at a free table (this uses up the turn's task). Takes them out of the queue, puts them at
     * the table and marks them SEATED. Fails with a reason if the customer isn't in the queue or the table is already
     * taken.
     *
     * @param customer the customer to seat
     * @param queue    the waiting queue they're in
     * @param table    the table to put them at
     * @return the result of trying to seat them
     */
    public final ActionResult seat(Customer customer, WaitingQueue queue, Table table) {
        // TODO
        return null;
    }

    /**
     * All the seating moves possible right now, one for each queued customer who can go to a free table.
     *
     * @param restaurant the restaurant to check
     * @return the seating tasks (empty if nobody is waiting or no table is free)
     */
    protected List<StaffTask> seatingTasks(Restaurant restaurant) {
        // TODO
        return List.of();
    }
}
