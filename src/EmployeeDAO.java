
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // ================= ADD EMPLOYEE =================

    public boolean addEmployee(Employee emp) {

        String sql = "INSERT INTO employee "
                + "(emp_name, email, department, designation) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emp.getEmpName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getDepartment());
            ps.setString(4, emp.getDesignation());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Add Employee Error: " + e.getMessage());
            return false;
        }
    }

    // ================= VIEW ALL EMPLOYEES =================

    public List<Employee> getAllEmployees() {

        List<Employee> employees = new ArrayList<>();

        String sql = "SELECT emp_id, emp_name, email, department, designation "
                + "FROM employee ORDER BY emp_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Employee emp = new Employee();

                emp.setEmpId(rs.getInt("emp_id"));
                emp.setEmpName(rs.getString("emp_name"));
                emp.setEmail(rs.getString("email"));
                emp.setDepartment(rs.getString("department"));
                emp.setDesignation(rs.getString("designation"));

                employees.add(emp);
            }

        } catch (SQLException e) {
            System.err.println("View Employees Error: " + e.getMessage());
        }

        return employees;
    }

    // ================= UPDATE EMPLOYEE =================

    public boolean updateEmployee(Employee emp) {

        String sql = "UPDATE employee SET emp_name = ?, email = ?, "
                + "department = ?, designation = ? WHERE emp_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, emp.getEmpName());
            ps.setString(2, emp.getEmail());
            ps.setString(3, emp.getDepartment());
            ps.setString(4, emp.getDesignation());
            ps.setInt(5, emp.getEmpId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Update Employee Error: " + e.getMessage());
            return false;
        }
    }

    // ================= DELETE EMPLOYEE =================

    public boolean deleteEmployee(int empId) {

        String sql = "DELETE FROM employee WHERE emp_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Delete Employee Error: " + e.getMessage());
            return false;
        }
    }
}
