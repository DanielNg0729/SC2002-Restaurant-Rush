package entity;

import java.util.List;
import java.util.function.Supplier;

/**
 * A combo of two dishes. The combo creates and owns its dishes, costs 90% of their total price and needs the prep units
 * of both added together.
 *
 * @since Stage 3
 */
public class ComboMeal implements MenuItem {

    private static final int PRICE_PERCENT = 90;

    private final List<Dish> components;

    /**
     * Makes a combo that creates its own two dishes, for example {@code new ComboMeal(Salad::new, Pasta::new)}.
     *
     * @param first  makes the first dish
     * @param second makes the second dish
     */
    public ComboMeal(Supplier<Dish> first, Supplier<Dish> second) {
        this.components = List.of(first.get(), second.get());
    }

    /**
     * Name made from both dishes, like "Combo (Salad + Pasta)".
     *
     * @return the combo's display name
     */
    @Override
    public String getName() {
        // TODO
        return null;
    }

    /**
     * {@code PRICE_PERCENT}% of the two dishes' total, rounded to the nearest cent. Salad + Pasta comes out to 2430
     * cents.
     *
     * @return the combo price in cents
     */
    @Override
    public int getPriceCents() {
        // TODO
        return 0;
    }

    /**
     * Prep units of both dishes added up (Salad + Pasta = 3).
     *
     * @return total prep units
     */
    @Override
    public int getPrepUnits() {
        // TODO
        return 0;
    }
}
