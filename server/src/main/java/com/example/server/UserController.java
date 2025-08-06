package com.example.server;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;
@CrossOrigin(origins = "*")//(origins = "http://localhost:3000") // Allow requests from React frontend
@RestController
@RequestMapping("/api")
public class UserController {

   /*  @PostMapping("/signup")
   public ResponseEntity<?> signupUser(@RequestBody User user) {
      try {
          DBController.insertUser(user);
          return ResponseEntity.ok(Map.of("message", "User signed up successfully"));
   
      } catch (SQLException e) {
          e.printStackTrace();
          return ResponseEntity
                  .status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(Map.of("error", e.getMessage()));
      }
   }*/
    
   /*  @PostMapping("/signup")
   public ResponseEntity<String> signupUser(@RequestBody User user) {
       try {
           //if there is already a user with the same username or mail return an error
           if (DBController.userExists(user.getUsername(), user.getEmail())) {
               return ResponseEntity.status(HttpStatus.CONFLICT)
                       .body("Username or email already exists.");
           }
           //otherwise insert the user to the DB
           DBController.insertUser(user);
           return ResponseEntity.ok("User registered successfully!");
       } catch (SQLException e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Server error");
       }
   }*/

@PostMapping("/signup")
public ResponseEntity<Map<String, String>> signupUser(@RequestBody User user) {
    try{if (DBController.userExists(user.getUsername(), user.getEmail())) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(Map.of("error", "Username or email already exists."));
    }
    
    
        DBController.insertUser(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    } catch (SQLException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(Map.of("error", "Database error: " + e.getMessage()));
    }
}

    
    
    @GetMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String username, @RequestParam String password) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            boolean userExists = DBController.checkUser(user);
            System.out.println("User exists? " + userExists);

            if (userExists) {
                return ResponseEntity.ok(Map.of("message", "User registered successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid username or password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
            }
    }

    
    
    /* 
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            boolean userExists = DBController.checkUser(user);
             System.out.println("User exists? " + userExists);
            if (userExists) {
                return ResponseEntity.ok(Map.of("message", "User registered successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid username or password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
        }
    }*/

    
}
