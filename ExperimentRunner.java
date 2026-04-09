/**
 * Runs experiments for analyzing custom hash table behavior.
 */
public class ExperimentRunner {

    public static void runExperiments() {
        runExperiment1UniqueGrowth();
        runExperiment2VocabularyEffect();
        runExperiment3SearchPerformance();
        runExperiment4InitialCapacityEffect();
    }

    /**
     * Experiment 1:
     * Measures insertion performance as the number of distinct keys grows.
     */
    private static void runExperiment1UniqueGrowth() {
        int[] sizes = {1000, 5000, 10000, 20000, 50000};

        System.out.println("\n=== Experiment 1: Insertion vs Growing Unique Words ===");
        System.out.printf("%-12s %-12s %-14s %-14s %-12s %-12s%n",
                "TotalWords", "UniqueWords", "Time(ms)", "Collisions", "LoadFactor", "MaxChain");
        System.out.println("--------------------------------------------------------------------------------");

        for (int uniqueCount : sizes) {
            String text = TextGenerator.generateUniqueText(uniqueCount);
            HashTable hashTable = new HashTable();

            long start = System.nanoTime();
            TextProcessor.processText(text, hashTable);
            long end = System.nanoTime();

            double timeMs = (end - start) / 1_000_000.0;

            System.out.printf("%-12d %-12d %-14.3f %-14d %-12.4f %-12d%n",
                    uniqueCount,
                    hashTable.getSize(),
                    timeMs,
                    hashTable.getCollisionCount(),
                    hashTable.getLoadFactor(),
                    hashTable.getMaxChainLength());
        }
    }

    /**
     * Experiment 2:
     * Keeps total words fixed and varies vocabulary size to observe collision behavior.
     */
    private static void runExperiment2VocabularyEffect() {
        int totalWords = 50000;
        int[] vocabSizes = {10, 100, 1000, 5000, 10000};

        System.out.println("\n=== Experiment 2: Collisions vs Vocabulary Size (TotalWords=50000) ===");
        System.out.printf("%-10s %-12s %-14s %-14s %-12s %-14s %-12s%n",
                "Total", "VocabSize", "ActualUnique", "Collisions", "LoadFactor", "Time(ms)", "MaxChain");
        System.out.println("------------------------------------------------------------------------------------------------");

        for (int vocabSize : vocabSizes) {
            String text = TextGenerator.generateTextWithLimitedVocabulary(totalWords, vocabSize);
            HashTable hashTable = new HashTable();

            long start = System.nanoTime();
            TextProcessor.processText(text, hashTable);
            long end = System.nanoTime();

            double timeMs = (end - start) / 1_000_000.0;

            System.out.printf("%-10d %-12d %-14d %-14d %-12.4f %-14.3f %-12d%n",
                    totalWords,
                    vocabSize,
                    hashTable.getSize(),
                    hashTable.getCollisionCount(),
                    hashTable.getLoadFactor(),
                    timeMs,
                    hashTable.getMaxChainLength());
        }
    }

    /**
     * Experiment 3:
     * Measures average lookup time for frequent, rare, and missing words.
     */
    private static void runExperiment3SearchPerformance() {
        int vocabularySize = 20000;
        int frequentRepeats = 20000;
        int searchesPerCategory = 50000;

        // Build a dataset where all words exist at least once, then boost a few words to be frequent.
        String baseUniqueText = TextGenerator.generateUniqueText(vocabularySize);
        String frequentBoostText = TextGenerator.generateTextWithLimitedVocabulary(frequentRepeats, 5);
        String text = baseUniqueText + " " + frequentBoostText;

        HashTable hashTable = new HashTable();
        TextProcessor.processText(text, hashTable);

        String[] frequentWords = {"word0", "word1", "word2", "word3", "word4"};
        String[] rareWords = {
                "word19995", "word19996", "word19997", "word19998", "word19999"
        };
        String[] missingWords = {
                "missing_word_a", "missing_word_b", "missing_word_c", "missing_word_d", "missing_word_e"
        };

        double frequentAvgNs = measureAverageLookupTimeNs(hashTable, frequentWords, searchesPerCategory);
        double rareAvgNs = measureAverageLookupTimeNs(hashTable, rareWords, searchesPerCategory);
        double missingAvgNs = measureAverageLookupTimeNs(hashTable, missingWords, searchesPerCategory);

        System.out.println("\n=== Experiment 3: Search Performance by Query Type ===");
        System.out.printf("%-20s %-20s%n", "Category", "Avg Lookup Time (ns)");
        System.out.println("----------------------------------------------");
        System.out.printf("%-20s %-20.2f%n", "Existing Frequent", frequentAvgNs);
        System.out.printf("%-20s %-20.2f%n", "Existing Rare", rareAvgNs);
        System.out.printf("%-20s %-20.2f%n", "Missing", missingAvgNs);
    }

    /**
     * Experiment 4:
     * Compares insertion behavior for different initial capacities.
     */
    private static void runExperiment4InitialCapacityEffect() {
        int totalWords = 50000;
        int vocabularySize = 20000;
        int[] capacities = {16, 64, 256, 1024, 4096};
        double loadFactorThreshold = 0.75;

        String text = TextGenerator.generateTextWithLimitedVocabulary(totalWords, vocabularySize);

        System.out.println("\n=== Experiment 4: Effect of Initial Capacity ===");
        System.out.printf("%-12s %-12s %-14s %-14s %-12s %-12s %-12s%n",
                "InitCap", "FinalCap", "Collisions", "LoadFactor", "Time(ms)", "AvgChain", "MaxChain");
        System.out.println("------------------------------------------------------------------------------------------------");

        for (int initialCapacity : capacities) {
            HashTable hashTable = new HashTable(initialCapacity, loadFactorThreshold);

            long start = System.nanoTime();
            TextProcessor.processText(text, hashTable);
            long end = System.nanoTime();

            double timeMs = (end - start) / 1_000_000.0;

            System.out.printf("%-12d %-12d %-14d %-14.4f %-12.3f %-12.3f %-12d%n",
                    initialCapacity,
                    hashTable.getCapacity(),
                    hashTable.getCollisionCount(),
                    hashTable.getLoadFactor(),
                    timeMs,
                    hashTable.getAverageChainLength(),
                    hashTable.getMaxChainLength());
        }
    }

    private static double measureAverageLookupTimeNs(HashTable hashTable, String[] words, int totalLookups) {
        long start = System.nanoTime();
        int sink = 0;

        for (int i = 0; i < totalLookups; i++) {
            String word = words[i % words.length];
            sink += hashTable.get(word);
        }

        long end = System.nanoTime();
        if (sink == Integer.MIN_VALUE) {
            System.out.print("");
        }
        return (double) (end - start) / totalLookups;
    }
}
