/**
 * Entry point for the assignment project.
 * Demonstrates word frequency counting and runs experiments.
 */
public class Main {
    public static void main(String[] args) {
        // Small demo
        String sampleText = "The cat and the dog. The cat is happy.";
        HashTable demoTable = new HashTable();
        TextProcessor.processText(sampleText, demoTable);

        System.out.println("=== Demo: Word Frequency Analysis ===");
        System.out.println("Input text: \"" + sampleText + "\"");
        System.out.println("Frequency('the') = " + demoTable.get("the"));
        System.out.println("Frequency('cat') = " + demoTable.get("cat"));
        System.out.println("Frequency('dog') = " + demoTable.get("dog"));
        System.out.println("Frequency('happy') = " + demoTable.get("happy"));
        System.out.println("Contains('bird') = " + demoTable.contains("bird"));
        System.out.println("Unique words = " + demoTable.getSize());
        System.out.println("Collisions = " + demoTable.getCollisionCount());
        System.out.printf("Load factor = %.4f%n", demoTable.getLoadFactor());

        // Experiments
        ExperimentRunner.runExperiments();
    }
}
