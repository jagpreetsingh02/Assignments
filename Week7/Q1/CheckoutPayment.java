abstract class PaymentMethod {
    private static int counter = 1000;

    private final String transactionId;

    protected PaymentMethod() {
        counter++;
        this.transactionId = "TXN-" + counter;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public abstract String processPayment(double amount);

    String processPayment(double amount, String note) {
        return processPayment(amount) + " (" + note + ")";
    }
}

class CreditCardPayment extends PaymentMethod {
    private final String cardNumberLastFour;

    public CreditCardPayment(String cardNumberLastFour) {
        this.cardNumberLastFour = cardNumberLastFour;
    }

    @Override
    public String processPayment(double amount) {
        return "Charged $" + amount + " to card ending " + cardNumberLastFour + " - Txn " + getTransactionId();
    }
}

class CashPayment extends PaymentMethod {

    public CashPayment() {
        super();
    }

    @Override
    public String processPayment(double amount) {
        return "Received $" + amount + " in cash - Txn " + getTransactionId();
    }
}

public class CheckoutPayment {

    static void printConfirmation(PaymentMethod payment, double amount) {
        System.out.println(payment.processPayment(amount));
    }

    static void testUpcasting(CreditCardPayment cc) {
        // Upcasting: a CreditCardPayment stored in a PaymentMethod variable.
        PaymentMethod ref = cc;
        printConfirmation(ref, 250.0);
    }

    public static void main(String[] args) {
        CreditCardPayment cc = new CreditCardPayment("4471");
        System.out.println(cc.processPayment(250.0));

        CashPayment cash = new CashPayment();
        System.out.println(cash.processPayment(40.0));

        System.out.println(cc.processPayment(250.0, "Birthday gift"));

        testUpcasting(cc);

        // PaymentMethod p = new PaymentMethod();   // does not compile: PaymentMethod is abstract
    }
}
