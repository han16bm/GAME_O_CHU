package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private final String url = "jdbc:mysql://localhost:3306/ochu";
    private final String user = "root";
    private final String password = "";
    
    private DBConnection() {
        try {
            // Tải MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Kiểm tra kết nối
            DriverManager.getConnection(url, user, password).close();
            System.out.println("Kết nối database thành công");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Lỗi kết nối đến database: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
