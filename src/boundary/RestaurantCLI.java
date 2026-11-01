package boundary;

import control.GameController;
import control.GameResult;
import entity.Customer;
import entity.HappyHour;
import entity.Order;
import entity.Restaurant;
import entity.StaffTask;
import entity.Table;
import java.util.List;
import java.util.Scanner;

/** Console-only presentation and input. Restaurant rules stay in GameController and entities. */
public class RestaurantCLI {
    private final GameController controller;
    private final Scanner scanner = new Scanner(System.in);

    public RestaurantCLI(GameController controller) {
        this.controller = controller;
        controller.setCLI(this);
    }

    public void run() {
        displayMessage("=== Restaurant Rush ===");
        displayResult(controller.runGame());
    }

    public void displayTurnState() {
        Restaurant restaurant = controller.getRestaurant();
        HappyHour happyHour = controller.getHappyHour();
        System.out.println("\n========== TURN " + controller.getCurrentTurn() + "/18 ==========");
        System.out.println("Revenue: " + dollars(restaurant.getLedger().getRevenueCents())
                + " | Paid: " + restaurant.getLedger().getPaidCount()
                + " | Average paid satisfaction: "
                + restaurant.getLedger().getAverageSatisfaction()
                        .stream().mapToObj(v -> String.format("%.1f", v)).findFirst().orElse("N/A"));
        System.out.println("Happy Hour: " + (happyHour.isActive()
                ? happyHour.getRemainingTurns() + " turn(s) remaining" : "inactive"));
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

    public StaffTask chooseWaiterTask(List<StaffTask> tasks) {
        return chooseTask("Waiter", tasks);
    }

    public StaffTask chooseHostTask(List<StaffTask> tasks) {
        return chooseTask("Host", tasks);
    }

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

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayResult(GameResult result) {
        Restaurant restaurant = controller.getRestaurant();
        System.out.println("\n========== FINAL RESULT ==========");
        System.out.println(result.isVictory() ? "VICTORY" : "DEFEAT");
        System.out.println("Served: " + controller.getServedCount());
        System.out.println("Paid: " + restaurant.getLedger().getPaidCount());
        System.out.println("Unhappy departures: " + restaurant.getDepartureCount());
        System.out.println("Revenue: " + dollars(restaurant.getLedger().getRevenueCents()));
        System.out.println("Average paid satisfaction: "
                + restaurant.getLedger().getAverageSatisfaction()
                        .stream().mapToObj(v -> String.format("%.1f", v)).findFirst().orElse("N/A"));
        System.out.println("Turns used: " + controller.getCurrentTurn());
    }

    private String dollars(int cents) {
        return String.format("$%d.%02d", cents / 100, cents % 100);
    }
}
