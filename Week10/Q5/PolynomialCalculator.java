class Node {
    int coeff;
    int exp;
    Node next;

    Node(int coeff, int exp) {
        this.coeff = coeff;
        this.exp = exp;
    }
}

public class PolynomialCalculator {

    // Time O(m + n): each term of p and q is visited once. Space O(m + n) for the result
    // list in the worst case (no matching exponents); no other extra space is used.
    static Node addPolynomials(Node p, Node q) {
        Node dummy = new Node(0, 0);
        Node tail = dummy;

        while (p != null || q != null) {
            int coeff;
            int exp;

            if (q == null || (p != null && p.exp > q.exp)) {
                coeff = p.coeff;
                exp = p.exp;
                p = p.next;
            } else if (p == null || q.exp > p.exp) {
                coeff = q.coeff;
                exp = q.exp;
                q = q.next;
            } else {
                coeff = p.coeff + q.coeff;
                exp = p.exp;
                p = p.next;
                q = q.next;
            }

            if (coeff != 0) {
                tail.next = new Node(coeff, exp);
                tail = tail.next;
            }
        }
        return dummy.next;
    }

    static String format(Node head) {
        if (head == null) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        for (Node t = head; t != null; t = t.next) {
            int abs = Math.abs(t.coeff);

            if (sb.length() == 0) {
                if (t.coeff < 0) {
                    sb.append("-");
                }
            } else {
                sb.append(t.coeff < 0 ? " - " : " + ");
            }

            if (abs != 1 || t.exp == 0) {
                sb.append(abs);
            }
            if (t.exp >= 1) {
                sb.append("x");
            }
            if (t.exp > 1) {
                sb.append("^").append(t.exp);
            }
        }
        return sb.toString();
    }

    static Node build(int[][] terms) {
        Node dummy = new Node(0, 0);
        Node tail = dummy;
        for (int[] term : terms) {
            tail.next = new Node(term[0], term[1]);
            tail = tail.next;
        }
        return dummy.next;
    }

    public static void main(String[] args) {
        Node p1 = build(new int[][]{{5, 3}, {4, 2}, {2, 0}});
        Node q1 = build(new int[][]{{5, 2}, {-3, 1}, {1, 0}});
        System.out.println(format(addPolynomials(p1, q1)));

        Node p2 = build(new int[][]{{3, 2}, {1, 0}});
        Node q2 = build(new int[][]{{-3, 2}, {4, 1}});
        System.out.println(format(addPolynomials(p2, q2)));

        Node p3 = build(new int[][]{{2, 1}});
        Node q3 = build(new int[][]{{-2, 1}});
        System.out.println(format(addPolynomials(p3, q3)));
    }
}
