# Test runs

Each `.txt` file in this folder is the complete keyboard input for one game in the base setting. The game has no
randomness, so replaying a file always plays the same game. `expected/` holds the full output each run printed when
we recorded it.

## How to replay

From the project root, in Git Bash:

```bash
bash test-runs/replay.sh                                            # replay every run here and compare with expected/
javac -d out src/*.java src/boundary/*.java src/control/*.java src/entity/*.java
java -cp out Main < test-runs/run-a-victory.txt                     # watch one run
```

In PowerShell, pipe the file instead: `Get-Content test-runs\run-a-victory.txt | java -cp out Main`.

Each line answers one prompt. `y` or `n` answers the Happy Hour question. For a staff member, `0` means Wait and `k`
picks the k-th task in the list printed above the prompt. Stage 3 asks about Happy Hour, then the Host, the Waiter and the Chef, on every
turn, so each file has 18 × 4 = 72 lines.

This folder only holds the runs for the code at this version. The runs for earlier stages are kept at their tags,
because their input plays a different game once the customers, staff and menu change:
[Stage 1 runs](https://github.com/DanielNg0729/SC2002-Restaurant-Rush/tree/stage-1/test-runs) and
[Stage 2 runs](https://github.com/DanielNg0729/SC2002-Restaurant-Rush/tree/stage-2/test-runs).

## Stage 3 runs

These are the runs in Part 4.5 of our report. Customer 2 is a VIP and customer 5 a Critic, as in Stage 2, and the menu
now ends with Combo (Salad + Pasta), $24.30 and 3 prep units. Customer 4 orders the Combo (menu item 4), and so does
the Critic, because it is the most expensive item.

Run A is the scenario in our sequence diagram. Happy Hour is switched on at turn 4. The Host seats each arrival. The
Waiter serves a READY order first, otherwise takes an order. The Chef prepares an order whenever one needs work (the
first one listed). Each other run changes one kind of decision and keeps these priorities everywhere else.

| Run | Decision changed from run A | Paid | Revenue | Average paid satisfaction | Result |
| --- | --- | :---: | :---: | :---: | --- |
| `run-a-victory` | none | 4 | $62.10 | 88.0 | VICTORY |
| `run-b-defeat` | Chef waits on turns 10-13 | 3 | $37.80 | 92.0 | DEFEAT |
| `run-c-victory` | On turn 13 the Waiter takes the Critic's order before serving customer 4 | 5 | $86.40 | 81.6 | VICTORY |
| D | Happy Hour never used | 4 | $64.80 | 86.75 | VICTORY |
| E | Happy Hour on turn 10 | 4 | $59.94 | 88.75 | VICTORY |
| F | Happy Hour on turn 13 | 4 | $64.80 | 90.5 | VICTORY |

Runs D, E and F are not saved as files because they are run A with one answer moved. Line 13 of `run-a-victory.txt`
is the `y` for turn 4. For D change it to `n`. For E change it to `n` and line 37 (turn 10) to `y`. For F change it
to `n` and line 49 (turn 13) to `y`.

### `run-a-victory`

| Customer | Type | Dish | Bill | Paid on turn | Satisfaction when paying |
| --- | --- | --- | :---: | :---: | :---: |
| 1 | Regular | Salad | $9.00 | 4 | 92 |
| 2 | VIP | Burger | $10.80 | 7 | 100 |
| 3 | Regular | Pasta | $18.00 | 11 | 84 |
| 4 | Regular | Combo (Salad + Pasta) | $24.30 | 15 | 76 |
| 5 | Critic | Combo (Salad + Pasta) | - | not paid (served on turn 17) | - |
| 6 | Regular | Burger | - | not paid (served on turn 18) | - |

Happy Hour is on for turns 4 and 5. The VIP orders on turn 4 and pays $15.00 × 0.9 × 0.8 = $10.80, then recovers 15
points at the start of turn 5 (up to 100). With the Host seating each arrival, the Waiter can take the order in the
same turn. Customer 4's Combo is prepared on turns 10-12, served on turn 13 and paid on turn 15. On turn 13 the Waiter
serves customer 4 instead of taking the Critic's order, so the Critic's Combo only starts on turn 14 and is not paid
by turn 18.

### `run-b-defeat`

| Customer | Type | Dish | Bill | Paid on turn | Satisfaction when paying |
| --- | --- | --- | :---: | :---: | :---: |
| 1 | Regular | Salad | $9.00 | 4 | 92 |
| 2 | VIP | Burger | $10.80 | 7 | 100 |
| 3 | Regular | Pasta | $18.00 | 11 | 84 |
| 4 | Regular | Combo (Salad + Pasta) | - | not paid (never finished) | - |
| 5 | Critic | Combo (Salad + Pasta) | - | not paid (served on turn 17) | - |
| 6 | Regular | - | - | not paid (no free table) | - |

The Chef waits on turns 10-13, so no Combo can start before turn 14. From then on the first order listed is the
Critic's, at table 1: READY on turn 16, served on turn 17 and ready to pay only at the end of turn 18. Customer 4's
Combo gets two prep units on turns 17 and 18 and is never finished. Only 3 customers pay, for $37.80, so the game
fails the paid and revenue targets. The average is high only because customer 4, the least satisfied customer, never
pays.

### `run-c-victory`

| Customer | Type | Dish | Bill | Paid on turn | Satisfaction when paying |
| --- | --- | --- | :---: | :---: | :---: |
| 1 | Regular | Salad | $9.00 | 4 | 92 |
| 2 | VIP | Burger | $10.80 | 7 | 100 |
| 3 | Regular | Pasta | $18.00 | 11 | 84 |
| 4 | Regular | Combo (Salad + Pasta) | $24.30 | 16 | 68 |
| 5 | Critic | Combo (Salad + Pasta) | $24.30 | 18 | 64 |
| 6 | Regular | Burger | - | not paid (served on turn 18) | - |

On turn 13 the Host seats the Critic while customer 4's Combo is READY, and the Waiter has one task for both. Taking
the Critic's order first lets the Chef start the Critic's Combo in the same turn, because the Chef acts after the
Waiter. Customer 4 is served a turn later and pays with 68, and the Critic pays on turn 18 with 64. Compared with run
A, revenue rises by $24.30 and the average falls from 88.0 to 81.6. Both runs win: the player trades satisfaction for
revenue.
