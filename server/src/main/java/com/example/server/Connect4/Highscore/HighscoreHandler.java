package com.example.server.connect4.highscore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/scores")
public class HighscoreHandler {

    @GetMapping("/top10")
public ResponseEntity<List<UserInfo>> getTop10Scores() {
    try {
        List<UserInfo> topScores = HighscoreDAO.getTop10Scores();
        return ResponseEntity.ok(topScores); 
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("sqlexception");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

@GetMapping("/userinfo")
public ResponseEntity<UserInfo> getUserInfo(@RequestParam String username) {
    try {
        UserInfo userInfo = HighscoreDAO.getUserInfo(username);
        if (userInfo != null) {
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("sqlexception");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

}
