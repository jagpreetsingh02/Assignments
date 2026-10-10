public class TypingAccuracyChecker {

    static void checkTypingAccuracy(String original, String typed) {
        if (original.length() != typed.length()) {
            System.out.println("Both strings must be of equal length");
            return;
        }

        int total = original.length();
        int matched = 0;
        int firstMismatch = -1;

        for (int i = 0; i < total; i++) {
            if (original.charAt(i) == typed.charAt(i)) {
                matched++;
            } else if (firstMismatch == -1) {
                firstMismatch = i;
            }
        }

        double accuracy = total == 0 ? 100.0 : (matched * 100.0) / total;
        String result = String.format("Matched: %d/%d | Accuracy: %.2f%% | ", matched, total, accuracy);

        if (firstMismatch == -1) {
            result += "No Mismatches";
        } else {
            result += "First Mismatch at position " + (firstMismatch + 1)
                    + " ('" + original.charAt(firstMismatch) + "' vs '" + typed.charAt(firstMismatch) + "')";
        }
        System.out.println(result);
    }

    public static void main(String[] args) {
        checkTypingAccuracy("hello world", "hello worlt");
        checkTypingAccuracy("coding", "coding");
    }
}
