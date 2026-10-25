package entity;

/**
 * All the stages a customer goes through, from arriving to paying (or leaving unhappy).
 *
 * @since Stage 1
 */
public enum CustomerState {
    WAITING,
    SEATED,
    ORDERED,
    SERVED,
    EATING,
    READY_TO_PAY,
    PAID,
    LEFT;

    /**
     * True while the customer is still waiting for a table or for food (WAITING, SEATED or ORDERED). These are the only
     * states that lose satisfaction at the end of a turn.
     *
     * @return true if the customer is still waiting to be served
     */
    public boolean isAwaitingService() {
        // TODO
        return false;
    }

    /**
     * Checks if we're allowed to go from this state to {@code next}. A customer can only move one step forward through
     * WAITING, SEATED, ORDERED, SERVED, EATING, READY_TO_PAY, PAID, or go to LEFT while they're still waiting for
     * service.
     *
     * @param next the state to move to
     * @return true if the move is allowed
     */
    public boolean canMoveTo(CustomerState next) {
        // TODO
        return false;
    }
}
