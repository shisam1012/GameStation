package com.example.server.connect4.highscore;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/scores")

/**
 * REST controller that handles the HTTP requests from the client (highscore information request - top 10 and userinfo)
 * Provides endpoint for top10 and userinfo.
 * Interacts with HighscoreDAO to connect with the DB
 */

public class HighscoreController {

    /**
     * End point for top 10 requst 
     * @return ResponseEntity with a message on success (200 ok) or
     *         500 INTERNAL_SERVER_ERROR on database error
     */
    @GetMapping("/top10")
    public ResponseEntity<List<UserInfo>> getTop10Scores() {
        try {
            List<UserInfo> topScores = HighscoreDAO.getTop10Scores();
            return ResponseEntity.ok(topScores);
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("sqlexception");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * End point for userinfo requst 
     * @return ResponseEntity with a message on success (200 ok) or
     *         500 INTERNAL_SERVER_ERROR on database error
     */
    @GetMapping("/userinfo")
    public ResponseEntity<UserInfo> getUserInfo(@RequestParam String username){
        try {
            UserInfo userInfo = HighscoreDAO.getUserInfo(username);
            if (userInfo != null) {
                return ResponseEntity.ok(userInfo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println("sqlexception");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
