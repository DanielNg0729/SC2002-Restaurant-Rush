package control;

import boundary.RestaurantCLI;
import entity.ActionResult;
import entity.Customer;
import entity.CustomerState;
import entity.HappyHour;
import entity.Restaurant;
import entity.Staff;
import entity.StaffTask;
import entity.StandardPricing;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/** Coordinates the 18-turn game. Rule decisions are handled here, not in the CLI. */
public class GameController {
    private static final int MAX_TURNS = 18;
    private static final int TARGET_PAID = 4;
    private static final int TARGET_REVENUE_CENTS = 4500;
    private static final int TARGET_AVG_SATISFACTION = 60;

    private final Restaurant restaurant;
    private final HappyHour happyHour;
    private final ArrivalSchedule arrivalSchedule;
    private final List<Customer> arrivals = new ArrayList<>();
    private RestaurantCLI ui;
    private int currentTurn;
    private boolean happyHourUsed;

    public GameController(Restaurant restaurant, ArrivalSchedule arrivalSchedule) {
        this.restaurant = restaurant;
        this.happyHour = new HappyHour(new StandardPricing());
        this.restaurant.setPricing(this.happyHour);
        this.arrivalSchedule = arrivalSchedule;
    }

    /** Connects the CLI to the controller without changing the existing constructors. */
    public void setCLI(RestaurantCLI ui) {
        if (ui == null) {
            throw new IllegalArgumentException("A CLI is required.");
        }
        this.ui = ui;
    }

    public Restaurant getRestaurant() { return restaurant; }
    public HappyHour getHappyHour() { return happyHour; }
    public int getCurrentTurn() { return currentTurn; }
    public int getServedCount() {
        return (int) arrivals.stream().filter(customer ->
                customer.getState() == CustomerState.SERVED
                        || customer.getState() == CustomerState.EATING
                        || customer.getState() == CustomerState.READY_TO_PAY
                        || customer.getState() == CustomerState.PAID).count();
    }

    /** Plays all turns, even if the targets have already been met. */
    public GameResult runGame() {
        if (ui == null) {
            throw new IllegalStateException("Connect a RestaurantCLI before starting.");
        }
        for (currentTurn = 1; currentTurn <= MAX_TURNS; currentTurn++) {
            runTurn();
        }
        currentTurn = MAX_TURNS;
        return checkResult();
    }

    private void runTurn() {
        // Cashier needs to know the restaurant before its start-of-turn eligibility snapshot.
        restaurant.getCashier().listAvailableTasks(restaurant);
        for (Staff staff : restaurant.getAllStaff()) {
            staff.startTurn();
        }
        offerHappyHour();
        processArrival();
        happyHour.startTurn(activeCustomers());
        ui.displayTurnState();
        processManagerChoices();
        processEatingAndPayment();
        processEndTurn();
        happyHour.endTurn();
    }

    private List<Customer> activeCustomers() {
        List<Customer> customers = new ArrayList<>(restaurant.getWaitingQueue().getCustomers());
        customers.addAll(restaurant.getSeatedCustomers());
        return customers;
    }

    private void offerHappyHour() {
        if (happyHour.isActive()) {
            ui.displayMessage("Happy Hour active: " + happyHour.getRemainingTurns() + " turn(s) remaining.");
        } else if (!happyHourUsed && ui.chooseHappyHourActivation()) {
            ActionResult result = activateHappyHour();
            ui.displayMessage(result.getMessage());
        }
    }

    private ActionResult activateHappyHour() {
        ActionResult result = happyHour.activate();
        if (result.isSuccess()) { happyHourUsed = true; }
        return result;
    }

    private void processArrival() {
        if (arrivalSchedule.isArrivalTurn(currentTurn)) {
            Customer customer = arrivalSchedule.createCustomer(currentTurn);
            arrivals.add(customer);
            restaurant.getWaitingQueue().join(customer);
            ui.displayMessage("Customer " + customer.getId() + " arrived.");
        }
    }

    private void processManagerChoices() {
        chooseAndRun(restaurant.getHost());
        chooseAndRun(restaurant.getWaiter());
        chooseAndRun(restaurant.getChef());
    }

    private void chooseAndRun(Staff staff) {
        while (staff.isAvailable()) {
            List<StaffTask> tasks = staff.listAvailableTasks(restaurant);
            StaffTask chosen;
            if (staff == restaurant.getHost()) {
                chosen = ui.chooseHostTask(tasks);
            } else if (staff == restaurant.getWaiter()) {
                chosen = ui.chooseWaiterTask(tasks);
            } else {
                chosen = ui.chooseChefTask(tasks);
            }
            if (chosen == null) {
                ui.displayMessage(staff.getRole() + " waits.");
                return;
            }
            ActionResult result = chosen.execute();
            ui.displayMessage(result.getMessage());
            if (result.isSuccess()) {
                return;
            }
            // An unsuccessful action does not use the task; present refreshed choices.
        }
    }

    private void processEatingAndPayment() {
        // Customer.endTurn() advances eating later in processEndTurn().
        // The Cashier's startTurn() snapshot prevents charging anyone who only
        // becomes READY_TO_PAY this turn.
        ActionResult payment = restaurant.getCashier().processNextPayment(restaurant);
        if (payment.isSuccess()) {
            ui.displayMessage(payment.getMessage());
        }
    }

    private void processEndTurn() {
        for (Customer customer : activeCustomers()) {
            customer.endTurn();
            if (customer.hasLeft()) {
                restaurant.removeLeavingCustomer(customer);
                ui.displayMessage("Customer " + customer.getId() + " left unhappy.");
            }
        }
    }

    private GameResult checkResult() {
        int paid = restaurant.getLedger().getPaidCount();
        int revenue = restaurant.getLedger().getRevenueCents();
        OptionalDouble average = restaurant.getLedger().getAverageSatisfaction();
        boolean victory = paid >= TARGET_PAID && revenue >= TARGET_REVENUE_CENTS
                && average.isPresent() && average.getAsDouble() >= TARGET_AVG_SATISFACTION;
        int served = getServedCount();
        return new GameResult(victory, paid, served, restaurant.getDepartureCount(),
                revenue, average.orElse(Double.NaN), MAX_TURNS);
    }
}
