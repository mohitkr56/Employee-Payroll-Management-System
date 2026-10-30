
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PayrollDAO {

    // ================= DUPLICATE PAYROLL CHECK =================

    public boolean payrollExists(int empId, String payrollMonth) {

        String sql = "SELECT COUNT(*) FROM payroll "
                + "WHERE emp_id = ? "
                + "AND UPPER(TRIM(payroll_month)) = UPPER(TRIM(?))";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);
            ps.setString(2, payrollMonth);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Payroll Duplicate Check Error: "
                    + e.getMessage());
        }

        return false;
    }

    // ================= GENERATE PAYROLL =================

    public boolean generatePayroll(Payroll payroll) {

        String sql = "INSERT INTO payroll "
                + "(emp_id, basic_salary, hra, allowances, deductions, "
                + "gross_salary, net_salary, payroll_month) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, payroll.getEmpId());
            ps.setDouble(2, payroll.getBasicSalary());
            ps.setDouble(3, payroll.getHra());
            ps.setDouble(4, payroll.getAllowances());
            ps.setDouble(5, payroll.getDeductions());
            ps.setDouble(6, payroll.getGrossSalary());
            ps.setDouble(7, payroll.getNetSalary());
            ps.setString(8, payroll.getPayrollMonth());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Payroll Generation Error: "
                    + e.getMessage());
            return false;
        }
    }

    // ================= VIEW ALL PAYROLL RECORDS =================

    public ArrayList<Payroll> getAllPayrolls() {

        ArrayList<Payroll> payrollList = new ArrayList<>();

        String sql = "SELECT p.*, e.emp_name, e.department "
                + "FROM payroll p "
                + "JOIN employee e ON p.emp_id = e.emp_id "
                + "ORDER BY p.payroll_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Payroll payroll = new Payroll();

                payroll.setPayrollId(rs.getInt("payroll_id"));
                payroll.setEmpId(rs.getInt("emp_id"));

                // Setting these values automatically recalculates salary.
                payroll.setBasicSalary(rs.getDouble("basic_salary"));
                payroll.setHra(rs.getDouble("hra"));
                payroll.setAllowances(rs.getDouble("allowances"));
                payroll.setDeductions(rs.getDouble("deductions"));

                payroll.setPayrollMonth(rs.getString("payroll_month"));
                payroll.setPaymentDate(rs.getDate("payment_date"));

                payroll.setEmployeeName(rs.getString("emp_name"));
                payroll.setDepartment(rs.getString("department"));

                payrollList.add(payroll);
            }

        } catch (SQLException e) {
            System.err.println("Retrieve Payroll Error: "
                    + e.getMessage());
        }

        return payrollList;
    }

    // ================= PAYROLL BY EMPLOYEE ID =================

    public ArrayList<Payroll> getPayrollByEmployeeId(int empId) {

        ArrayList<Payroll> payrollList = new ArrayList<>();

        String sql = "SELECT p.*, e.emp_name, e.department "
                + "FROM payroll p "
                + "JOIN employee e ON p.emp_id = e.emp_id "
                + "WHERE p.emp_id = ? "
                + "ORDER BY p.payroll_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Payroll payroll = new Payroll();

                    payroll.setPayrollId(rs.getInt("payroll_id"));
                    payroll.setEmpId(rs.getInt("emp_id"));

                    payroll.setBasicSalary(rs.getDouble("basic_salary"));
                    payroll.setHra(rs.getDouble("hra"));
                    payroll.setAllowances(rs.getDouble("allowances"));
                    payroll.setDeductions(rs.getDouble("deductions"));

                    payroll.setPayrollMonth(
                            rs.getString("payroll_month")
                    );

                    payroll.setPaymentDate(rs.getDate("payment_date"));

                    payroll.setEmployeeName(rs.getString("emp_name"));
                    payroll.setDepartment(rs.getString("department"));

                    payrollList.add(payroll);
                }
            }

        } catch (SQLException e) {
            System.err.println("Employee Payroll Retrieval Error: "
                    + e.getMessage());
        }

        return payrollList;
    }

    // ================= PAYROLL SUMMARY =================

    public void displayPayrollSummary() {

        String sql = "SELECT COUNT(*) AS total_records, "
                + "NVL(SUM(gross_salary), 0) AS total_gross, "
                + "NVL(SUM(deductions), 0) AS total_deductions, "
                + "NVL(SUM(net_salary), 0) AS total_net "
                + "FROM payroll";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                System.out.println("\n===== PAYROLL SUMMARY =====");

                System.out.println("Total Payroll Records: "
                        + rs.getInt("total_records"));

                System.out.printf("Total Gross Salary: ₹%.2f%n",
                        rs.getDouble("total_gross"));

                System.out.printf("Total Deductions: ₹%.2f%n",
                        rs.getDouble("total_deductions"));

                System.out.printf("Total Net Salary: ₹%.2f%n",
                        rs.getDouble("total_net"));

                System.out.println("===========================");
            }

        } catch (SQLException e) {
            System.err.println("Payroll Summary Error: "
                    + e.getMessage());
        }
    }
}
