class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
    }
}

public class PalindromeTrainCode {

    // Time O(n): find the middle (n/2), reverse (n/2), compare (n/2), reverse back (n/2).
    // Space O(1): only a few pointers. Copying into an array is also O(n) time,
    // but it needs O(n) extra memory, which the constraint does not allow.
    static boolean isPalindrome(Node head) {
        if (head == null || head.next == null) {
            return true;
        }

        Node slow = head;
        Node fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        Node secondHalf = reverse(slow.next);

        boolean palindrome = true;
        Node left = head;
        Node right = secondHalf;
        while (right != null) {
            if (left.data != right.data) {
                palindrome = false;
                break;
            }
            left = left.next;
            right = right.next;
        }

        slow.next = reverse(secondHalf);
        return palindrome;
    }

    static Node reverse(Node head) {
        Node prev = null;
        Node cur = head;
        while (cur != null) {
            Node next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        return prev;
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

    static String asString(Node head) {
        StringBuilder sb = new StringBuilder();
        for (Node cur = head; cur != null; cur = cur.next) {
            sb.append(cur.data);
            if (cur.next != null) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Node[] codes = {
                build(1, 2, 3, 2, 1),
                build(1, 2, 3, 2),
                build(1, 2, 2, 1),
                build(7)
        };

        for (Node code : codes) {
            boolean result = isPalindrome(code);
            System.out.println(result + " (list afterwards: " + asString(code) + ")");
        }
    }
}
