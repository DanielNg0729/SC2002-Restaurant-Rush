package entity;

import java.util.List;
import java.util.Objects;

public class CriticCustomer extends Customer {

    private static final int WAITING_LOSS = 12;

    public CriticCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    protected String getTypeName() {
        return "Critic";
    }

    @Override
    protected int getWaitingLoss() {
        return WAITING_LOSS;
    }

    @Override
    public MenuItem chooseItem(Menu menu) {
        Objects.requireNonNull(menu, "Menu must not be null.");

        List<MenuItem> items = menu.getItems();

        if (items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot choose an item from an empty menu."
            );
        }

        MenuItem mostExpensive = items.get(0);

        for (int i = 1; i < items.size(); i++) {
            MenuItem candidate = items.get(i);

            if (candidate.getPriceCents()
                    > mostExpensive.getPriceCents()) {
                mostExpensive = candidate;
            }
        }

        return mostExpensive;
    }
}
