package entity;

public class VIPCustomer extends Customer {

    private static final int WAITING_LOSS = 5;
    private static final int PRICE_PERCENT = 90;

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

    @Override
    public int getPricePercent() {
        return PRICE_PERCENT;
    }
}
