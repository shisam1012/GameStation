package com.example.server.connect4.highscore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.server.DBUtils;

public class HighscoreDAO {
    
    /**
     * Retrieves the top 10 players by total score from the database.
     * Uses a connection from DBUtils' connection pool.
     * Always releases the connection back to the pool in the finally block.
     *
     * @return a list of UserInfo objects representing the top 10 players
     * @throws SQLException if a database access error occurs
     */
    public static List<UserInfo> getTop10Scores() throws SQLException {
        List<UserInfo> topScores = new ArrayList<>();
        String query = "SELECT username, games_count, wins_count, total_score " +
                "FROM user_info " +
                "ORDER BY total_score DESC " +
                "LIMIT 10";

        Connection conn = DBUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                topScores.add(new UserInfo(
                        rs.getString("username"),
                        rs.getInt("games_count"),
                        rs.getInt("wins_count"),
                        rs.getInt("total_score")));
            }
        }

        finally {
            DBUtils.releaseConnection(conn);
        }
        return topScores;
    }

    /**
     * Retrieves information for a specific user by username.
     * Returns a UserInfo object if found, or null if the user does not exist.
     * Uses a connection from DBUtils and ensures it is released after use.
     *
     * @param username the username of the player
     * @return UserInfo object for the player, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public static UserInfo getUserInfo(String username) throws SQLException {
        String query = "SELECT username, games_count, wins_count, total_score " +
                       "FROM user_info WHERE username = ?";
        Connection conn = DBUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new UserInfo(
                            rs.getString("username"),
                            rs.getInt("games_count"),
                            rs.getInt("wins_count"),
                            rs.getInt("total_score"));
                }
            }
        }
         finally {
            DBUtils.releaseConnection(conn); 
        }
        return null;
    }
}