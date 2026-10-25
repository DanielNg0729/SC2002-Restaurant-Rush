package entity;

/**
 * A food critic. Always orders the most expensive thing on the menu and loses 12 satisfaction for every turn they're
 * kept waiting.
 *
 * @since Stage 2
 */
public class CriticCustomer extends Customer {

    private static final int WAITING_LOSS = 12;

    /**
     * Makes a critic customer.
     *
     * @param id          arrival number, starting from 1
     * @param arrivalTurn the turn they arrive on
     */
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

    /**
     * Picks the most expensive item on the menu. If there's a tie, takes whichever comes first on the menu.
     *
     * @param menu the menu to pick from
     * @return the chosen item
     */
    @Override
    public MenuItem chooseItem(Menu menu) {
        // TODO
        return null;
    }
}
