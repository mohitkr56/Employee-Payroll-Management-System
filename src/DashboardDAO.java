
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO {

    // ================= DASHBOARD STATISTICS =================

    public Object[] getDashboardStats() {

        String sql =
                "SELECT " +
                        "(SELECT COUNT(*) FROM employee) AS total_employees, " +
                        "(SELECT COUNT(*) FROM payroll) AS total_payroll, " +
                        "(SELECT NVL(SUM(gross_salary), 0) FROM payroll) AS total_gross, " +
                        "(SELECT NVL(SUM(net_salary), 0) FROM payroll) AS total_net " +
                        "FROM dual";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new Object[]{
                        rs.getInt("total_employees"),
                        rs.getInt("total_payroll"),
                        rs.getDouble("total_gross"),
                        rs.getDouble("total_net")
                };
            }

        } catch (SQLException e) {
            System.err.println("Dashboard Statistics Error: " + e.getMessage());
        }

        return new Object[]{0, 0, 0.0, 0.0};
    }

    // ================= MONTHLY PAYROLL TREND =================

    public List<Object[]> getMonthlyPayrollTrend() {

        List<Object[]> data = new ArrayList<>();

        String sql =
                "SELECT TO_CHAR(month_date, 'Mon') AS month_name, " +
                        "       NVL(SUM(p.net_salary), 0) AS total_salary " +
                        "FROM (" +
                        "   SELECT ADD_MONTHS(TRUNC(SYSDATE, 'MM'), -LEVEL + 1) month_date " +
                        "   FROM dual CONNECT BY LEVEL <= 6" +
                        ") m " +
                        "LEFT JOIN payroll p " +
                        "ON TRUNC(p.payment_date, 'MM') = m.month_date " +
                        "GROUP BY month_date " +
                        "ORDER BY month_date";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                data.add(new Object[]{
                        rs.getString("month_name"),
                        rs.getDouble("total_salary")
                });
            }

        } catch (SQLException e) {
            System.err.println("Monthly Trend Error: " + e.getMessage());
        }

        return data;
    }

    // ================= DEPARTMENT PAYROLL =================

    public List<Object[]> getDepartmentPayroll() {

        List<Object[]> data = new ArrayList<>();

        String sql =
                "SELECT NVL(e.department, 'Unassigned') AS department, " +
                        "       NVL(SUM(p.net_salary), 0) AS total_salary " +
                        "FROM employee e " +
                        "LEFT JOIN payroll p ON e.emp_id = p.emp_id " +
                        "GROUP BY e.department " +
                        "ORDER BY total_salary DESC, e.department";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                data.add(new Object[]{
                        rs.getString("department"),
                        rs.getDouble("total_salary")
                });
            }

        } catch (SQLException e) {
            System.err.println("Department Payroll Error: " + e.getMessage());
        }

        return data;
    }

    // ================= RECENT PAYROLL RECORDS =================

    public List<Object[]> getRecentPayrolls() {

        List<Object[]> data = new ArrayList<>();

        String sql =
                "SELECT * FROM (" +
                        "   SELECT p.payroll_id, e.emp_name, e.department, " +
                        "          p.payroll_month, p.payment_date, p.net_salary " +
                        "   FROM payroll p " +
                        "   JOIN employee e ON p.emp_id = e.emp_id " +
                        "   ORDER BY p.payment_date DESC NULLS LAST, p.payroll_id DESC" +
                        ") WHERE ROWNUM <= 8";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                data.add(new Object[]{
                        rs.getInt("payroll_id"),
                        rs.getString("emp_name"),
                        rs.getString("department"),
                        rs.getString("payroll_month"),
                        rs.getDate("payment_date"),
                        rs.getDouble("net_salary")
                });
            }

        } catch (SQLException e) {
            System.err.println("Recent Payroll Error: " + e.getMessage());
        }

        return data;
    }
}
