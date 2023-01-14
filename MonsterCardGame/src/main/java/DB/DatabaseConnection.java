package DB;

import java.sql.*;

public class DatabaseConnection {

     static Connection _connection = null;
    private static final String url = "jdbc:postgresql://localhost:5432/cardgame";
    private static final String user ="postgres";
    private static final String password="pwd123456";
    public static void openConnection() throws SQLException {
        _connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connection established...");
    }
    static void closeConnection() throws SQLException {
        _connection.close();
        _connection = null;
        System.out.println("Connection terminated...");
    }





}
