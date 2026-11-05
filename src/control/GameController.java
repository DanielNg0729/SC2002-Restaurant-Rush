package control;

import boundary.RestaurantCLI;
import entity.ActionResult;
import entity.Customer;
import entity.HappyHour;
import entity.Restaurant;
import entity.SalesLedger;
import entity.Staff;
import entity.StaffTask;
import entity.StandardPricing;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.Function;

/**
 * Runs the game turn by turn and decides if the player wins or loses. All the game rules live here, while the UI stuff
 * is in RestaurantCLI.
 *
 * @since Stage 1
 */
public class GameController {

    private static final int MAX_TURNS = 18;

    /** Victory targets used by {@link #checkResult()}. (Not on the class diagram.) */
    private static final int TARGET_PAID = 4;
    private static final int TARGET_REVENUE_CENTS = 4500;
    private static final int TARGET_AVG_SATISFACTION = 60;

    private final Restaurant restaurant;
    private final HappyHour happyHour;
    private final ArrivalSchedule arrivalSchedule;
    private int currentTurn;

    /**
     * The CLI this controller calls for prompts and messages, as in the sequence diagram. (Not on the class diagram.)
     */
    private RestaurantCLI cli;

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
        this.restaurant.setPricing(this.happyHour);
        this.arrivalSchedule = arrivalSchedule;
    }

    /**
     * Connects the CLI. Game calls this while putting everything together, before {@link #runGame()}. (Not on the class
     * diagram.)
     *
     * @param cli the CLI that asks the player and shows messages
     */
    void setCli(RestaurantCLI cli) {
        this.cli = cli;
    }

    /**
     * The restaurant, so the CLI can show its state. (Not on the class diagram.)
     *
     * @return the restaurant we're playing in
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * How many Happy Hour turns are left, for the display. (Not on the class diagram.)
     *
     * @return turns left, or 0 if it's off
     */
    public int getHappyHourRemainingTurns() {
        return happyHour.getRemainingTurns();
    }

    /**
     * The turn being played, for the display. It stays on the last turn once the game is over. (Not on the class
     * diagram.)
     *
     * @return the current turn number
     */
    public int getCurrentTurn() {
        return currentTurn;
    }

    /**
     * How many customers have been served so far. A served customer can't leave and keeps their table until they pay,
     * so this is everyone who paid plus everyone at a table who isn't awaiting service. (Not on the class diagram.)
     *
     * @return the served count
     */
    public int getServedCount() {
        long servedNotPaid = restaurant.getSeatedCustomers().stream()
                .filter(customer -> !customer.getState().isAwaitingService())
                .count();
        return restaurant.getLedger().getPaidCount() + (int) servedNotPaid;
    }

    /**
     * Plays all {@code MAX_TURNS} turns one after another (the game never ends early) and returns the result.
     *
     * @return the result after the last turn
     */
    public GameResult runGame() {
        while (currentTurn < MAX_TURNS) {
            currentTurn++;
            runTurn();
        }
        return checkResult();
    }

    /**
     * Plays a single turn: reset the staff, ask about Happy Hour, arrivals, Happy Hour recovery, show the state, the
     * manager's choices (kitchen and serving), paying, then the end of the turn.
     */
    private void runTurn() {
        for (Staff staff : restaurant.getAllStaff()) {
            staff.startTurn();
        }
        if (cli.chooseHappyHourActivation()) {
            cli.displayMessage(activateHappyHour().getMessage());
        }
        processArrival();
        happyHour.startTurn(activeCustomers());
        cli.displayTurnState();
        processManagerChoices();
        processEatingAndPayment();
        processEndTurn();
    }

    /**
     * Step 1. If a customer is due this turn, create them and put them in the waiting queue.
     */
    private void processArrival() {
        if (arrivalSchedule.isArrivalTurn(currentTurn)) {
            Customer customer = arrivalSchedule.createCustomer(currentTurn);
            restaurant.getWaitingQueue().join(customer);
            cli.displayMessage("Customer " + customer.getId() + " arrived.");
        }
    }

    /**
     * Steps 2 and 3. Asks the player what each role should do (or Wait) and runs it, Host first, then Waiter, then
     * Chef. If a choice is invalid we say why and ask again, and the task isn't used up.
     */
    private void processManagerChoices() {
        runChosenTask(restaurant.getHost(), cli::chooseHostTask);
        runChosenTask(restaurant.getWaiter(), cli::chooseWaiterTask);
        runChosenTask(restaurant.getChef(), cli::chooseChefTask);
    }

    /**
     * Step 4. The Cashier takes at most one payment, starting with whoever has been ready to pay the longest. Eating
     * moves forward in {@code Customer.endTurn()} (see {@link #processEndTurn()}), and the Cashier's start-of-turn
     * snapshot means someone who becomes READY_TO_PAY this turn pays next turn at the earliest.
     */
    private void processEatingAndPayment() {
        ActionResult payment = restaurant.getCashier().processNextPayment(restaurant);
        if (payment.isSuccess()) {
            cli.displayMessage(payment.getMessage());
        }
    }

    /**
     * Step 5. Calls {@code endTurn()} on every customer in the queue or at a table, removes anyone whose satisfaction
     * hit 0, counts down Happy Hour and shows the end-of-turn summary.
     */
    private void processEndTurn() {
        for (Customer customer : activeCustomers()) {
            customer.endTurn();
            if (customer.hasLeft()) {
                restaurant.removeLeavingCustomer(customer);
                cli.displayMessage("Customer " + customer.getId() + " left unhappy.");
            }
        }
        happyHour.endTurn();
        cli.displayMessage(turnSummary());
    }

    /**
     * Turns on Happy Hour for the current turn if the player asks for it and it hasn't been used yet.
     *
     * @return whether it worked, and why not if it didn't
     * @since Stage 3
     */
    private ActionResult activateHappyHour() {
        return happyHour.activate();
    }

    /**
     * Step 6. Works out the final result. To win you need at least 4 paid customers, at least $45 in revenue and an
     * average paid satisfaction of 60 or more.
     *
     * @return the game result
     */
    private GameResult checkResult() {
        int paid = restaurant.getLedger().getPaidCount();
        int revenue = restaurant.getLedger().getRevenueCents();
        OptionalDouble average = restaurant.getLedger().getAverageSatisfaction();
        boolean victory = paid >= TARGET_PAID && revenue >= TARGET_REVENUE_CENTS
                && average.isPresent() && average.getAsDouble() >= TARGET_AVG_SATISFACTION;
        return new GameResult(victory, paid, getServedCount(), restaurant.getDepartureCount(),
                revenue, average.orElse(Double.NaN), MAX_TURNS);
    }

    /**
     * Asks the player for one staff member's task and runs it. A failed task doesn't use up their turn, so we show why
     * and ask again. (Not on the class diagram.)
     *
     * @param staff   the staff member acting
     * @param chooser the CLI prompt for that role, which returns null for Wait
     */
    private void runChosenTask(Staff staff, Function<List<StaffTask>, StaffTask> chooser) {
        while (staff.isAvailable()) {
            StaffTask chosen = chooser.apply(staff.listAvailableTasks(restaurant));
            if (chosen == null) {
                cli.displayMessage(staff.getRole() + " waits.");
                return;
            }
            cli.displayMessage(chosen.execute().getMessage());
        }
    }

    /**
     * Everyone in the queue or at a table. This is a copy, so customers can leave while we go through it. (Not on the
     * class diagram.)
     *
     * @return the active customers
     */
    private List<Customer> activeCustomers() {
        List<Customer> customers = new ArrayList<>(restaurant.getWaitingQueue().getCustomers());
        customers.addAll(restaurant.getSeatedCustomers());
        return customers;
    }

    /**
     * The numbers shown at the end of every turn. The average is N/A until someone has paid. (Not on the class
     * diagram.)
     *
     * @return the summary line
     */
    private String turnSummary() {
        SalesLedger ledger = restaurant.getLedger();
        int revenue = ledger.getRevenueCents();
        OptionalDouble average = ledger.getAverageSatisfaction();
        return String.format("End of turn %d: served %d, unhappy departures %d, revenue $%d.%02d, "
                + "average paid satisfaction %s.",
                currentTurn, getServedCount(), restaurant.getDepartureCount(), revenue / 100, revenue % 100,
                average.isPresent() ? String.format("%.1f", average.getAsDouble()) : "N/A");
    }
}
