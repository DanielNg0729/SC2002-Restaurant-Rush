package entity;

import java.util.OptionalDouble;

/**
 * Keeps track of paid bills and how satisfied each customer was when they paid. Used for total revenue and the average
 * paid satisfaction.
 *
 * @since Stage 1
 */
public class SalesLedger {

    private int revenueCents = 0;
    private int paidCount = 0;
    private int satisfactionSum = 0;

    /**
     * Starts empty. Package-private because only Restaurant creates the ledger.
     */
    SalesLedger() {
    }

    /**
     * Records a payment. Adds the order's locked bill to revenue, counts one more paid customer and adds their
     * satisfaction at the moment they paid.
     *
     * @param order the order being paid for
     */
    void recordPayment(Order order) {
        revenueCents += order.getBillCents();
        paidCount++;
        satisfactionSum += order.getCustomer().getSatisfaction();
    }

    /**
     * Revenue from paid orders only.
     *
     * @return revenue in cents
     */
    public int getRevenueCents() {
        return revenueCents;
    }

    public int getPaidCount() {
        return paidCount;
    }

    /**
     * Average satisfaction of paying customers (taken when they paid). Empty if nobody has paid yet, which shows up as
     * N/A.
     *
     * @return average paid satisfaction
     */
    public OptionalDouble getAverageSatisfaction() {
        if (paidCount == 0) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of((double) satisfactionSum / paidCount);
    }
}
