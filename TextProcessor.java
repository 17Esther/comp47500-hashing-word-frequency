import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for text cleaning and insertion into the hash table.
 */
public class TextProcessor {

    /**
     * Extracts normalized words from raw text.
     * Steps:
     * 1) Lowercase
     * 2) Remove punctuation/non-alphanumeric symbols
     * 3) Split by whitespace
     * 4) Ignore empty tokens
     */
    public static String[] extractWords(String text) {
        if (text == null || text.isEmpty()) {
            return new String[0];
        }

        String cleaned = text.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ")
                .trim();

        if (cleaned.isEmpty()) {
            return new String[0];
        }

        String[] tokens = cleaned.split("\\s+");
        List<String> words = new ArrayList<>();
        for (String token : tokens) {
            if (!token.isEmpty()) {
                words.add(token);
            }
        }

        return words.toArray(new String[0]);
    }

    /**
     * Processes text and inserts all extracted words into the hash table.
     */
    public static void processText(String text, HashTable hashTable) {
        if (hashTable == null) {
            throw new IllegalArgumentException("HashTable must not be null");
        }

        String[] words = extractWords(text);
        for (String word : words) {
            hashTable.put(word);
        }
    }
}
