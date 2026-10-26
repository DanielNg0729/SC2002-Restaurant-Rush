package control;

import entity.CriticCustomer;
import entity.Customer;
import entity.RegularCustomer;
import entity.VIPCustomer;
import java.util.List;

/**
 * Fixed list of turns when customers arrive, so every game plays out the same way. It also creates the customer for
 * each arrival. The 2nd arrival is a VIP and the 5th is a Critic, everyone else is Regular.
 *
 * @since Stage 1
 */
public class ArrivalSchedule {

    /**
     * Arrival number of the VIP. A VIP ordering a Burger shows the 10% discount ($13.50). (Not on the class diagram.)
     *
     * @since Stage 2
     */
    private static final int VIP_ARRIVAL = 2;

    /**
     * Arrival number of the Critic. They arrive late and order the most expensive item, so the player has to rush
     * them to get paid by turn 18. (Not on the class diagram.)
     *
     * @since Stage 2
     */
    private static final int CRITIC_ARRIVAL = 5;

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
        return arrivalTurns.contains(turn);
    }

    /**
     * Creates the customer who arrives on {@code turn}. The 1st arrival gets id 1, the 2nd gets id 2, and so on.
     * Arrival {@code VIP_ARRIVAL} is a VIP, arrival {@code CRITIC_ARRIVAL} is a Critic and the rest are Regular.
     *
     * @param turn an arrival turn
     * @return the new customer, waiting with full satisfaction
     * @throws IllegalArgumentException if nobody arrives on {@code turn}
     */
    public Customer createCustomer(int turn) {
        if (!isArrivalTurn(turn)) {
            throw new IllegalArgumentException("Nobody arrives on turn " + turn);
        }
        int id = arrivalTurns.indexOf(turn) + 1;
        if (id == VIP_ARRIVAL) {
            return new VIPCustomer(id, turn);
        }
        if (id == CRITIC_ARRIVAL) {
            return new CriticCustomer(id, turn);
        }
        return new RegularCustomer(id, turn);
    }
}
