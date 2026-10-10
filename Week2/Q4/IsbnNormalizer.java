public class IsbnNormalizer {

    static String normalizeCode(String raw) {
        String code = raw.trim();
        if (code.length() < 3) {
            return code;
        }
        return code.substring(0, 3).toUpperCase() + code.substring(3);
    }

    static String validateAndFormat(String code) {
        if (code.length() != 13) {
            return "Invalid: code must be exactly 13 characters";
        }

        for (int i = 0; i < 3; i++) {
            if (!Character.isLetter(code.charAt(i))) {
                return "Invalid: publisher code must be 3 letters";
            }
        }

        for (int i = 3; i < 13; i++) {
            if (!Character.isDigit(code.charAt(i))) {
                return "Invalid: year and catalog number must be digits";
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[").append(code.substring(0, 3)).append("]")
          .append(" YEAR: ").append(code.substring(3, 7))
          .append(" | CATALOG: ").append(code.substring(7));
        return sb.toString();
    }

    public static void main(String[] args) {
        String[] inputs = {"  pen2026004251  ", "12N2026004251", "abc2026", "LIB2026A04251"};

        for (String raw : inputs) {
            System.out.println(validateAndFormat(normalizeCode(raw)));
        }
    }
}
