package entity;

/**
 * Stages an order goes through, from being taken to being paid (or cancelled if the customer leaves).
 *
 * @since Stage 1
 */
public enum OrderStatus {
    PLACED,
    IN_PREPARATION,
    READY,
    SERVED,
    PAID,
    CANCELLED
}
