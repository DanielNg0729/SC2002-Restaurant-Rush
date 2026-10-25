package control;

import entity.Customer;
import java.util.List;

/**
 * Fixed list of turns when customers arrive, so every game plays out the same way. It also creates the customer for
 * each arrival.
 *
 * @since Stage 1
 */
public class ArrivalSchedule {

    private final List<Integer> arrivalTurns;

    /**
     * Makes the schedule. (This constructor isn't on the class diagram.)
     *
     * @param arrivalTurns turns where one customer shows up, in increasing order
     */
    public ArrivalSchedule(List<Integer> arrivalTurns) {
        this.arrivalTurns = List.copyOf(arrivalTurns);
    }

    /**
     * Checks if a customer is due to arrive at the start of {@code turn}.
     *
     * @param turn turn number, starting from 1
     * @return true if someone arrives this turn
     */
    public boolean isArrivalTurn(int turn) {
        // TODO
        return false;
    }

    /**
     * Creates the customer who arrives on {@code turn}. The 1st arrival gets id 1, the 2nd gets id 2, and so on.
     *
     * @param turn an arrival turn
     * @return the new customer, waiting with full satisfaction
     */
    public Customer createCustomer(int turn) {
        // TODO
        return null;
    }
}
