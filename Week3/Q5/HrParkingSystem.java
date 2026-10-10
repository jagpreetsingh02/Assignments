class Employee {
    private String empId;
    private String empName;
    private double salary;

    Employee(String empId, String empName, double salary) {
        this.empId = empId;
        this.empName = empName;
        this.salary = salary;
    }

    double getSalary() {
        return salary;
    }
}

class ManagerEmployee extends Employee {
    private double teamBonus;

    ManagerEmployee(String empId, String empName, double salary, double teamBonus) {
        super(empId, empName, salary);
        this.teamBonus = teamBonus;
    }

    double effectiveSalary() {
        return getSalary() + teamBonus;
    }
}

class ParkingSlot {
    String slotNo;
    int capacity;
    int occupiedCount;

    ParkingSlot(String slotNo, int capacity, int occupiedCount) {
        this.slotNo = slotNo;
        this.capacity = capacity;
        this.occupiedCount = occupiedCount;
    }

    boolean allot(String vehicleNo) {
        if (occupiedCount < capacity) {
            occupiedCount++;
            return true;
        }
        return false;
    }

    static ParkingSlot findAvailableSlot(ParkingSlot[] slots) {
        if (slots == null) {
            return null;
        }
        for (ParkingSlot slot : slots) {
            if (slot != null && slot.occupiedCount < slot.capacity) {
                return slot;
            }
        }
        return null;
    }

    static ParkingSlot safeAllot(ParkingSlot[] slots, String vehicleNo) {
        ParkingSlot slot = findAvailableSlot(slots);
        if (slot == null) {
            System.out.println("No slots available for " + vehicleNo);
            return null;
        }
        slot.allot(vehicleNo);
        return slot;
    }
}

class CompanyEmployeeRecord {
    static int totalRecords = 0;

    String name;
    String empId;
    Employee employee;
    ParkingSlot slot;

    CompanyEmployeeRecord(String name, String empId, Employee employee) {
        this.name = name;
        this.empId = empId;
        this.employee = employee;
        totalRecords++;
    }

    void assignSlot(ParkingSlot slot) {
        this.slot = slot;
    }

    double effectivePay() {
        if (employee instanceof ManagerEmployee) {
            return ((ManagerEmployee) employee).effectiveSalary();
        }
        return employee.getSalary();
    }

    String fullProfile() {
        String slotText = (slot == null) ? "no parking assigned" : slot.slotNo;
        return name + " | Pay: Rs " + effectivePay() + " | Slot: " + slotText;
    }
}

public class HrParkingSystem {
    public static void main(String[] args) {
        ParkingSlot[] slots = {
                new ParkingSlot("A1", 1, 0),
                new ParkingSlot("A2", 1, 0)
        };

        CompanyEmployeeRecord divya = new CompanyEmployeeRecord("Divya", "E101",
                new ManagerEmployee("E101", "Divya", 70000, 8000));
        CompanyEmployeeRecord karan = new CompanyEmployeeRecord("Karan", "E102",
                new Employee("E102", "Karan", 40000));
        CompanyEmployeeRecord meera = new CompanyEmployeeRecord("Meera", "E103",
                new Employee("E103", "Meera", 10000));

        divya.assignSlot(ParkingSlot.safeAllot(slots, "KA01AB1111"));
        karan.assignSlot(ParkingSlot.safeAllot(slots, "KA02CD2222"));

        CompanyEmployeeRecord[] records = {divya, karan, meera};
        for (CompanyEmployeeRecord r : records) {
            System.out.println(r.fullProfile());
        }
        System.out.println("Total records: " + CompanyEmployeeRecord.totalRecords);
    }
}
