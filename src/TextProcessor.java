import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for text cleaning and insertion into the hash table.
 */
public class TextProcessor {

    /**
     * Extracts normalized words from raw text.
     */
    public static String[] extractWords(String text) {
        if (text == null || text.isEmpty()) {
            return new String[0];
        }

        String cleaned = text.toLowerCase() // lowercase the text
                .replaceAll("[^a-z0-9\\s]", " ") // remove punctuation/non-alphanumeric symbols
                .trim();

        if (cleaned.isEmpty()) {
            return new String[0];
        }

        String[] tokens = cleaned.split("\\s+"); // split by whitespace
        List<String> words = new ArrayList<>();
        for (String token : tokens) { // ignore empty tokens
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
