package entity;

import java.util.List;

/**
 * List of items customers can order, in display order. It's built for each stage and shared with the restaurant.
 *
 * @since Stage 1
 */
public class Menu {

    private final List<MenuItem> items;

    /**
     * Makes a menu with the given items, kept in the same order.
     *
     * @param items the menu items (needs at least one)
     */
    public Menu(List<MenuItem> items) {
        this.items = List.copyOf(items);
    }

    /**
     * Menu items in display order.
     *
     * @return the items (read-only list)
     */
    public List<MenuItem> getItems() {
        return items;
    }
}
