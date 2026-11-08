# Test runs

Each `.txt` file in this folder is the complete keyboard input for one game in the base setting. The game has no
randomness, so replaying a file always plays the same game. `expected/` holds the full output each run printed when
we recorded it.

## How to replay

From the project root, in Git Bash:

```bash
bash test-runs/replay.sh                                            # replay every run here and compare with expected/
javac -d out src/*.java src/boundary/*.java src/control/*.java src/entity/*.java
java -cp out Main < test-runs/stage-2-victory.txt                   # watch one run
```

In PowerShell, pipe the file instead: `Get-Content test-runs\stage-2-victory.txt | java -cp out Main`.

Each line answers one prompt. `0` means Wait and `n` picks the n-th task in the list printed above the prompt.
Stage 2 asks the Waiter, then the Chef, on every turn, so each file has 18 × 2 = 36 lines.

This folder only holds the runs for the code at this version. The runs for earlier stages are kept at their tags,
because their input plays a different game once the customers change:
[Stage 1 runs](https://github.com/DanielNg0729/SC2002-Restaurant-Rush/tree/stage-1/test-runs).

## Stage 2 runs

Customer 2 is a VIP and customer 5 is a Critic. Both runs use the same priorities as the Stage 1 runs. The Waiter
serves a READY order first, otherwise takes an order, otherwise seats the first customer in the queue. The Chef
prepares an order whenever one needs work (the first one listed). The defeat changes one decision.

| Run | Decision changed | Paid | Revenue | Average paid satisfaction | Result |
| --- | --- | :---: | :---: | :---: | --- |
| `stage-2-victory` | none | 4 | $49.50 | 81.5 | VICTORY |
| `stage-2-defeat` | Chef waits on turns 12-14 | 3 | $40.50 | 83.3 | DEFEAT |

### `stage-2-victory`

| Customer | Type | Dish | Bill | Paid on turn | Satisfaction when paying |
| --- | --- | --- | :---: | :---: | :---: |
| 1 | Regular | Salad | $9.00 | 5 | 84 |
| 2 | VIP | Burger | $13.50 | 8 | 90 |
| 3 | Regular | Pasta | $18.00 | 12 | 76 |
| 4 | Regular | Salad | $9.00 | 15 | 76 |
| 5 | Critic | Pasta | - | not paid (served on turn 17) | - |
| 6 | Regular | Pasta | - | not paid | - |

The VIP pays 10% less ($13.50 for the Burger) and loses 5 points per waiting turn instead of 8, so they pay with 90.
The Critic arrives on turn 13, orders the most expensive item (Pasta, 2 prep units) and loses 12 points per waiting
turn. They are served on turn 17 and are only ready to pay at the end of turn 18, so 4 customers pay. That is still
enough to win: $49.50 and an average of 81.5.

### `stage-2-defeat`

| Customer | Type | Dish | Bill | Paid on turn | Satisfaction when paying |
| --- | --- | --- | :---: | :---: | :---: |
| 1 | Regular | Salad | $9.00 | 5 | 84 |
| 2 | VIP | Burger | $13.50 | 8 | 90 |
| 3 | Regular | Pasta | $18.00 | 12 | 76 |
| 4 | Regular | Salad | - | not paid (served on turn 18) | - |
| 5 | Critic | Pasta | - | not paid (served on turn 17) | - |
| 6 | Regular | - | - | not paid (still in the queue) | - |

The Chef waits on turns 12-14, so customer 4's Salad, ordered on turn 12, is not started. When the Chef starts again
on turn 15, the first order listed is the Critic's Pasta at table 1, which takes turns 15 and 16. Customer 4's Salad
is only cooked on turn 17. The Critic and customer 4 are served on turns 17 and 18, too late to pay. Only 3 customers
pay, for $40.50, so the game fails the paid and revenue targets even though the average (83.3) is high.
