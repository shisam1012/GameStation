package com.example.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBController {

    // טעינת דרייבר MySQL פעם אחת כשמחלקה נטענת
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // אפשר גם לזרוק RuntimeException אם רוצים לעצור את האפליקציה
        }
    }

    // יצירת חיבור ל-DB
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/gamestation?serverTimezone=UTC&useSSL=false";
        String user = "root";
        String password = "SD!123sa";
        return DriverManager.getConnection(url, user, password);
    }

    // הכנסת משתמש לטבלה
    public static void insertUser(User user) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO users (username, email, password) VALUES (?, ?, ?)")) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();
        }
    }
}
