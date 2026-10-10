class Canteen {
    private static final int DEFAULT_TRUST_SCORE = 3;

    private final String canteenCode;
    private final String canteenName;
    private final int trustScore;

    public Canteen(String canteenCode, String canteenName, int trustScore) {
        this.canteenCode = canteenCode;
        this.canteenName = canteenName;
        this.trustScore = trustScore;
    }

    public Canteen(String canteenCode, String canteenName) {
        this(canteenCode, canteenName, DEFAULT_TRUST_SCORE);
    }

    // Order: higher trust score first, then canteen code ignoring case,
    // then shorter name first. Codes like "hb1-c" and "HB1-C" count as the same
    // code for ordering, but the stored code itself is never changed.
    int compareTo(Canteen other) {
        if (this.trustScore != other.trustScore) {
            return Integer.compare(other.trustScore, this.trustScore);
        }
        int byCode = this.canteenCode.compareToIgnoreCase(other.canteenCode);
        if (byCode != 0) {
            return byCode;
        }
        return Integer.compare(this.canteenName.length(), other.canteenName.length());
    }

    static Canteen[] rankCanteens(Canteen[] canteens) {
        Canteen[] ranked = canteens.clone();

        for (int i = 1; i < ranked.length; i++) {
            Canteen key = ranked[i];
            int j = i - 1;
            while (j >= 0 && ranked[j].compareTo(key) > 0) {
                ranked[j + 1] = ranked[j];
                j--;
            }
            ranked[j + 1] = key;
        }
        return ranked;
    }

    String getCanteenCode() {
        return canteenCode;
    }
}

public class CanteenRanking {
    public static void main(String[] args) {
        Canteen[] input = {
                new Canteen("HB3-C", "Spice Junction", 3),
                new Canteen("hb1-c", "Grand Mess", 5),
                new Canteen("HB2-C", "Southern Treats")
        };

        Canteen[] ranked = Canteen.rankCanteens(input);

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < ranked.length; i++) {
            sb.append("\"").append(ranked[i].getCanteenCode()).append("\"");
            if (i < ranked.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        System.out.println(sb);
    }
}
