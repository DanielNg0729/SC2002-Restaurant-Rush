package entity;

import java.util.List;

/**
 * The Host. Can seat one queued customer per turn, on top of whatever the Waiter does.
 *
 * @since Stage 3
 */
public class Host extends FrontOfHouseStaff {

    /**
     * Package-private because only Restaurant creates staff.
     *
     * @param name the Host's name
     */
    Host(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "Host";
    }

    /**
     * What the Host can do this turn, which is seating a queued customer at a free table.
     *
     * @param restaurant the restaurant to check
     * @return the available tasks
     */
    @Override
    public List<StaffTask> listAvailableTasks(Restaurant restaurant) {
        return seatingTasks(restaurant);
    }
}
