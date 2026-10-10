public class BrowserHistory {

    private static class Node {
        String url;
        Node prev, next;

        Node(String url) {
            this.url = url;
        }
    }

    private Node current;

    public BrowserHistory(String homepage) {
        current = new Node(homepage);
    }

    // O(1). The new node is linked right after current, and current.next is overwritten,
    // so the old forward pages lose their last reference from the list and the
    // garbage collector reclaims them. Their prev link only points backwards into the
    // live list, which does not keep them reachable.
    public void visit(String url) {
        Node page = new Node(url);
        page.prev = current;
        current.next = page;
        current = page;
    }

    // O(min(steps, pages behind)).
    public String back(int steps) {
        while (steps > 0 && current.prev != null) {
            current = current.prev;
            steps--;
        }
        return current.url;
    }

    // O(min(steps, pages ahead)).
    public String forward(int steps) {
        while (steps > 0 && current.next != null) {
            current = current.next;
            steps--;
        }
        return current.url;
    }

    // Space is O(p), where p is the number of pages currently reachable in the history.
    public static void main(String[] args) {
        BrowserHistory history = new BrowserHistory("srm.edu");
        history.visit("a.com");
        history.visit("b.com");
        history.visit("c.com");

        System.out.println(history.back(1));
        System.out.println(history.back(1));
        System.out.println(history.forward(1));
        history.visit("d.com");
        System.out.println(history.forward(2));
        System.out.println(history.back(2));
        System.out.println(history.back(7));
    }
}
