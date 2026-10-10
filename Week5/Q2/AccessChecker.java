public class AccessChecker {

    static String classifyAccess(String fieldModifier, String accessorContext) {
        boolean sameClass = accessorContext.equals("SAME_CLASS");
        boolean samePackage = sameClass || accessorContext.equals("SAME_PACKAGE");
        boolean subclassOwnType = accessorContext.equals("SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE");

        boolean allowed;
        switch (fieldModifier) {
            case "private":
                allowed = sameClass;
                break;
            case "default":
                allowed = samePackage;
                break;
            case "protected":
                // A subclass in another package may only reach a protected member through
                // a reference whose declared type is the subclass itself (or its own subtype).
                allowed = samePackage || subclassOwnType;
                break;
            case "public":
                allowed = true;
                break;
            default:
                throw new IllegalArgumentException("Unknown modifier: " + fieldModifier);
        }
        return allowed ? "ALLOWED" : "DENIED";
    }

    static String describeContext(String accessorContext) {
        String[] parts = accessorContext.toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String[] modifiers = {"private", "default", "protected", "public"};
        String[] contexts = {
                "SAME_CLASS",
                "SAME_PACKAGE",
                "DIFFERENT_PACKAGE",
                "SUBCLASS_DIFFERENT_PACKAGE_OWN_TYPE",
                "SUBCLASS_DIFFERENT_PACKAGE_PARENT_TYPE"
        };

        for (String ctx : contexts) {
            System.out.println(describeContext(ctx));
            for (String mod : modifiers) {
                System.out.println("  " + mod + ": " + classifyAccess(mod, ctx));
            }
        }
    }
}
