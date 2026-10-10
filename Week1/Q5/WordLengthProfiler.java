public class WordLengthProfiler {

    static void classifyWordLengths(String review) {
        int shortCount = 0;
        int mediumCount = 0;
        int longCount = 0;

        String[] words = review.trim().split("\\s+");

        for (String word : words) {
            int letters = 0;
            for (int i = 0; i < word.length(); i++) {
                if (Character.isLetter(word.charAt(i))) {
                    letters++;
                }
            }

            if (letters == 0) {
                continue;
            } else if (letters <= 4) {
                shortCount++;
            } else if (letters <= 8) {
                mediumCount++;
            } else {
                longCount++;
            }
        }

        System.out.println("Short: " + shortCount + " | Medium: " + mediumCount + " | Long: " + longCount);
    }

    public static void main(String[] args) {
        classifyWordLengths("This movie was absolutely fantastic and thrilling");
        classifyWordLengths("Worst film ever, a complete waste of time!");
    }
}
