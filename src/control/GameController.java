package control;

import entity.Restaurant;

/**
 * Runs the game turn by turn and decides if the player wins or loses. All the game rules live here, while the UI stuff
 * is in RestaurantCLI.
 *
 * @since Stage 1
 */
public class GameController {

    private static final int MAX_TURNS = 18;

    private final Restaurant restaurant;
    private final ArrivalSchedule arrivalSchedule;
    private int currentTurn;

    /**
     * Sets up a new game. (This constructor isn't on the class diagram.)
     *
     * @param restaurant      the restaurant we're playing in
     * @param arrivalSchedule when customers arrive
     */
    public GameController(Restaurant restaurant, ArrivalSchedule arrivalSchedule) {
        this.restaurant = restaurant;
        this.arrivalSchedule = arrivalSchedule;
    }

    /**
     * Plays all {@code MAX_TURNS} turns one after another (the game never ends early) and returns the result.
     *
     * @return the result after the last turn
     */
    public GameResult runGame() {
        // TODO
        return null;
    }

    /**
     * Plays a single turn: arrivals, the manager's choices (kitchen and serving), eating and paying, then the end of
     * the turn.
     */
    private void runTurn() {
        // TODO
    }

    /**
     * Step 1. If a customer is due this turn, create them and put them in the waiting queue.
     */
    private void processArrival() {
        // TODO
    }

    /**
     * Steps 2 and 3. Asks the player what each role should do (or Wait) and runs it, Waiter first and then Chef. If a
     * choice is invalid we say why and ask again, and the task isn't used up.
     */
    private void processManagerChoices() {
        // TODO
    }

    /**
     * Step 4. Customers served on an earlier turn finish eating, then the Cashier takes at most one payment, starting
     * with whoever has been ready to pay the longest.
     */
    private void processEatingAndPayment() {
        // TODO
    }

    /**
     * Step 5. Calls {@code endTurn()} on every customer in the queue or at a table, and removes anyone whose
     * satisfaction hit 0.
     */
    private void processEndTurn() {
        // TODO
    }

    /**
     * Step 6. Works out the final result. To win you need at least 4 paid customers, at least $45 in revenue and an
     * average paid satisfaction of 60 or more.
     *
     * @return the game result
     */
    private GameResult checkResult() {
        // TODO
        return null;
    }
}
