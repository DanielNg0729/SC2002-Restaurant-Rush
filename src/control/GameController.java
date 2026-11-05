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

/** Coordinates the 18-turn game. Rule decisions are handled here, not in the CLI. */
public class GameController {

    private static final int MAX_TURNS = 18;
    private static final int TARGET_PAID = 4;
    private static final int TARGET_REVENUE_CENTS = 4500;
    private static final int TARGET_AVG_SATISFACTION = 60;

    private final Restaurant restaurant;
    private final HappyHour happyHour;
    private final ArrivalSchedule arrivalSchedule;
    private int currentTurn;
    private RestaurantCLI cli;

    public GameController(Restaurant restaurant, ArrivalSchedule arrivalSchedule) {
        this.restaurant = restaurant;
        this.happyHour = new HappyHour(new StandardPricing());
        this.restaurant.setPricing(this.happyHour);
        this.arrivalSchedule = arrivalSchedule;
    }

    void setCli(RestaurantCLI cli) {
        this.cli = cli;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public int getHappyHourRemainingTurns() {
        return happyHour.getRemainingTurns();
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public int getServedCount() {
        long servedNotPaid = restaurant.getSeatedCustomers().stream()
                .filter(customer -> !customer.getState().isAwaitingService())
                .count();
        return restaurant.getLedger().getPaidCount() + (int) servedNotPaid;
    }

    public GameResult runGame() {
        while (currentTurn < MAX_TURNS) {
            currentTurn++;
            runTurn();
        }
        return checkResult();
    }

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

    private void processArrival() {
        if (arrivalSchedule.isArrivalTurn(currentTurn)) {
            Customer customer = arrivalSchedule.createCustomer(currentTurn);
            restaurant.getWaitingQueue().join(customer);
            cli.displayMessage("Customer " + customer.getId() + " arrived.");
        }
    }

    private void processManagerChoices() {
        runChosenTask(restaurant.getHost(), cli::chooseHostTask);
        runChosenTask(restaurant.getWaiter(), cli::chooseWaiterTask);
        runChosenTask(restaurant.getChef(), cli::chooseChefTask);
    }

    private void processEatingAndPayment() {
        ActionResult payment = restaurant.getCashier().processNextPayment(restaurant);
        if (payment.isSuccess()) {
            cli.displayMessage(payment.getMessage());
        }
    }

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

    private ActionResult activateHappyHour() {
        return happyHour.activate();
    }

    private GameResult checkResult() {
        int paid = restaurant.getLedger().getPaidCount();
        int revenue = restaurant.getLedger().getRevenueCents();
        OptionalDouble average = restaurant.getLedger().getAverageSatisfaction();
        boolean victory = paid >= TARGET_PAID && revenue >= TARGET_REVENUE_CENTS
                && average.isPresent() && average.getAsDouble() >= TARGET_AVG_SATISFACTION;
        return new GameResult(victory, paid, getServedCount(), restaurant.getDepartureCount(),
                revenue, average.orElse(Double.NaN), MAX_TURNS);
    }

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

    private List<Customer> activeCustomers() {
        List<Customer> customers = new ArrayList<>(restaurant.getWaitingQueue().getCustomers());
        customers.addAll(restaurant.getSeatedCustomers());
        return customers;
    }

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
