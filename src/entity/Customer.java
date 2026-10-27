package entity;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class Customer {

    private static final int MAX_SATISFACTION = 100;

    private final int id;
    private final int arrivalTurn;

    private int satisfaction = MAX_SATISFACTION;
    private CustomerState state = CustomerState.WAITING;
    private Order order;

    protected Customer(int id, int arrivalTurn) {
        if (id < 1) {
            throw new IllegalArgumentException(
                    "Customer id must be at least 1."
            );
        }

        if (arrivalTurn < 1) {
            throw new IllegalArgumentException(
                    "Arrival turn must be at least 1."
            );
        }

        this.id = id;
        this.arrivalTurn = arrivalTurn;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTurn() {
        return arrivalTurn;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public CustomerState getState() {
        return state;
    }

    public Optional<Order> getOrder() {
        return Optional.ofNullable(order);
    }

    public boolean isReadyToPay() {
        return state == CustomerState.READY_TO_PAY;
    }

    public boolean hasLeft() {
        return state == CustomerState.LEFT;
    }

    
    public MenuItem chooseItem(Menu menu) {
        Objects.requireNonNull(menu, "Menu must not be null.");

        List<MenuItem> items = menu.getItems();

        if (items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot choose an item from an empty menu."
            );
        }

        int index = (id - 1) % items.size();
        return items.get(index);
    }

    
    public int getPricePercent() {
        return 100;
    }

    
    public final void endTurn() {
        if (state.isAwaitingService()) {
            applyWaitingLoss();
        } else if (state == CustomerState.SERVED
                || state == CustomerState.EATING) {
            advanceEating();
        }
    }

    
    final void recoverIfWaiting(int points) {
        if (points < 0) {
            throw new IllegalArgumentException(
                    "Recovery points must not be negative."
            );
        }

        if (state.isAwaitingService()) {
            adjustSatisfaction(points);
        }
    }

    final void markSeated() {
        moveTo(CustomerState.SEATED);
    }

    final void placeOrder(Order order) {
        Objects.requireNonNull(order, "Order must not be null.");

        if (order.getCustomer() != this) {
            throw new IllegalArgumentException(
                    "The order belongs to a different customer."
            );
        }

        if (this.order != null) {
            throw new IllegalStateException(
                    "This customer already has an order."
            );
        }

        
        moveTo(CustomerState.ORDERED);
        this.order = order;
    }

    final void markServed() {
        moveTo(CustomerState.SERVED);
    }

    final void markPaid() {
        moveTo(CustomerState.PAID);
    }

    protected abstract String getTypeName();

    protected abstract int getWaitingLoss();

    
    private void advanceEating() {
        if (state == CustomerState.SERVED) {
            moveTo(CustomerState.EATING);
        } else if (state == CustomerState.EATING) {
            moveTo(CustomerState.READY_TO_PAY);
        }
    }

    private void applyWaitingLoss() {
        adjustSatisfaction(-getWaitingLoss());

        if (satisfaction == 0) {
            moveTo(CustomerState.LEFT);
        }
    }

    private void moveTo(CustomerState next) {
        if (!state.canMoveTo(next)) {
            throw new IllegalStateException(
                    "Illegal customer state transition: "
                            + state + " -> " + next
            );
        }

        state = next;
    }

    private void adjustSatisfaction(int delta) {
        long adjusted = (long) satisfaction + delta;

        satisfaction = (int) Math.max(
                0L,
                Math.min((long) MAX_SATISFACTION, adjusted)
        );
    }
}
