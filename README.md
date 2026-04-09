# COMP47500 Assignment 4 — Hash Table Word Frequency

A Java project that implements a **custom hash table** with **separate chaining** to count word frequencies, plus a small **benchmark harness** that studies collisions, load factor, resizing, and lookup cost on synthetic text.

## Contents

| Path | Description |
|------|-------------|
| `src/Main.java` | Entry point: demo text + runs all experiments |
| `src/HashTable.java` | Hash table: `put`, `get`, `contains`, resize, statistics |
| `src/WordEntry.java` | Linked-list node: word, count, `next` |
| `src/TextProcessor.java` | Normalizes text and feeds tokens into the table |
| `src/TextGenerator.java` | Deterministic synthetic corpora for experiments |
| `src/ExperimentRunner.java` | Four timed experiments printed to standard output |

## Requirements

- **Java JDK** (any recent version with `javac` / `java`)


## Build and run


If your shell is already inside `src/`:

```bash
javac *.java
java Main
```

To save output for a report:

```bash
java -cp src Main > results/run.txt
```

(Create a `results/` directory first if you want to keep outputs there.)

## What the program does

1. **Demo** — Inserts a short sample sentence, prints frequencies for a few words, `contains`, unique word count, collision count, and load factor.
2. **Experiments** — Builds larger synthetic texts, measures wall-clock insertion time (nanoseconds converted to ms) and table statistics.

## Hash table design

- **Collision resolution:** separate chaining (`WordEntry` singly linked lists per bucket).
- **Hash function:** `String.hashCode()`, masked to a non-negative value, then ` % capacity`.
- **Insert policy:** new distinct keys are **prepended** to the chain (`O(1)` link; may affect which keys are “deeper” in a bucket).
- **Dynamic resizing:** when **load factor** exceeds the **load factor threshold**, capacity **doubles** and all entries are **rehashed** into a new array. Collision statistics from earlier inserts are **not** reset on resize.

### Constructor defaults

```text
new HashTable()  →  initial capacity 16, load factor threshold 0.75
new HashTable(initialCapacity, loadFactorThreshold)  →  custom values
```

Threshold must be strictly between `0` and `1`.

### Metrics (important for the report)

| Term | Meaning in this project |
|------|-------------------------|
| **Size** | Number of **distinct** words (unique keys). |
| **Load factor** | `size / capacity` — average keys per bucket in a coarse sense; higher values usually mean longer chains on average. |
| **Load factor threshold** | Triggers a resize when **load factor is greater than** the threshold after inserting a new unique key. |
| **Collision count** | Incremented only when a **new distinct word** is placed into a bucket that **already holds a different word**. Repeated `put`s for the same word (frequency updates) **do not** increase this counter. |
| **Max chain length** | Longest linked list in any bucket after all operations. |
| **Avg chain length** | Average length over **non-empty** buckets (`HashTable.getAverageChainLength()`), used in Experiment 4. |

## Text processing

`TextProcessor.extractWords`:

- Lowercases input.
- Strips punctuation by replacing non `[a-z0-9]` (except spaces) with spaces.
- Splits on whitespace and drops empty tokens.

Words are then passed to `HashTable.put`.

## Synthetic data (`TextGenerator`)

- **`generateUniqueText(n)`** — `word0 word1 … word(n-1)` (all distinct).
- **`generateTextWithLimitedVocabulary(totalWords, vocabularySize)`** — random sequence of `word0 … word{vocabularySize-1}` with a **deterministic seed** derived from `totalWords` and `vocabularySize` so runs are reproducible.

## Experiments (summary)

| # | What it varies | What it prints |
|---|----------------|----------------|
| **1** | Distinct vocabulary size `n ∈ {1000, …, 50000}` (unique text) | Insertion time, collisions, final load factor, max chain |
| **2** | Vocabulary size with **50 000** total tokens | Actual unique count, collisions, load factor, time, max chain |
| **3** | Lookup workload | Average `get` time (ns) for frequent vs rare existing words vs missing words |
| **4** | Initial capacity `{16, 64, 256, 1024, 4096}` on a fixed 50k-token / 20k-vocab text | Final capacity, collisions, load factor, time, avg/max chain |

**Note:** Experiment 4 constructs tables with **`new HashTable(initialCapacity, 0.75)`** — the second argument is **fixed at 0.75** in `ExperimentRunner`, independent of the no-arg `HashTable()` default. If you change the default constructor’s threshold for Experiments 1–3, Experiment 4’s threshold stays `0.75` unless you edit that method.

## Interpreting results (short guide)

- **More collisions** (with this definition) generally correlates with **more chaining** while the table grows; **lower load factor threshold** (e.g. 0.5) tends to resize earlier → **larger final table** → often **fewer collisions** and **shorter chains**, at the cost of **more memory** and **more resize/rehash work**.
- **Insertion times** in milliseconds can **fluctuate** between runs (JVM warmup, CPU scheduling). For stable numbers, run multiple times or add an explicit warmup loop.
- **Experiment 3** nanosecond averages are **noisy**; use them to discuss **trends** (e.g. missing vs existing), not precise microbenchmarking.

## Project layout

```text
COMP47500_Assigment4/
├── README.md
├── .gitignore
└── src/
    ├── Main.java
    ├── HashTable.java
    ├── WordEntry.java
    ├── TextProcessor.java
    ├── TextGenerator.java
    └── ExperimentRunner.java
```

## Author

UCD COMP47500 — Jiawei Li 25212461
