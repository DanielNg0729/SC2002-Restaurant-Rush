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
| 2. Customer behaviour | [`stage-2`](https://github.com/DanielNg0729/SC2002-Restaurant-Rush/tree/stage-2) | VIP and Critic customers |
| 3. New role and menu policy | [`stage-3`](https://github.com/DanielNg0729/SC2002-Restaurant-Rush/tree/stage-3) | Host, Combo meal and Happy Hour |

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

**Test runs.** [`test-runs/` at tag `stage-1`](https://github.com/DanielNg0729/SC2002-Restaurant-Rush/tree/stage-1/test-runs)
has one reproducible victory and one reproducible defeat, with notes:

| Run | Paid | Revenue | Average paid satisfaction | Result |
| --- | :---: | :---: | :---: | --- |
| `stage-1-victory` | 5 | $66.00 | 79.2 | VICTORY |
| `stage-1-defeat` (Waiter waits on turns 1-3) | 4 | $51.00 | 58.0 | DEFEAT |

## Stage 2: Customer behaviour

Customer 2 is now a VIP and customer 5 a Critic. The other four are Regular, so there are still six arrivals.

| Customer | Satisfaction lost per waiting turn | Bill | Food choice |
| --- | :---: | --- | --- |
| Regular | 8 | Full price | Customer n orders menu item n, going back to item 1 after the last |
| VIP | 5 | 10% off (Burger $13.50) | Same as Regular |
| Critic | 12 | Full price | Always orders the most expensive item on the menu (the first one if there is a tie) |

The Critic's rule makes them the hardest customer to finish: they arrive on turn 13, lose satisfaction fastest and
order Pasta, which needs 2 prep units (from Stage 3, the 3-unit Combo), so the player has to rush them to get paid by
turn 18.

All three types go through the same order and service flow. The differences live in the customer objects: `Customer`
asks its subclass for `getWaitingLoss()` at the end of each turn, `StandardPricing` asks for `getPricePercent()` when
the order is taken, and the Waiter asks the customer to `chooseItem(menu)`. The controller has no type checks.

**Changes from Stage 1** ([compare `stage-1...stage-2`](https://github.com/DanielNg0729/SC2002-Restaurant-Rush/compare/stage-1...stage-2)):

| File | Change | Why |
| --- | --- | --- |
| `src/entity/VIPCustomer.java` | New | Overrides `getWaitingLoss()` (5) and `getPricePercent()` (90) |
| `src/entity/CriticCustomer.java` | New | Overrides `getWaitingLoss()` (12) and `chooseItem()` (most expensive item) |
| `src/control/ArrivalSchedule.java` | Changed | Creates a VIP for the 2nd arrival and a Critic for the 5th, so every base-setting game has one of each |
| `test-runs/` | Stage 1 runs replaced by Stage 2 runs | The Stage 1 input plays a different game with a VIP and a Critic. The Stage 1 runs stay at tag `stage-1` |
| `README.md` | Changed | This section |

No other class changed. The controller, staff and pricing already worked with any `Customer`, so the new types plug in
without touching them.

**Test runs.** [`test-runs/` at tag `stage-2`](https://github.com/DanielNg0729/SC2002-Restaurant-Rush/tree/stage-2/test-runs)
uses the same priorities as Stage 1:

| Run | Paid | Revenue | Average paid satisfaction | Result |
| --- | :---: | :---: | :---: | --- |
| `stage-2-victory` | 4 | $49.50 | 81.5 | VICTORY |
| `stage-2-defeat` (Chef waits on turns 12-14) | 3 | $40.50 | 83.3 | DEFEAT |

## Stage 3: New role and menu policy

Each turn now asks about Happy Hour, then the Host, the Waiter and the Chef.

**Host.** A second front-of-house role. Before the Waiter acts, the Host can seat one queued customer at a free
table, or Wait. The Waiter can still seat customers too, so two customers can be seated in one turn.

**Combo meal.** The menu gains a 4th item, Combo (Salad + Pasta). Its price is 90% of its two dishes, rounded to the
nearest cent: ($9.00 + $18.00) × 0.9 = $24.30. Its prep units are the sum of the two dishes: 1 + 2 = 3. Customers
choose a Combo by the same rules as any other item: customer n orders menu item n, so customer 4 orders the Combo, and
the Critic orders it because it is now the most expensive item. Customer 6 goes back to the start of the menu and
orders a Burger.

**Happy Hour.** At the start of any turn the player can switch Happy Hour on, once per game. It lasts for that turn
and the next, and the turn display shows how many turns are left. While it is on:

- Orders taken get 20% off, after the VIP discount. A VIP Burger costs $15.00 × 0.9 × 0.8 = $10.80. The bill is
  locked when the order is taken, so orders placed earlier keep their price.
- At the start of each Happy Hour turn, after the arrival, every customer still waiting for a table or for food gets
  15 satisfaction back (up to 100). Customers who have left are not in the restaurant, so they are not affected.

**New classes.** `Host` reuses the seating it shares with the Waiter through `FrontOfHouseStaff`. `ComboMeal` is a
`MenuItem` made of two `Dish` objects. `HappyHour` is a `PricingPolicy` that wraps `StandardPricing`, which is why the
VIP discount applies first.

**Changes from Stage 2** ([compare `stage-2...stage-3`](https://github.com/DanielNg0729/SC2002-Restaurant-Rush/compare/stage-2...stage-3)):

| File | Change | Why |
| --- | --- | --- |
| `src/entity/Host.java` | New | A `FrontOfHouseStaff` whose only tasks are the seating tasks it inherits |
| `src/entity/ComboMeal.java` | New | Works out its name, price and prep units from its two dishes |
| `src/entity/HappyHour.java` | New | 20% off on top of `StandardPricing` while active, the once-per-game two-turn countdown, and the recovery |
| `src/control/Game.java` | Changed | Adds Combo (Salad + Pasta) to the menu |
| `src/entity/Restaurant.java` | Changed | Creates the Host and lists it first in `getAllStaff()`. Adds `setPricing()` so the controller can make Happy Hour the pricing policy for new orders |
| `src/control/GameController.java` | Changed | Asks about Happy Hour at the start of each turn, applies its recovery after the arrival and counts it down at the end of the turn. Runs the Host step before the Waiter |
| `src/control/GameView.java` | Changed | Adds `chooseHostTask()` and `chooseHappyHourActivation()`, so the controller can ask for the two new decisions |
| `src/boundary/RestaurantCLI.java` | Changed | Implements the two new prompts and shows Happy Hour's remaining turns and the Host's task count |
| `src/entity/Customer.java` | Changed | Adds `recoverIfWaiting()`, so Happy Hour can give satisfaction back without reaching into the customer's state |
| `test-runs/` | Stage 2 runs replaced by runs A, B and C | The Stage 2 input does not answer the new Happy Hour and Host prompts. The Stage 2 runs stay at tag `stage-2` |
| `README.md` | Changed | This section |

No class was deleted. The Waiter, Chef, Cashier, `Order` and `StandardPricing` did not change. The Waiter already
prices each order with whatever `PricingPolicy` the restaurant holds, and the Chef already works through any
`MenuItem`'s prep units.

**Test runs.** [`test-runs/`](test-runs/) holds runs A, B and C from Part 4.5 of our report, with notes. Each run
replays through the normal entry point, for example `java -cp out Main < test-runs/run-a-victory.txt`. Run A is the
scenario in our sequence diagram, and each other run changes one kind of decision:

| Run | Decision changed from run A | Paid | Revenue | Average paid satisfaction | Result |
| --- | --- | :---: | :---: | :---: | --- |
| `run-a-victory` | none | 4 | $62.10 | 88.0 | VICTORY |
| `run-b-defeat` | Chef waits on turns 10-13 | 3 | $37.80 | 92.0 | DEFEAT |
| `run-c-victory` | On turn 13 the Waiter takes the Critic's order before serving customer 4 | 5 | $86.40 | 81.6 | VICTORY |

`bash test-runs/replay.sh` replays every run of the version you have checked out and compares the output with
`test-runs/expected/`.

## Diagrams

`assets/diagrams/` holds the [class diagram](assets/diagrams/class-diagram.png) and the
[sequence diagram](assets/diagrams/sequence-diagram.png) of the final design, which match the Stage 3 code.

## Video

| # | Segment | Timestamp |
| --- | --- | --- |
| 1 | The game launching from the submitted project | |
| 2 | Manager choices, including a decision where staff are constrained | |
| 3 | A dish going READY, SERVED, then payment | |
| 4 | A VIP or Critic behaviour | |
| 5 | A Stage 3 feature | |
| 6 | One victory and one defeat | |
| 7 | The stage tag comparisons and the diagrams | |

## Rules

- Branch off `main`, edit only your task's files, and open a PR into `main`.
- Don't rename classes, methods or fields or change signatures. They have to match the class diagram. If you need
  a change in someone else's file, ask the owner.
- The Javadoc is the spec. Update it if the behaviour changes.
- Commit with `git commit -s`.
