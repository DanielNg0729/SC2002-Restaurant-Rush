package entity;

import java.util.List;
import java.util.function.Supplier;

/**
 * Base class for staff. Each staff member can do at most one task per turn and can list the tasks they're able to do.
 *
 * @since Stage 1
 */
public abstract class Staff {

    private final String name;
    private boolean actedThisTurn = false;

    /**
     * Makes a staff member who hasn't acted yet.
     *
     * @param name the staff member's name
     */
    protected Staff(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * True if this staff member hasn't done a task yet this turn.
     *
     * @return true if they can still act this turn
     */
    public final boolean isAvailable() {
        // TODO
        return false;
    }

    /**
     * Resets the staff member at the start of a turn so they can act again.
     */
    public final void startTurn() {
        // TODO
    }

    /**
     * Runs {@code action} if the staff member hasn't acted yet this turn. Only a successful result uses up the turn, so
     * an invalid choice can be tried again. Trying to act twice just gives back a failed result.
     *
     * @param action the action to run
     * @return the action's result, or a failure if they've already acted
     */
    protected final ActionResult performTask(Supplier<ActionResult> action) {
        // TODO
        return null;
    }

    /**
     * Role name shown to the player, like "Waiter".
     *
     * @return the role name
     */
    public abstract String getRole();

    /**
     * Tasks this staff member can actually do given the restaurant's current state.
     *
     * @param restaurant the restaurant to check
     * @return the available tasks, empty if there's nothing to do
     */
    public abstract List<StaffTask> listAvailableTasks(Restaurant restaurant);
}
