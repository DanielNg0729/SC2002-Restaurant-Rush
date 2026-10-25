package entity;

/**
 * What happened when a staff member tried to do something: whether it worked, plus a message for the player.
 *
 * @since Stage 1
 */
public class ActionResult {

    private final boolean success;
    private final String message;

    private ActionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Makes a result for an action that worked.
     *
     * @param message what happened
     * @return a success result
     */
    public static ActionResult ok(String message) {
        return new ActionResult(true, message);
    }

    /**
     * Makes a result for an action that didn't work. The staff member still has their task for the turn.
     *
     * @param reason why it couldn't be done
     * @return a failed result
     */
    public static ActionResult fail(String reason) {
        return new ActionResult(false, reason);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
