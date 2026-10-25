package entity;

/**
 * A VIP customer. Pays 90% of the price and only loses 5 satisfaction per turn of waiting.
 *
 * @since Stage 2
 */
public class VIPCustomer extends Customer {

    private static final int WAITING_LOSS = 5;
    private static final int PRICE_PERCENT = 90;

    /**
     * Makes a VIP customer.
     *
     * @param id          arrival number, starting from 1
     * @param arrivalTurn the turn they arrive on
     */
    public VIPCustomer(int id, int arrivalTurn) {
        super(id, arrivalTurn);
    }

    @Override
    protected String getTypeName() {
        return "VIP";
    }

    @Override
    protected int getWaitingLoss() {
        return WAITING_LOSS;
    }

    /**
     * VIPs pay 90%, so they get 10% off.
     *
     * @return 90
     */
    @Override
    public int getPricePercent() {
        return PRICE_PERCENT;
    }
}
