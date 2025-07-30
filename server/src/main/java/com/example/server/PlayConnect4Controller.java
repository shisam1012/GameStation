package com.example.server;


//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.web.bind.annotation.*;
//import java.sql.SQLException;
import java.util.Map;

/**
 * REST API to initiate Connect4 game from the client.
 */

@CrossOrigin(origins = "*")//(origins = "http://localhost:3000") // Allow requests from React frontend
@RestController
//@RequestMapping("/api")
public class PlayConnect4Controller {


    //private final JmsTemplate jmsTemplate;

    /*public PlayConnect4Controller(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }*/

    @GetMapping("/play-connect-4")
    public ResponseEntity<?> connect4Player(@RequestParam String username, @RequestParam String difficulty) {
        
        //String playerInfo = username + ":" + difficulty;
        //jmsTemplate.convertAndSend("connect4easy.queue", playerInfo);
        System.out.println("Player " + username + " wants to play in " + difficulty + " mode");

            
        return ResponseEntity.ok(Map.of("message", "Ready to start a socket"));
             
        
    }

    


    
}
