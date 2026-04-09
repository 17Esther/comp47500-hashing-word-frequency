/**
 * Represents one node in a bucket chain of the hash table.
 * Stores a word, its frequency count, and a pointer to the next node.
 */
public class WordEntry {
    String word;
    int count;
    WordEntry next;

    public WordEntry(String word) {
        this.word = word;
        this.count = 1;
        this.next = null;
    }
}
