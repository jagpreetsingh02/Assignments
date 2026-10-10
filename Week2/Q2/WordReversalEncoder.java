public class WordReversalEncoder {

    static String reverseEachWord(String sentence) {
        String[] words = sentence.split(" ");
        StringBuilder result = new StringBuilder();

        for (int w = 0; w < words.length; w++) {
            StringBuilder reversed = new StringBuilder();
            for (int i = words[w].length() - 1; i >= 0; i--) {
                reversed.append(words[w].charAt(i));
            }

            result.append(reversed);
            if (w < words.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(reverseEachWord("hello club"));
        System.out.println(reverseEachWord("java is fun"));
    }
}
