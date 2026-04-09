import java.util.Random;

/**
 * Generates synthetic text datasets for experiments.
 * Uses deterministic randomness for reproducibility.
 */
public class TextGenerator {
    private static final long BASE_SEED = 42L;

    /**
     * Generates random text with a limited vocabulary.
     * Words are drawn from: word0, word1, ..., word(vocabularySize-1).
     */
    public static String generateTextWithLimitedVocabulary(int totalWords, int vocabularySize) {
        if (totalWords <= 0 || vocabularySize <= 0) {
            return "";
        }

        // Deterministic seed per parameter pair for repeatable experiments.
        long seed = BASE_SEED + 31L * totalWords + 17L * vocabularySize;
        Random random = new Random(seed);

        StringBuilder sb = new StringBuilder(totalWords * 8);
        for (int i = 0; i < totalWords; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            int idx = random.nextInt(vocabularySize);
            sb.append("word").append(idx);
        }
        return sb.toString();
    }

    /**
     * Generates text containing only unique words:
     * word0 word1 word2 ... word(uniqueWordCount-1)
     */
    public static String generateUniqueText(int uniqueWordCount) {
        if (uniqueWordCount <= 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(uniqueWordCount * 9);
        for (int i = 0; i < uniqueWordCount; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append("word").append(i);
        }
        return sb.toString();
    }

    /**
     * Backward-compatible helper kept for convenience.
     */
    public static String generateText(int wordCount) {
        return generateTextWithLimitedVocabulary(wordCount, 30);
    }
}
