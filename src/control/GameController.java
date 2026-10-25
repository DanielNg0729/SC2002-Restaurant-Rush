package control;

import entity.ActionResult;
import entity.HappyHour;
import entity.Restaurant;
import entity.StandardPricing;

/**
 * Runs the game turn by turn and decides if the player wins or loses. All the game rules live here, while the UI stuff
 * is in RestaurantCLI.
 *
 * @since Stage 1
 */
public class GameController {

    private static final int MAX_TURNS = 18;

    private final Restaurant restaurant;
    private final HappyHour happyHour;
    private final ArrivalSchedule arrivalSchedule;
    private int currentTurn;

    /**
     * Sets up a new game. Happy Hour starts off unused and sits on top of standard pricing. (This constructor isn't on
     * the class diagram.)
     *
     * @param restaurant      the restaurant we're playing in
     * @param arrivalSchedule when customers arrive
     */
    public GameController(Restaurant restaurant, ArrivalSchedule arrivalSchedule) {
        this.restaurant = restaurant;
        this.happyHour = new HappyHour(new StandardPricing());
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
     * Turns on Happy Hour for the current turn if the player asks for it and it hasn't been used yet.
     *
     * @return whether it worked, and why not if it didn't
     * @since Stage 3
     */
    private ActionResult activateHappyHour() {
        // TODO
        return null;
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
