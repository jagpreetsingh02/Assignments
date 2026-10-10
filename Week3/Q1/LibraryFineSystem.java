class BookIssue {
    String title;
    String borrowerName;
    int daysOverdue;

    BookIssue(String title, String borrowerName, int daysOverdue) {
        this.title = title;
        this.borrowerName = borrowerName;
        this.daysOverdue = daysOverdue;
    }

    double fineAmount() {
        return daysOverdue > 0 ? daysOverdue * 5 : 0;
    }

    boolean isSeverelyOverdue() {
        return daysOverdue > 14;
    }

    // fineAmount() depends on one book's own daysOverdue, so it is an instance method.
    // totalFineCollected() works across many books and needs no single object's state,
    // so it belongs to the class itself and is static.
    static double totalFineCollected(BookIssue[] issues) {
        double total = 0;
        for (BookIssue issue : issues) {
            total += issue.fineAmount();
        }
        return total;
    }
}

public class LibraryFineSystem {
    public static void main(String[] args) {
        BookIssue[] issues = {
                new BookIssue("Clean Code", "Aman", 18),
                new BookIssue("Effective Java", "Sneha", 5),
                new BookIssue("Refactoring", "Rahul", 0),
                new BookIssue("DSA Handbook", "Kavya", 21),
                new BookIssue("Design Patterns", "Arjun", 9)
        };

        for (BookIssue b : issues) {
            String status = b.isSeverelyOverdue() ? "Severely overdue" : "OK";
            System.out.println(b.title + " - " + b.daysOverdue + " days - " + status);
        }

        System.out.println("Total fine collected: Rs " + BookIssue.totalFineCollected(issues));
    }
}
