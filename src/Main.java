import boundary.RestaurantCLI;
import control.ArrivalSchedule;
import control.GameController;
import entity.Burger;
import entity.ComboMeal;
import entity.Menu;
import entity.Pasta;
import entity.Restaurant;
import entity.Salad;
import java.util.List;

/**
 * Starting point of the program. Sets up the menu, the restaurant and the arrival schedule, then hands over to the CLI.
 *
 * @since Stage 1
 */
public class Main {

    /**
     * Sets everything up and starts the game.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Menu menu = new Menu(List.of(
                new Salad(),
                new Burger(),
                new Pasta(),
                new ComboMeal(Salad::new, Pasta::new)));
        Restaurant restaurant = new Restaurant(menu);
        ArrivalSchedule arrivalSchedule = new ArrivalSchedule(List.of(1, 4, 7, 10, 13, 16));
        GameController controller = new GameController(restaurant, arrivalSchedule);
        new RestaurantCLI(controller).run();
    }
}
