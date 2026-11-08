# SC2002-Restaurant-Rush

A turn-based restaurant management game in Java with a command-line interface. The player is the manager and decides
what the staff do on each turn. The code is split into `boundary`, `control` and `entity` packages.

Repository: <https://github.com/DanielNg0729/SC2002-Restaurant-Rush>

## Build and run

Needs Java 17 or later.

```bash
javac -d out src/*.java src/boundary/*.java src/control/*.java src/entity/*.java
java -cp out Main
```

## Stages

The game is built in stages. Each stage is a tagged version that compiles and plays on its own.

| Stage | Tag | Adds |
| --- | --- | --- |
| 1. Basic operation | [`stage-1`](https://github.com/DanielNg0729/SC2002-Restaurant-Rush/tree/stage-1) | Regular customers, the menu, Waiter, Chef and Cashier, the full turn sequence, victory and defeat |

To play a stage, check out its tag and build it as above, for example `git checkout stage-1`. Run `git checkout main`
to come back.

## Stage 1: Basic restaurant operation

**Base setting.** 18 turns, 2 tables, one Waiter, one Chef and one Cashier. One customer arrives at the start of turns
1, 4, 7, 10, 13 and 16. The menu is Salad ($9.00, 1 prep unit), Burger ($15.00, 1) and Pasta ($18.00, 2).

**A turn.** The start of each turn shows the turn number, revenue, average paid satisfaction, the waiting queue, the
tables, each customer's satisfaction and order status, and how many tasks each staff member has. Then:

1. **Arrival.** The scheduled customer joins the waiting queue with satisfaction 100.
2. **Waiter.** The player picks one task: seat a queued customer at a free table, take a seated customer's order,
   serve a READY order, or Wait.
3. **Chef.** The player picks one prep unit on one order, or Wait. The Chef acts after the Waiter, so the Chef can
   start an order taken this turn, and a dish that becomes READY this turn is served next turn at the earliest.
4. **Payment.** The Cashier takes at most one payment on its own, from the earliest-arriving customer who is
   READY_TO_PAY. The bill goes into revenue, the customer and order become PAID and the table is freed.
5. **End of turn.** A customer served on turn t eats during turn t + 1 and pays on turn t + 2 at the earliest. Every
   customer still waiting for a table or for food loses 8 satisfaction. A customer who reaches 0 leaves without
   paying: their unfinished order is cancelled and their table is freed. The turn summary shows the served count,
   unhappy departures, revenue and average paid satisfaction (N/A until someone pays).
6. **Result.** The game always plays all 18 turns. It is a victory if at least 4 customers paid, revenue is at least
   $45.00 and the average satisfaction of paid customers (taken when they paid) is at least 60. Otherwise it is a
   defeat.

An invalid choice says why it failed and asks again, without using up the staff member's task. Money is kept in
whole cents.

**Food choice.** Customers choose their own food, and the manager never does. Customer n orders menu item n, going
back to item 1 after the last: customer 1 orders Salad, 2 Burger, 3 Pasta, 4 Salad, and so on. The choice is shown
when the Waiter takes the order.

**Main classes.**

| Package | Classes | Responsibility |
| --- | --- | --- |
| `entity` | `Customer` (abstract), `RegularCustomer`, `CustomerState` | A customer's life cycle and satisfaction |
| `entity` | `MenuItem`, `Dish`, `Salad`, `Burger`, `Pasta`, `Menu` | What can be ordered, with price and prep units |
| `entity` | `Order`, `OrderStatus`, `PricingPolicy`, `StandardPricing` | The bill, locked when the order is taken, and the cooking progress |
| `entity` | `Staff`, `FrontOfHouseStaff`, `Waiter`, `Chef`, `Cashier`, `StaffTask`, `ActionResult` | One task per staff member per turn, and the tasks each one can do right now |
| `entity` | `Restaurant`, `Table`, `WaitingQueue`, `SalesLedger` | The floor: tables, queue, staff, revenue and departures |
| `control` | `GameController`, `ArrivalSchedule`, `GameResult`, `GameView`, `Game` | The turn sequence, arrivals and the result. `Game` wires everything together |
| `boundary` | `RestaurantCLI` | Shows the state and reads the player's choices. It holds no game rules |

**Test runs.** [`test-runs/`](test-runs/) has one reproducible victory and one reproducible defeat, with notes:

| Run | Paid | Revenue | Average paid satisfaction | Result |
| --- | :---: | :---: | :---: | --- |
| `stage-1-victory` | 5 | $66.00 | 79.2 | VICTORY |
| `stage-1-defeat` (Waiter waits on turns 1-3) | 4 | $51.00 | 58.0 | DEFEAT |

Replay them all with `bash test-runs/replay.sh`.

## Diagrams

`assets/diagrams/` holds the class diagram and the sequence diagram of the final design.

## Rules

- Branch off `main`, edit only your task's files, and open a PR into `main`.
- Don't rename classes, methods or fields or change signatures. They have to match the class diagram. If you need
  a change in someone else's file, ask the owner.
- The Javadoc is the spec. Update it if the behaviour changes.
- Commit with `git commit -s`.
