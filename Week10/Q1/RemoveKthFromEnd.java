class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
    }
}

public class RemoveKthFromEnd {

    // Time O(n), single pass. Space O(1).
    // The dummy node sits before head, so when k equals the length the trailing
    // pointer stops at the dummy and removing the head is the same unlink as any other node.
    static Node removeKthFromEnd(Node head, int k) {
        Node dummy = new Node(0);
        dummy.next = head;

        Node lead = dummy;
        Node trail = dummy;

        for (int i = 0; i <= k; i++) {
            lead = lead.next;
        }

        while (lead != null) {
            lead = lead.next;
            trail = trail.next;
        }

        trail.next = trail.next.next;
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
        print(removeKthFromEnd(build(10, 20, 30, 40, 50), 2));
        print(removeKthFromEnd(build(10, 20, 30, 40, 50), 5));
        print(removeKthFromEnd(build(10, 20, 30, 40, 50), 1));
        print(removeKthFromEnd(build(10), 1));
    }
}
