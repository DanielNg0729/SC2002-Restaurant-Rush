# SC2002-Restaurant-Rush

A turn-based restaurant management game in Java, split into `boundary`, `control` and `entity` packages.
The skeleton follows the class diagram in `assets/diagrams/`. Every method left to write is marked `// TODO`,
and its Javadoc explains what it has to do.

## Build and run

```bash
javac -d out src/*.java src/boundary/*.java src/control/*.java src/entity/*.java
java -cp out Main
```

## Task split

| # | Task | Owner | Files | TODOs | Uses methods from | Branch |
| --- | --- | --- | --- | :---: | :---: | --- |
| 1 | Customers | | `Customer`, `CustomerState`, `CriticCustomer` | 16 | - | `task1-customers` |
| 2 | Orders & pricing | | `Order`, `PricingPolicy`, `StandardPricing`, `ComboMeal`, `HappyHour` | 17 | 1 | `task2-pricing` |
| 3 | Staff | | `Staff`, `StaffTask`, `FrontOfHouseStaff`, `Waiter`, `Chef`, `Cashier`, `Host` | 15 | 1, 2, 4 | `task3-staff` |
| 4 | Restaurant floor | | `Table`, `WaitingQueue`, `SalesLedger`, `Restaurant`, `ArrivalSchedule` | 17 | 2 | `task4-restaurant` |
| 5 | Game flow & CLI | | `GameController`, `RestaurantCLI`, `Game` | 16 | 1-4 | `task5-game` |

Merge order: tasks 1, 2 and 4 first, then 3, then 5.

### Task 1 - Customers

- `CustomerState`: `isAwaitingService()` is true for WAITING, SEATED and ORDERED. `canMoveTo(next)` allows one step
  forward in the life cycle, or a move to LEFT while still awaiting service.
- `Customer` state changes: `moveTo` (rejects illegal moves), `markSeated`, `placeOrder`, `markServed`, `markPaid`,
  `isReadyToPay`, `hasLeft`.
- Satisfaction: `adjustSatisfaction` (keep it between 0 and 100), `applyWaitingLoss` (LEFT at 0), `recoverIfWaiting`
  (only while awaiting service).
- `endTurn()`: a customer served on an earlier turn finishes eating (`advanceEating` to READY_TO_PAY). A customer
  still awaiting service loses `getWaitingLoss()` points.
- `chooseItem`: customer n orders menu item n, wrapping back to item 1. `CriticCustomer.chooseItem` picks the most
  expensive item (the first one if there's a tie).

### Task 2 - Orders & pricing

- `Order`: `getRemainingUnits`, `isReady`, `needsPreparation`, `advancePreparation` (PLACED, then IN_PREPARATION,
  then READY), `markServed`, `markPaid`, `cancel`.
- `PricingPolicy.applyPercent`: `(cents * percent + 50) / 100`.
- `StandardPricing.priceFor`: item price with the customer's `getPricePercent()` applied.
- `ComboMeal` (Stage 3): name `Combo (Salad + Pasta)`, 90% of both prices (2430 cents), prep units added (3).
- `HappyHour` (Stage 3): `activate` works once per game and lasts 2 turns. `priceFor` takes 20% off the base price
  (VIP discount first). `startTurn` gives +15 satisfaction to waiting customers. `endTurn` counts down. `isActive`.

### Task 3 - Staff

- `Staff`: `isAvailable`, `startTurn`, `performTask` (one task per turn, and only a successful result uses it up).
  `StaffTask.execute`.
- `FrontOfHouseStaff`: `seat` moves a customer from the queue to a table and marks them SEATED, or fails with a
  reason. `seatingTasks` lists one task per customer who can be seated.
- `Waiter`: tasks are seat, take order or serve a READY order. `takeOrder` (customer chooses, the pricing policy
  locks the bill, new PLACED order). `serve`.
- `Chef`: one task per order that needs preparation. `prepare` does one prep unit.
- `Cashier`: `processNextPayment` takes payment from the oldest READY_TO_PAY customer (from an earlier turn), records
  it in the ledger, marks the order and customer PAID and frees the table.
- `Host` (Stage 3): `listAvailableTasks` returns the seating tasks.

### Task 4 - Restaurant floor

- `Table`: `isFree`, `seat`, `release`.
- `WaitingQueue`: `join`, `remove`, `contains`, `isEmpty`, `size`.
- `SalesLedger`: `recordPayment` (revenue, paid count, satisfaction at payment). `getAverageSatisfaction` is empty if
  nobody has paid.
- `Restaurant`: `getFreeTables`, `findTableOf`, `getSeatedCustomers`, `getActiveOrders`, `removeLeavingCustomer`
  (cancel the unfinished order, remove from the queue or free the table, count the departure).
- `ArrivalSchedule`: `isArrivalTurn`, `createCustomer` (the n-th arrival gets id n). For Stage 2, at least one of the
  six arrivals must be a VIP and one a Critic. Pick which ones and note it here.

### Task 5 - Game flow & CLI

- `GameController.runGame`: play all 18 turns (no early end), then `checkResult`.
- `runTurn`: reset staff, optional Happy Hour, then `processArrival`, `processManagerChoices` (Host, Waiter, then
  Chef; ask again on an invalid choice), `processEatingAndPayment`, `processEndTurn` (remove customers who LEFT).
- `activateHappyHour`, and call `HappyHour.startTurn` / `endTurn` every turn.
- `checkResult`: victory needs at least 4 paid customers, at least $45 revenue and an average paid satisfaction of
  at least 60.
- `RestaurantCLI`: `run`, `displayTurnState`, `chooseWaiterTask` / `chooseHostTask` / `chooseChefTask` (list the
  tasks plus Wait, ask again on bad input), `chooseHappyHourActivation`, `displayMessage`, `displayResult`.

## Rules

- Branch off `main`, edit only your task's files, and open a PR into `main`.
- Don't rename classes, methods or fields or change signatures. They have to match the class diagram. If you need
  a change in someone else's file, ask the owner.
- The Javadoc is the spec. Update it if the behaviour changes.
- Commit with `git commit -s`.
