package com.example.server.Connect4.Game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;

public class C4DBController {
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
        //String url = "jdbc:mysql://localhost:3306/gamestation";
        String url = "jdbc:mysql://localhost:3306/gamestation?useSSL=false&allowPublicKeyRetrieval=true";

        String user = "root";
        String password = "SD!123sa";
        return DriverManager.getConnection(url, user, password);
    }

    public static void updateInfo(String username, int score, int win) throws SQLException {
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "Update user_info SET total_score=total_score+ ?, games_count=games_count + 1, wins_count =wins_count+? WHERE username =?")) {
            stmt.setInt(1, score);
            stmt.setInt(2, win);
            stmt.setString(3, username);
            stmt.executeUpdate();
        }

    }
    
    

}
