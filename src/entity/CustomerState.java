package entity;

public enum CustomerState {
    WAITING,
    SEATED,
    ORDERED,
    SERVED,
    EATING,
    READY_TO_PAY,
    PAID,
    LEFT;

    public boolean isAwaitingService() {
        return this == WAITING
                || this == SEATED
                || this == ORDERED;
    }

    public boolean canMoveTo(CustomerState next) {
        if (next == null) {
            return false;
        }

        if (next == LEFT) {
            return isAwaitingService();
        }

        switch (this) {
            case WAITING:
                return next == SEATED;

            case SEATED:
                return next == ORDERED;

            case ORDERED:
                return next == SERVED;

            case SERVED:
                return next == EATING;

            case EATING:
                return next == READY_TO_PAY;

            case READY_TO_PAY:
                return next == PAID;

            case PAID:
            case LEFT:
            default:
                return false;
        }
    }
}
