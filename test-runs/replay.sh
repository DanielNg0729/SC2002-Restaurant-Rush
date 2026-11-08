#!/usr/bin/env bash
# Replays the runs in test-runs/ and checks each output against test-runs/expected/.
# Run it from anywhere:   bash test-runs/replay.sh            (every run in the folder)
#                         bash test-runs/replay.sh NAME ...   (only these runs, e.g. stage-1-victory)
cd "$(dirname "$0")/.." || exit 1
javac -d out/replay src/*.java src/boundary/*.java src/control/*.java src/entity/*.java || exit 1

runs=("$@")
if [ ${#runs[@]} -eq 0 ]; then
    for input in test-runs/*.txt; do
        runs+=("$(basename "$input" .txt)")
    done
fi

failed=0
for run in "${runs[@]}"; do
    actual=$(java -cp out/replay Main < "test-runs/$run.txt" | tr -d '\r')
    if [ "$actual" == "$(tr -d '\r' < "test-runs/expected/$run.txt")" ]; then
        echo "PASS  $run  ($(echo "$actual" | grep -E '^(VICTORY|DEFEAT)$'))"
    else
        echo "FAIL  $run"
        failed=1
    fi
done
exit $failed
