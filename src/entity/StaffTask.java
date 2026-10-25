package entity;

import java.util.function.Supplier;

/**
 * One thing a staff member can do this turn. It has a description for the player and only runs if the player picks it.
 *
 * @since Stage 1
 */
public class StaffTask {

    private final String description;
    private final Supplier<ActionResult> action;

    /**
     * Package-private because only staff members create tasks.
     *
     * @param description what the task does (shown to the player)
     * @param action      what to run when the task is picked
     */
    StaffTask(String description, Supplier<ActionResult> action) {
        this.description = description;
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Runs the action and returns what happened.
     *
     * @return the result of the action
     */
    public ActionResult execute() {
        // TODO
        return null;
    }
}
