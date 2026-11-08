package control;

import entity.StaffTask;
import java.util.List;

/**
 * What the game needs from a user interface: asking the player for choices and showing the game. The controller owns
 * this interface and only knows about it, so the game flow doesn't depend on any particular UI.
 *
 * @since Stage 1
 */
public interface GameView {

    /**
     * Shows the player the state of the game at the start of a turn.
     */
    void displayTurnState();

    /**
     * Shows the player the running totals at the end of a turn.
     */
    void displayTurnSummary();

    /**
     * Asks what the Waiter should do.
     *
     * @param tasks what the Waiter can do this turn
     * @return the task the player picked, or null for Wait
     */
    StaffTask chooseWaiterTask(List<StaffTask> tasks);

    /**
     * Asks what the Host should do.
     *
     * @param tasks what the Host can do this turn
     * @return the task the player picked, or null for Wait
     * @since Stage 3
     */
    StaffTask chooseHostTask(List<StaffTask> tasks);

    /**
     * Asks what the Chef should do.
     *
     * @param tasks what the Chef can do this turn
     * @return the task the player picked, or null for Wait
     */
    StaffTask chooseChefTask(List<StaffTask> tasks);

    /**
     * Asks if the player wants to turn on Happy Hour.
     *
     * @return true if the player wants Happy Hour now
     * @since Stage 3
     */
    boolean chooseHappyHourActivation();

    /**
     * Shows the player a message, e.g. what an action did or why a choice didn't work.
     *
     * @param message the text to show
     */
    void displayMessage(String message);
}
