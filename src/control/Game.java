package control;

import boundary.RestaurantCLI;
import entity.Burger;
import entity.Menu;
import entity.Pasta;
import entity.Restaurant;
import entity.Salad;
import java.util.List;

/**
 * Puts the base-setting game together (menu, restaurant, arrival schedule, controller and CLI) so Main doesn't need
 * to know how any of it is built. This is the only class in {@code control} that knows the concrete CLI.
 *
 * @since Stage 1
 */
public final class Game {

    private static final List<Integer> ARRIVAL_TURNS = List.of(1, 4, 7, 10, 13, 16);

    private Game() {
    }

    /**
     * Builds the base setting and runs the game in the CLI.
     */
    public static void start() {
        Restaurant restaurant = new Restaurant(createMenu());
        ArrivalSchedule arrivalSchedule = new ArrivalSchedule(ARRIVAL_TURNS);
        GameController controller = new GameController(restaurant, arrivalSchedule);
        RestaurantCLI cli = new RestaurantCLI(controller);
        controller.setView(cli);
        cli.run();
    }

    /**
     * The base-setting menu, in display order.
     *
     * @return the menu
     */
    private static Menu createMenu() {
        return new Menu(List.of(
                new Salad(),
                new Burger(),
                new Pasta()));
    }
}
