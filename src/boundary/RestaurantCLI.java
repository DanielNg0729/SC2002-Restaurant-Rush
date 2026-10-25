package boundary;

import control.GameController;
import control.GameResult;
import entity.StaffTask;
import java.util.List;

/**
 * Text-based UI for the game. It prints the game state and reads the player's choices, but doesn't hold any game rules
 * itself (that's the controller's job).
 *
 * @since Stage 1
 */
public class RestaurantCLI {

    private GameController controller;

    /**
     * Makes the CLI for a game. (This constructor isn't on the class diagram.)
     *
     * @param controller the controller running the game
     */
    public RestaurantCLI(GameController controller) {
        this.controller = controller;
    }

    /**
     * Starts the game through the controller and prints the result at the end.
     */
    public void run() {
        // TODO
    }

    /**
     * Prints what the player needs at the start of a turn: turn number, revenue, average paid satisfaction, the waiting
     * queue, who is at which table, each customer's satisfaction and order status, and what the staff can do.
     */
    public void displayTurnState() {
        // TODO
    }

    /**
     * Shows the Waiter's tasks plus a Wait option and returns what the player picks. Keeps asking until the input is
     * valid.
     *
     * @param tasks what the Waiter can do this turn
     * @return the task the player picked
     */
    public StaffTask chooseWaiterTask(List<StaffTask> tasks) {
        // TODO
        return null;
    }

    /**
     * Shows the Host's tasks plus a Wait option and returns what the player picks. Keeps asking until the input is
     * valid.
     *
     * @param tasks what the Host can do this turn
     * @return the task the player picked
     * @since Stage 3
     */
    public StaffTask chooseHostTask(List<StaffTask> tasks) {
        // TODO
        return null;
    }

    /**
     * Shows the Chef's tasks plus a Wait option and returns what the player picks. Keeps asking until the input is
     * valid.
     *
     * @param tasks what the Chef can do this turn
     * @return the task the player picked
     */
    public StaffTask chooseChefTask(List<StaffTask> tasks) {
        // TODO
        return null;
    }

    /**
     * Asks the player at the start of the turn if they want to turn on Happy Hour.
     *
     * @return true if the player wants Happy Hour now
     * @since Stage 3
     */
    public boolean chooseHappyHourActivation() {
        // TODO
        return false;
    }

    /**
     * Prints a message for the player, e.g. what an action did or why a choice didn't work.
     *
     * @param message the text to print
     */
    public void displayMessage(String message) {
        // TODO
    }

    /**
     * Prints the end-of-game summary: win or lose, how many were served, how many left unhappy, revenue in dollars,
     * average paid satisfaction (N/A if nobody paid) and how many turns were used.
     *
     * @param result the final result of the game
     */
    public void displayResult(GameResult result) {
        // TODO
    }
}
