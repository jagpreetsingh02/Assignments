import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

enum LeaveStatus {
    PENDING, APPROVED, REJECTED;

    boolean canMoveTo(LeaveStatus next) {
        return this == PENDING && next != PENDING;
    }

    String label() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}

abstract class Employee {
    private final String name;

    protected Employee(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    abstract int maxDaysPerRequest();

    abstract String type();
}

class FullTimeEmployee extends Employee {
    FullTimeEmployee(String name) {
        super(name);
    }

    @Override
    int maxDaysPerRequest() {
        return 15;
    }

    @Override
    String type() {
        return "Full-time";
    }
}

class PartTimeEmployee extends Employee {
    PartTimeEmployee(String name) {
        super(name);
    }

    @Override
    int maxDaysPerRequest() {
        return 7;
    }

    @Override
    String type() {
        return "Part-time";
    }
}

class ContractEmployee extends Employee {
    ContractEmployee(String name) {
        super(name);
    }

    @Override
    int maxDaysPerRequest() {
        return 3;
    }

    @Override
    String type() {
        return "Contract";
    }
}

class LeaveRequest {
    private final Employee employee;
    private final LocalDate from;
    private final LocalDate to;
    private LeaveStatus status = LeaveStatus.PENDING;

    LeaveRequest(Employee employee, LocalDate from, LocalDate to) {
        this.employee = employee;
        this.from = from;
        this.to = to;
    }

    long days() {
        return ChronoUnit.DAYS.between(from, to) + 1;
    }

    boolean moveTo(LeaveStatus next) {
        if (!status.canMoveTo(next)) {
            System.out.println("Cannot change status: " + status.label()
                    + " request cannot revert to " + next.label() + ".");
            return false;
        }
        status = next;
        return true;
    }

    Employee getEmployee() {
        return employee;
    }

    LocalDate getFrom() {
        return from;
    }

    LocalDate getTo() {
        return to;
    }

    LeaveStatus getStatus() {
        return status;
    }
}

class LeaveManager {
    private final List<LeaveRequest> requests = new ArrayList<>();

    LeaveRequest submit(Employee employee, LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            System.out.println("Leave request rejected at submission: end date is before start date.");
            return null;
        }
        LeaveRequest request = new LeaveRequest(employee, from, to);
        requests.add(request);
        System.out.println("Leave request submitted by " + employee.getName() + " for "
                + from + " to " + to + ". Status: " + request.getStatus().label() + ".");
        return request;
    }

    void review(LeaveRequest request, boolean approve) {
        if (request.getStatus() != LeaveStatus.PENDING) {
            System.out.println("Request for " + request.getEmployee().getName() + " has already been reviewed.");
            return;
        }

        Employee e = request.getEmployee();
        LeaveStatus decision = approve ? LeaveStatus.APPROVED : LeaveStatus.REJECTED;
        if (approve && request.days() > e.maxDaysPerRequest()) {
            System.out.println(e.type() + " policy allows at most " + e.maxDaysPerRequest()
                    + " days per request; " + request.days() + " requested.");
            decision = LeaveStatus.REJECTED;
        }

        if (request.moveTo(decision)) {
            System.out.println("Leave request for " + e.getName() + " " + decision.label().toLowerCase()
                    + ". Status: " + decision.label() + ".");
        }
    }

    void changeStatus(LeaveRequest request, LeaveStatus next) {
        if (request.moveTo(next)) {
            System.out.println("Leave request for " + request.getEmployee().getName()
                    + " is now " + next.label() + ".");
        }
    }
}

public class LeaveManagementSystem {
    public static void main(String[] args) {
        LeaveManager manager = new LeaveManager();

        Employee john = new FullTimeEmployee("John Doe");
        Employee jane = new PartTimeEmployee("Jane Smith");
        Employee sam = new ContractEmployee("Sam Wilson");

        LeaveRequest johnRequest = manager.submit(john, LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 12));
        manager.review(johnRequest, true);
        manager.submit(jane, LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 5));
        manager.changeStatus(johnRequest, LeaveStatus.PENDING);

        System.out.println();
        LeaveRequest samRequest = manager.submit(sam, LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 6));
        manager.review(samRequest, true);
        manager.changeStatus(samRequest, LeaveStatus.APPROVED);
    }
}
