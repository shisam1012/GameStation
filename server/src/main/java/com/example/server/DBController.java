package com.example.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBController {

    //Loading the driver one time
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Creating a new connection to the DB 
    //Using JDBC for the connection
    //******* !!!handle a connection pool ******* */
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/gamestation";
        String user = "root";
        String password = "SD!123sa";
        return DriverManager.getConnection(url, user, password);
    }

    /*The username and email of every player should be unique */
    public static boolean userExists(String username, String email) throws SQLException {
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(
             "SELECT * FROM users WHERE username = ? OR email = ?")) {

        stmt.setString(1, username);
        stmt.setString(2, email);
        ResultSet rs = stmt.executeQuery(); //
        return rs.next();
    }
}


    //This function inserts a new user into the users table
    public static void insertUser(User user) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt1 = conn.prepareStatement(
                        "INSERT INTO users (username, email, password) VALUES (?, ?, ?)");
                        PreparedStatement stmt2 = conn.prepareStatement(
                        "INSERT INTO user_info (username, wins_count, games_count, total_score) VALUES (?,0,0,0)")) {

            stmt1.setString(1, user.getUsername());
            stmt1.setString(2, user.getEmail());
            stmt1.setString(3, user.getPassword());
            stmt1.executeUpdate();

            stmt2.setString(1, user.getUsername());
            stmt2.executeUpdate();
        }


    }
    
    public static boolean checkUser(User user) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT * FROM users WHERE username = ? AND password = ?")) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            ResultSet rs = stmt.executeQuery();

            
            return rs.next();
        }
    }
} 
