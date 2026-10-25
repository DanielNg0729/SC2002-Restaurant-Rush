package entity;

/**
 * A normal customer. Pays full price and loses 8 satisfaction for every turn they're kept waiting.
 *
 * @since Stage 1
 */
public class RegularCustomer extends Customer {

    private static final int WAITING_LOSS = 8;

    /**
     * Makes a regular customer.
     *
     * @param id          arrival number, starting from 1
     * @param arrivalTurn the turn they arrive on
     */
    public RegularCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    public String getTypeName() {
        return "Regular";
    }

    @Override
    protected int getWaitingLoss() {
        return WAITING_LOSS;
    }
}
