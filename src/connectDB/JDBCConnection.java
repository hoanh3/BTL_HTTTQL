package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCConnection {

    static String url = "jdbc:sqlserver://localhost:1433;databasename=QLTHUOC;encrypt=true;trustServerCertificate=true;";
    static String user = "sa";
    static String password = "123456";

    public static PreparedStatement getStmt(String sql, Object... args) throws Exception {
        Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement stmt;
        if (sql.trim().startsWith("{")) {
            stmt = con.prepareCall(sql);
        } else {
            stmt = con.prepareStatement(sql);
        }

        for (int i = 0; i < args.length; i++) {
            stmt.setObject(i + 1, args[i]);
        }
        return stmt;
    }

    public static int update(String sql, Object... args) {
        try {
            PreparedStatement stmt = JDBCConnection.getStmt(sql, args);
            try {
                return stmt.executeUpdate();
            } finally {
                stmt.getConnection().close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ResultSet query(String sql, Object... args) throws Exception {
        PreparedStatement stmt = JDBCConnection.getStmt(sql, args);
        return stmt.executeQuery();
    }

    public static void main(String[] args) {
        try {
            // Test query to check connection
            String testQuery = "SELECT 1";
            ResultSet rs = JDBCConnection.query(testQuery);
            if (rs.next()) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Connection failed!");
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection failed with exception: " + e.getMessage());
        }
    }
}
