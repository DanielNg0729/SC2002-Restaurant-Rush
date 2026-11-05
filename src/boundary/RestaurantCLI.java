package boundary;

import control.GameController;
import control.GameResult;
import control.GameView;
import entity.Customer;
import entity.Restaurant;
import entity.StaffTask;
import entity.Table;
import java.util.List;
import java.util.Scanner;

/**
 * Text-based UI for the game. It prints the game state and reads the player's choices, but doesn't hold any game rules
 * itself (that's the controller's job).
 *
 * @since Stage 1
 */
public class RestaurantCLI implements GameView {

    private GameController controller;
    private final Scanner scanner = new Scanner(System.in);

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
        displayMessage("=== Restaurant Rush ===");
        displayResult(controller.runGame());
    }

    /**
     * Prints what the player needs at the start of a turn: turn number, revenue, average paid satisfaction, the waiting
     * queue, who is at which table, each customer's satisfaction and order status, and what the staff can do.
     */
    @Override
    public void displayTurnState() {
        Restaurant restaurant = controller.getRestaurant();
        int happyHourTurns = controller.getHappyHourRemainingTurns();
        System.out.println("\n========== TURN " + controller.getCurrentTurn() + " ==========");
        System.out.println("Revenue: " + dollars(restaurant.getLedger().getRevenueCents())
                + " | Paid: " + restaurant.getLedger().getPaidCount()
                + " | Average paid satisfaction: " + averageSatisfaction(restaurant));
        System.out.println("Happy Hour: " + (happyHourTurns > 0 ? happyHourTurns + " turn(s) remaining" : "inactive"));
        System.out.println("Waiting queue:");
        List<Customer> queue = restaurant.getWaitingQueue().getCustomers();
        if (queue.isEmpty()) {
            System.out.println("  (empty)");
        } else {
            for (Customer customer : queue) {
                System.out.println("  " + describe(customer));
            }
        }
        System.out.println("Tables:");
        for (Table table : restaurant.getTables()) {
            System.out.println("  Table " + table.getNumber() + ": "
                    + table.getOccupant().map(this::describe).orElse("free"));
        }
        System.out.println("Staff tasks available: Host "
                + restaurant.getHost().listAvailableTasks(restaurant).size()
                + ", Waiter " + restaurant.getWaiter().listAvailableTasks(restaurant).size()
                + ", Chef " + restaurant.getChef().listAvailableTasks(restaurant).size());
    }

    private String describe(Customer customer) {
        String order = customer.getOrder()
                .map(o -> o.getItem().getName() + " [" + o.getStatus() + "]")
                .orElse("no order");
        return "Customer " + customer.getId() + " (" + customer.getClass().getSimpleName()
                + ", satisfaction " + customer.getSatisfaction() + ", " + customer.getState()
                + ", " + order + ")";
    }

    /**
     * Shows the Waiter's tasks plus a Wait option and returns what the player picks. Keeps asking until the input is
     * valid.
     *
     * @param tasks what the Waiter can do this turn
     * @return the task the player picked, or null for Wait
     */
    @Override
    public StaffTask chooseWaiterTask(List<StaffTask> tasks) {
        return chooseTask("Waiter", tasks);
    }

    /**
     * Shows the Host's tasks plus a Wait option and returns what the player picks. Keeps asking until the input is
     * valid.
     *
     * @param tasks what the Host can do this turn
     * @return the task the player picked, or null for Wait
     * @since Stage 3
     */
    @Override
    public StaffTask chooseHostTask(List<StaffTask> tasks) {
        return chooseTask("Host", tasks);
    }

    /**
     * Shows the Chef's tasks plus a Wait option and returns what the player picks. Keeps asking until the input is
     * valid.
     *
     * @param tasks what the Chef can do this turn
     * @return the task the player picked, or null for Wait
     */
    @Override
    public StaffTask chooseChefTask(List<StaffTask> tasks) {
        return chooseTask("Chef", tasks);
    }

    /** Returns null to wait; invalid input is reprompted without consuming a staff action. */
    private StaffTask chooseTask(String role, List<StaffTask> tasks) {
        while (true) {
            System.out.println("\n" + role + " actions:");
            System.out.println("  0. Wait");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + tasks.get(i).getDescription());
            }
            int selection = readInteger("Choose " + role + " action: ");
            if (selection == 0) return null;
            if (selection >= 1 && selection <= tasks.size()) return tasks.get(selection - 1);
            displayMessage("Invalid action. Choose a number from 0 to " + tasks.size() + ".");
        }
    }

    /**
     * Asks the player at the start of the turn if they want to turn on Happy Hour.
     *
     * @return true if the player wants Happy Hour now
     * @since Stage 3
     */
    @Override
    public boolean chooseHappyHourActivation() {
        while (true) {
            System.out.print("Activate Happy Hour for this and next turn? (y/n): ");
            if (!scanner.hasNextLine()) throw new IllegalStateException("Input ended before game finished.");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) return true;
            if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) return false;
            displayMessage("Please enter y or n.");
        }
    }

    private int readInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) throw new IllegalStateException("Input ended before game finished.");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                displayMessage("Enter a whole number.");
            }
        }
    }

    /**
     * Prints a message for the player, e.g. what an action did or why a choice didn't work.
     *
     * @param message the text to print
     */
    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Prints the end-of-game summary: win or lose, how many were served, how many left unhappy, revenue in dollars,
     * average paid satisfaction (N/A if nobody paid) and how many turns were used.
     *
     * @param result the final result of the game
     */
    public void displayResult(GameResult result) {
        Restaurant restaurant = controller.getRestaurant();
        System.out.println("\n========== FINAL RESULT ==========");
        System.out.println(result.isVictory() ? "VICTORY" : "DEFEAT");
        System.out.println("Served: " + controller.getServedCount());
        System.out.println("Paid: " + restaurant.getLedger().getPaidCount());
        System.out.println("Unhappy departures: " + restaurant.getDepartureCount());
        System.out.println("Revenue: " + dollars(restaurant.getLedger().getRevenueCents()));
        System.out.println("Average paid satisfaction: " + averageSatisfaction(restaurant));
        System.out.println("Turns used: " + controller.getCurrentTurn());
    }

    private String averageSatisfaction(Restaurant restaurant) {
        return restaurant.getLedger().getAverageSatisfaction()
                .stream().mapToObj(v -> String.format("%.1f", v)).findFirst().orElse("N/A");
    }

    private String dollars(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }
}
