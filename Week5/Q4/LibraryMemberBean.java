import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class LibraryMember {
    private String membershipId;
    private String name;
    private boolean premiumMember;
    private String securityAnswerHash;

    public LibraryMember() {
        this(null);
    }

    public LibraryMember(String name) {
        this(null, name);
    }

    public LibraryMember(String membershipId, String name) {
        this.membershipId = membershipId;
        this.name = name;
        this.premiumMember = false;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String id) {
        if (this.membershipId == null && id != null && !id.trim().isEmpty()) {
            this.membershipId = id.trim();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPremiumMember() {
        return premiumMember;
    }

    public void setPremiumMember(boolean premium) {
        this.premiumMember = premium;
    }

    public void setSecurityAnswer(String answer) {
        this.securityAnswerHash = hash(answer);
    }

    public boolean verifySecurityAnswer(String attempt) {
        return securityAnswerHash != null && securityAnswerHash.equals(hash(attempt));
    }

    private static String hash(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(value.trim().toLowerCase().getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String toString() {
        return "LibraryMember[id=" + membershipId + ", name=" + name + ", premium=" + premiumMember + "]";
    }
}

public class LibraryMemberBean {
    public static void main(String[] args) {
        System.out.println(new LibraryMember("Priya Nair").getMembershipId());
        System.out.println(new LibraryMember("LIB-8841", "Priya Nair").getMembershipId());

        LibraryMember m = new LibraryMember();
        m.setMembershipId("LIB-8841");
        m.setMembershipId("FAKE-0000");
        System.out.println(m.getMembershipId());

        m.setName("Priya Nair");
        m.setPremiumMember(true);
        m.setSecurityAnswer("Bangalore");
        System.out.println(m);
        System.out.println("Correct answer accepted: " + m.verifySecurityAnswer("bangalore"));
        System.out.println("Wrong answer accepted: " + m.verifySecurityAnswer("Chennai"));
    }
}
