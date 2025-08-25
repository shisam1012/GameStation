package com.example.server.connect4.game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.server.DBUtils;

/**
 * Data Access Object (DAO) for Connect4.
 * Provides methods to interact with the database (user_info table).
 * Currently, it updates player statistics (score, games played, wins).
 */
public class C4DAO {
    
    /**
     * Updates user statistics in the database:
     * - Increases total score
     * - Increases number of games played
     * - Increases number of wins (if applicable)
     *
     * @param username the player username
     * @param score    score to add (depends on difficulty and result)
     * @param win      1 if user won, 0 otherwise
     */
    public static void updateInfo(String username, int score, int win) throws SQLException {
        Connection conn = DBUtils.getConnection();
        try (//Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "Update user_info SET total_score=total_score+ ?, games_count=games_count + 1, wins_count =wins_count+? WHERE username =?")) {
            stmt.setInt(1, score);
            stmt.setInt(2, win);
            stmt.setString(3, username);
            stmt.executeUpdate();
        }
        finally {
            DBUtils.releaseConnection(conn); 
        }
    }
    

}
