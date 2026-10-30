
public class Employee {

    private int empId;
    private String empName;
    private String email;
    private String department;
    private String designation;

    // ================= CONSTRUCTORS =================

    public Employee() {
    }

    public Employee(String empName, String email,
                    String department, String designation) {

        this.empName = empName;
        this.email = email;
        this.department = department;
        this.designation = designation;
    }

    // ================= GETTERS AND SETTERS =================

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    // ================= DISPLAY EMPLOYEE =================

    @Override
    public String toString() {

        return empId + " | " + empName + " | " + email
                + " | " + department + " | " + designation;
    }
}
