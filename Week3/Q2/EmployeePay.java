class Employee {
    private int empId;
    private String empName;
    private double salary;

    Employee(int empId, String empName, double salary) {
        this.empId = empId;
        this.empName = empName;
        this.salary = salary;
    }

    double getSalary() {
        return salary;
    }

    String getEmpName() {
        return empName;
    }

    int getEmpId() {
        return empId;
    }
}

class ManagerEmployee extends Employee {
    private double teamBonus;

    ManagerEmployee(int empId, String empName, double salary, double teamBonus) {
        super(empId, empName, salary);
        this.teamBonus = teamBonus;
    }

    double effectiveSalary() {
        return getSalary() + teamBonus;
    }
}

class InternEmployee extends Employee {
    private double stipendCap;

    InternEmployee(int empId, String empName, double salary, double stipendCap) {
        super(empId, empName, salary);
        this.stipendCap = stipendCap;
    }

    double effectiveSalary() {
        return Math.min(getSalary(), stipendCap);
    }
}

public class EmployeePay {

    static void printPay(Employee e) {
        if (e instanceof ManagerEmployee) {
            System.out.println("Manager effective pay: Rs " + ((ManagerEmployee) e).effectiveSalary());
        } else if (e instanceof InternEmployee) {
            System.out.println("Intern effective pay: Rs " + ((InternEmployee) e).effectiveSalary());
        } else {
            System.out.println("Plain employee pay: Rs " + e.getSalary());
        }
    }

    public static void main(String[] args) {
        Employee[] staff = {
                new Employee(1, "Ravi", 40000),
                new ManagerEmployee(2, "Divya", 70000, 8000),
                new InternEmployee(3, "Meera", 12000, 10000)
        };

        for (Employee e : staff) {
            printPay(e);
        }
    }
}
