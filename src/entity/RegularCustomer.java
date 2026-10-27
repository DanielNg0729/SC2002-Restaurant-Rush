package entity;

public class RegularCustomer extends Customer {

    private static final int WAITING_LOSS = 8;

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
