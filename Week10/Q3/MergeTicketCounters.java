class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
    }
}

public class MergeTicketCounters {

    // Time O(m + n): every node is attached exactly once. Space O(1): only a dummy and a tail pointer.
    // Copying both lists into an array and sorting would cost O((m + n) log(m + n)) time
    // and O(m + n) extra space, and it would throw away the fact that both inputs are already sorted.
    static Node mergeSorted(Node a, Node b) {
        Node dummy = new Node(0);
        Node tail = dummy;

        while (a != null && b != null) {
            if (a.data <= b.data) {
                tail.next = a;
                a = a.next;
            } else {
                tail.next = b;
                b = b.next;
            }
            tail = tail.next;
        }

        tail.next = (a != null) ? a : b;
        return dummy.next;
    }

    static Node build(int... values) {
        Node dummy = new Node(0);
        Node tail = dummy;
        for (int v : values) {
            tail.next = new Node(v);
            tail = tail.next;
        }
        return dummy.next;
    }

    static void print(Node head) {
        StringBuilder sb = new StringBuilder();
        for (Node cur = head; cur != null; cur = cur.next) {
            sb.append(cur.data);
            if (cur.next != null) {
                sb.append(" -> ");
            }
        }
        System.out.println(sb.length() == 0 ? "(empty)" : sb.toString());
    }

    public static void main(String[] args) {
        print(mergeSorted(build(3, 8, 15), build(5, 8, 12, 20)));
        print(mergeSorted(build(), build(4)));
        print(mergeSorted(build(), build()));
    }
}
