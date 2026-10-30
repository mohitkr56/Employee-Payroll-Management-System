import java.sql.Date;

public class Payroll {

    private int payrollId;
    private int empId;
    private double basicSalary;
    private double hra;
    private double allowances;
    private double deductions;
    private double grossSalary;
    private double netSalary;
    private String payrollMonth;
    private Date paymentDate;
    private String employeeName;
    private String department;

    public Payroll() {
    }

    public Payroll(int empId, double basicSalary, double hra,
                   double allowances, double deductions,
                   String payrollMonth) {

        this.empId = empId;
        this.basicSalary = basicSalary;
        this.hra = hra;
        this.allowances = allowances;
        this.deductions = deductions;
        this.payrollMonth = payrollMonth;

        calculateSalary();
    }

    public void calculateSalary() {
        grossSalary = basicSalary + hra + allowances;
        netSalary = grossSalary - deductions;
    }

    public int getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(int payrollId) {
        this.payrollId = payrollId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
        calculateSalary();
    }

    public double getHra() {
        return hra;
    }

    public void setHra(double hra) {
        this.hra = hra;
        calculateSalary();
    }

    public double getAllowances() {
        return allowances;
    }

    public void setAllowances(double allowances) {
        this.allowances = allowances;
        calculateSalary();
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
        calculateSalary();
    }

    public double getGrossSalary() {
        return grossSalary;
    }

    public double getNetSalary() {
        return netSalary;
    }

    public String getPayrollMonth() {
        return payrollMonth;
    }

    public void setPayrollMonth(String payrollMonth) {
        this.payrollMonth = payrollMonth;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}