package control;

import java.util.OptionalDouble;

/**
 * Final outcome of a game (win or lose) plus the numbers we show at the end.
 *
 * @since Stage 1
 */
public class GameResult {

    private final boolean victory;
    private final int paidCount;
    private final int servedCount;
    private final int departureCount;
    private final int revenueCents;
    private final double averageSatisfaction;
    private final int turnsUsed;

    /**
     * Only GameController makes these, so the constructor is package-private.
     *
     * @param victory             true if all three targets were hit
     * @param paidCount           how many customers paid
     * @param servedCount         how many customers got served
     * @param departureCount      how many customers left unhappy
     * @param revenueCents        money from paid orders, in cents
     * @param averageSatisfaction average satisfaction at the time of payment
     * @param turnsUsed           how many turns were played
     */
    GameResult(boolean victory, int paidCount, int servedCount, int departureCount, int revenueCents,
            double averageSatisfaction, int turnsUsed) {
        this.victory = victory;
        this.paidCount = paidCount;
        this.servedCount = servedCount;
        this.departureCount = departureCount;
        this.revenueCents = revenueCents;
        this.averageSatisfaction = averageSatisfaction;
        this.turnsUsed = turnsUsed;
    }

    public boolean isVictory() {
        return victory;
    }

    public int getPaidCount() {
        return paidCount;
    }

    public int getServedCount() {
        return servedCount;
    }

    public int getDepartureCount() {
        return departureCount;
    }

    public int getRevenueCents() {
        return revenueCents;
    }

    /**
     * Average satisfaction of paying customers, taken when they paid. Empty if nobody paid, which shows up as N/A.
     *
     * @return average paid satisfaction
     */
    public OptionalDouble getAverageSatisfaction() {
        return paidCount == 0 ? OptionalDouble.empty() : OptionalDouble.of(averageSatisfaction);
    }

    public int getTurnsUsed() {
        return turnsUsed;
    }
}
