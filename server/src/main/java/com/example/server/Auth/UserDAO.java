package com.example.server.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.server.DBUtils;

public class UserDAO {

     /**
     * Checks if a user with the given username or email already exists
     * The username and email of every player should be unique
     * Uses a connection from the DBUtils connection pool.
     * Ensures the connection is released back to the pool in the finally block.
     *
     * @param username the username to check
     * @param email the email to check
     * @return true if a user exists with the username or email, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean userExists(String username, String email) throws SQLException {
        Connection conn = DBUtils.getConnection();
        try (//Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(
                        "SELECT * FROM users WHERE username = ? OR email = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery(); //
            return rs.next();
        }
        finally {
            DBUtils.releaseConnection(conn); 
        }
    
    }

    /**
     * Inserts a new user into the database.
     * Adds entries to both 'users' and 'user_info' tables
     * Connection is acquired from DBUtils and released in finally block.
     *
     * @param user the User object to insert
     * @throws SQLException if a database access error occurs
     */
    public static void insertUser(User user) throws SQLException {
        Connection conn = DBUtils.getConnection();

        try (//Connection conn = getConnection();
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
        } finally {
            DBUtils.releaseConnection(conn);
        }

    }
    
    /**
     * Checks if a user exists with the given username and password.
     * Useful for login verification.
     * Connection is acquired from DBUtils and released in finally block.
     *
     * @param user the User object containing username and password
     * @return true if the credentials match a record, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean checkUser(User user) throws SQLException {
        Connection conn = DBUtils.getConnection();
       
        try (PreparedStatement stmt = conn
                .prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
        
        finally {
            DBUtils.releaseConnection(conn); 
        }
    }
} 
