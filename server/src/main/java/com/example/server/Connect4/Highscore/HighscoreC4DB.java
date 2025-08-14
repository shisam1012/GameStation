package com.example.server.connect4.highscore;

 import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class HighscoreC4DB {
 




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

    
    
public static List<UserInfo> getTop10Scores() throws SQLException {
        List<UserInfo> topScores = new ArrayList<>();
        String query = "SELECT username, games_count, wins_count, total_score " +
                       "FROM user_info " +
                       "ORDER BY total_score DESC " +
                       "LIMIT 10";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                topScores.add(new UserInfo(
                    rs.getString("username"),
                    rs.getInt("games_count"),
                    rs.getInt("wins_count"),
                    rs.getInt("total_score")
                ));
            }
        }
        return topScores;
    }

    public static UserInfo getUserInfo(String username) throws SQLException {
        String query = "SELECT username, games_count, wins_count, total_score " +
                       "FROM user_info WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserInfo(
                        rs.getString("username"),
                        rs.getInt("games_count"),
                        rs.getInt("wins_count"),
                        rs.getInt("total_score")
                    );
                }
            }
        }
        return null;
    }
}