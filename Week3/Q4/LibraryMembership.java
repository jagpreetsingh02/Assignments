// name: static means every member shares one name, so a new member replaces the old one.
// memberId: an ID is supposed to identify one person; as static there is only one ID for everybody.
// booksIssued: each member borrows their own books; a shared counter mixes everyone's count together.
class BrokenLibraryMember {
    static String name;
    static String memberId;
    static int booksIssued;

    BrokenLibraryMember(String name, String memberId, int booksIssued) {
        BrokenLibraryMember.name = name;
        BrokenLibraryMember.memberId = memberId;
        BrokenLibraryMember.booksIssued = booksIssued;
    }

    String getName() {
        return name;
    }
}

class LibraryMember {
    static String libraryName = "City Central Library";
    static int memberCount = 0;

    String name;
    String memberId;
    int booksIssued;

    LibraryMember(String name, int booksIssued) {
        memberCount++;
        this.name = name;
        this.memberId = "LM-" + (1000 + memberCount);
        this.booksIssued = booksIssued;
    }

    void printMemberCard() {
        System.out.println(name + " | " + memberId);
    }

    static void printTotalMembers() {
        System.out.println("Total members: " + memberCount);
    }
}

public class LibraryMembership {
    public static void main(String[] args) {
        System.out.println("Broken version:");
        BrokenLibraryMember b1 = new BrokenLibraryMember("Aditi", "LM-1001", 2);
        BrokenLibraryMember b2 = new BrokenLibraryMember("Rohan", "LM-1002", 1);
        System.out.println(b1.getName());
        System.out.println(b2.getName());

        System.out.println();
        System.out.println("Fixed version:");
        LibraryMember m1 = new LibraryMember("Aditi", 2);
        LibraryMember m2 = new LibraryMember("Rohan", 1);
        m1.printMemberCard();
        m2.printMemberCard();
        LibraryMember.printTotalMembers();
    }
}
