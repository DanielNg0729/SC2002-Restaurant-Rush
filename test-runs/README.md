# Test runs

Each `.txt` file in this folder is the complete keyboard input for one game in the base setting. The game has no
randomness, so replaying a file always plays the same game. `expected/` holds the full output each run printed when
we recorded it.

## How to replay

From the project root, in Git Bash:

```bash
bash test-runs/replay.sh                                            # replay every run here and compare with expected/
javac -d out src/*.java src/boundary/*.java src/control/*.java src/entity/*.java
java -cp out Main < test-runs/stage-1-victory.txt                   # watch one run
```

In PowerShell, pipe the file instead: `Get-Content test-runs\stage-1-victory.txt | java -cp out Main`.

Each line answers one prompt. `0` means Wait and `n` picks the n-th task in the list printed above the prompt.
Stage 1 asks the Waiter, then the Chef, on every turn, so each file has 18 × 2 = 36 lines.

## Stage 1 runs

Both runs use the same priorities. The Waiter serves a READY order first, otherwise takes an order, otherwise seats
the first customer in the queue. The Chef prepares an order whenever one needs work (the first one listed). The
defeat changes one decision.

| Run | Decision changed | Paid | Revenue | Average paid satisfaction | Result |
| --- | --- | :---: | :---: | :---: | --- |
| `stage-1-victory` | none | 5 | $66.00 | 79.2 | VICTORY |
| `stage-1-defeat` | Waiter waits on turns 1-3 | 4 | $51.00 | 58.0 | DEFEAT |

### `stage-1-victory`

| Customer | Dish | Bill | Paid on turn | Satisfaction when paying |
| --- | --- | :---: | :---: | :---: |
| 1 | Salad | $9.00 | 5 | 84 |
| 2 | Burger | $15.00 | 8 | 84 |
| 3 | Pasta | $18.00 | 12 | 76 |
| 4 | Salad | $9.00 | 15 | 76 |
| 5 | Burger | $15.00 | 18 | 76 |
| 6 | Pasta | - | not paid | - |

Every customer needs three Waiter tasks (seat, take order, serve), and a new customer arrives every three turns, so
the Waiter has almost no spare turns. On turns 10, 13 and 16 the Waiter can either serve a READY order or seat the customer who
has just arrived. Serving first gets the payment in sooner, and the new arrival waits one turn in the queue (8
points). Customer 6 arrives on turn 16 and cannot pay within 18 turns, as the brief expects.

### `stage-1-defeat`

| Customer | Dish | Bill | Paid on turn | Satisfaction when paying |
| --- | --- | :---: | :---: | :---: |
| 1 | Salad | $9.00 | 8 | 60 |
| 2 | Burger | $15.00 | 11 | 60 |
| 3 | Pasta | $18.00 | 15 | 52 |
| 4 | Salad | $9.00 | 17 | 60 |
| 5 | Burger | - | not paid (served on turn 18) | - |
| 6 | - | - | not paid (still in the queue) | - |

The Waiter waits on turns 1-3, so customer 1 sits in the queue until turn 4. Because the Waiter has almost no spare
turns, the three lost turns are never won back and every later customer waits longer. Four customers still pay
$51.00, but their average satisfaction is 58.0, below the target of 60, so the game is lost on satisfaction alone.
