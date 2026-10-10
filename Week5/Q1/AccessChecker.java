class LibraryMember {
    private final String membershipId;
    String branchCode;
    protected double finesOwed;
    public String displayName;

    public LibraryMember(String membershipId, String branchCode, double finesOwed, String displayName) {
        if (membershipId == null || membershipId.trim().length() < 4) {
            throw new IllegalArgumentException("membershipId must be at least 4 non-blank characters");
        }
        this.membershipId = membershipId.trim();
        this.branchCode = branchCode;
        this.finesOwed = finesOwed;
        this.displayName = displayName;
    }

    public String getMembershipId() {
        return membershipId;
    }
}

public class AccessChecker {

    static final String[] MODIFIERS = {"private", "default", "protected", "public"};

    static String classifyAccess(String fieldModifier, String accessorContext) {
        boolean allowed;
        switch (fieldModifier) {
            case "private":
                allowed = accessorContext.equals("SAME_CLASS");
                break;
            case "default":
            case "protected":
                allowed = accessorContext.equals("SAME_CLASS") || accessorContext.equals("SAME_PACKAGE");
                break;
            case "public":
                allowed = true;
                break;
            default:
                throw new IllegalArgumentException("Unknown modifier: " + fieldModifier);
        }
        return allowed ? "ALLOWED" : "DENIED";
    }

    static String summarizeByModifier(String[][] attempts) {
        int[] allowed = new int[MODIFIERS.length];
        int[] denied = new int[MODIFIERS.length];

        for (String[] attempt : attempts) {
            int idx = indexOf(attempt[0]);
            if (classifyAccess(attempt[0], attempt[1]).equals("ALLOWED")) {
                allowed[idx]++;
            } else {
                denied[idx]++;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MODIFIERS.length; i++) {
            if (allowed[i] + denied[i] == 0) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(" | ");
            }
            sb.append(MODIFIERS[i]).append(": ")
              .append(allowed[i]).append(" allowed / ")
              .append(denied[i]).append(" denied");
        }
        return sb.toString();
    }

    private static int indexOf(String modifier) {
        for (int i = 0; i < MODIFIERS.length; i++) {
            if (MODIFIERS[i].equals(modifier)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unknown modifier: " + modifier);
    }

    public static void main(String[] args) {
        System.out.println(classifyAccess("private", "SAME_CLASS"));
        System.out.println(classifyAccess("protected", "DIFFERENT_PACKAGE"));

        String[][] attempts = {
                {"private", "SAME_CLASS"},
                {"private", "SAME_PACKAGE"},
                {"default", "SAME_PACKAGE"},
                {"default", "DIFFERENT_PACKAGE"},
                {"protected", "SAME_PACKAGE"},
                {"protected", "SAME_CLASS"},
                {"public", "DIFFERENT_PACKAGE"}
        };
        System.out.println(summarizeByModifier(attempts));

        String[] ids = {"LB9", "LB94", "    "};
        for (String id : ids) {
            try {
                LibraryMember m = new LibraryMember(id, "BR1", 0, "Priya Nair");
                System.out.println("\"" + id + "\" -> created " + m.getMembershipId());
            } catch (IllegalArgumentException e) {
                System.out.println("\"" + id + "\" -> construction rejected");
            }
        }
    }
}
