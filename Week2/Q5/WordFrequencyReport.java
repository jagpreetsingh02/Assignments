import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WordFrequencyReport {

    static final String[] STOP_WORDS = {"the", "was", "and", "a", "is", "of", "in"};

    static boolean isStopWord(String word) {
        for (String stop : STOP_WORDS) {
            if (stop.equals(word)) {
                return true;
            }
        }
        return false;
    }

    static void printFilteredWordFrequency(String feedback) {
        String cleaned = feedback.toLowerCase()
                .replace(".", "")
                .replace(",", "")
                .replace("!", "")
                .replace("?", "")
                .replace(";", "")
                .replace(":", "");

        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String word : cleaned.trim().split("\\s+")) {
            if (word.isEmpty() || isStopWord(word)) {
                continue;
            }
            counts.put(word, counts.getOrDefault(word, 0) + 1);
        }

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());
        entries.sort((x, y) -> y.getValue() - x.getValue());

        for (Map.Entry<String, Integer> e : entries) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
    }

    public static void main(String[] args) {
        printFilteredWordFrequency("The mentor was great, the session was great and clear.");
    }
}
