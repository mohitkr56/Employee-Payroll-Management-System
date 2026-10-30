import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) {

        String sql = """
                SELECT emp_id, emp_name, department
                FROM employee
                ORDER BY emp_id
                """;

        try (Connection con = DBConnection.getConnection()) {

            System.out.println("Connected to Oracle Database!");

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("\n--- Employee Records ---");

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    System.out.println(
                            rs.getInt("emp_id") + " | " +
                                    rs.getString("emp_name") + " | " +
                                    rs.getString("department")
                    );
                }

                if (!found) {
                    System.out.println("No employee records found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database operation failed!");
            e.printStackTrace();
        }
    }
}