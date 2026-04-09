/**
 * Custom hash table implementation using separate chaining.
 * Keys are words (String) and values are occurrence counts (int).
 */
public class HashTable {
    private WordEntry[] table;
    private int capacity;
    private int size;
    private int collisionCount;
    private final double loadFactorThreshold;

    /**
     * Creates a hash table with default capacity and load factor threshold.
     */
    public HashTable() {
        this(16, 0.75);
    }

    /**
     * Creates a hash table with custom initial capacity and threshold.
     */
    public HashTable(int initialCapacity, double loadFactorThreshold) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be > 0");
        }
        if (loadFactorThreshold <= 0.0 || loadFactorThreshold >= 1.0) {
            throw new IllegalArgumentException("Load factor threshold must be between 0 and 1");
        }

        this.capacity = initialCapacity;
        this.table = new WordEntry[this.capacity];
        this.size = 0;
        this.collisionCount = 0;
        this.loadFactorThreshold = loadFactorThreshold;
    }

    /**
     * Computes the bucket index for a word.
     */
    private int hash(String word) {
        int h = word.hashCode();
        // Ensure non-negative index, including Integer.MIN_VALUE case.
        h = h & 0x7fffffff;
        return h % capacity;
    }

    /**
     * Inserts a word into the table.
     * If the word exists, increments its count.
     * If a different word must be added to a non-empty bucket, counts a collision.
     */
    public void put(String word) {
        if (word == null || word.isEmpty()) {
            return;
        }

        int index = hash(word);
        WordEntry head = table[index];

        // Empty bucket: add directly.
        if (head == null) {
            table[index] = new WordEntry(word);
            size++;
        } else {
            // Traverse chain to find existing word.
            WordEntry current = head;
            while (current != null) {
                if (current.word.equals(word)) {
                    current.count++;
                    return;
                }
                current = current.next;
            }

            // Word does not exist in this non-empty bucket: this is a collision.
            collisionCount++;

            // Insert new entry at head for simplicity and O(1) insertion.
            WordEntry newEntry = new WordEntry(word);
            newEntry.next = head;
            table[index] = newEntry;
            size++;
        }

        // Resize after inserting a new unique word if load factor exceeds threshold.
        if (getLoadFactor() > loadFactorThreshold) {
            resize();
        }
    }

    /**
     * Returns the frequency count for a word, or 0 if not present.
     */
    public int get(String word) {
        if (word == null || word.isEmpty()) {
            return 0;
        }

        int index = hash(word);
        WordEntry current = table[index];

        while (current != null) {
            if (current.word.equals(word)) {
                return current.count;
            }
            current = current.next;
        }

        return 0;
    }

    /**
     * Checks whether the table contains the given word.
     */
    public boolean contains(String word) {
        return get(word) > 0;
    }

    /**
     * Number of unique words stored in the hash table.
     */
    public int getSize() {
        return size;
    }

    /**
     * Number of collisions encountered during insertion of new unique words.
     */
    public int getCollisionCount() {
        return collisionCount;
    }

    /**
     * Current load factor = unique keys / capacity.
     */
    public double getLoadFactor() {
        return (double) size / capacity;
    }

    /**
     * Current number of buckets in the table.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the maximum chain length among all buckets.
     */
    public int getMaxChainLength() {
        int max = 0;
        for (WordEntry head : table) {
            int length = 0;
            WordEntry current = head;
            while (current != null) {
                length++;
                current = current.next;
            }
            if (length > max) {
                max = length;
            }
        }
        return max;
    }

    /**
     * Returns average chain length among non-empty buckets.
     */
    public double getAverageChainLength() {
        int nonEmptyBuckets = 0;
        int totalEntriesInChains = 0;

        for (WordEntry head : table) {
            if (head != null) {
                nonEmptyBuckets++;
                WordEntry current = head;
                while (current != null) {
                    totalEntriesInChains++;
                    current = current.next;
                }
            }
        }

        if (nonEmptyBuckets == 0) {
            return 0.0;
        }
        return (double) totalEntriesInChains / nonEmptyBuckets;
    }

    /**
     * Doubles capacity and rehashes all entries into a new table.
     * Collision count is preserved as historical insertion statistic.
     */
    private void resize() {
        int oldCapacity = capacity;
        WordEntry[] oldTable = table;

        capacity = oldCapacity * 2;
        table = new WordEntry[capacity];

        // Reinsert existing nodes into new table without changing size or collision stats.
        for (WordEntry head : oldTable) {
            WordEntry current = head;
            while (current != null) {
                WordEntry nextNode = current.next;

                int newIndex = hash(current.word);
                current.next = table[newIndex];
                table[newIndex] = current;

                current = nextNode;
            }
        }
    }
}
