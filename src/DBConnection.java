import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521/FREEPDB1";

    private static final String USER =
            System.getenv("PAYROLL_DB_USER");

    private static final String PASS =
            System.getenv("PAYROLL_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {

        if (USER == null || PASS == null) {
            throw new SQLException(
                    "Database credentials are not configured."
            );
        }

        return DriverManager.getConnection(URL, USER, PASS);
    }
}