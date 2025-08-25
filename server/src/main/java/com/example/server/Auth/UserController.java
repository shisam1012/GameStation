package com.example.server.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@CrossOrigin(origins = "*")//(origins = "http://localhost:3000") // Allow requests from React frontend 
@RestController  //allows the class to handle REST API requests  //https://www.geeksforgeeks.org/advance-java/spring-rest-controller/
@RequestMapping("/api")

/**
 * REST controller that handles the HTTP requests from the client
 * Provides endpoints for user sign up and login. 
 * Interacts with UserDAO to connect with the DB
 */
public class UserController {

   
    /**
     * End point for signup requst 
     * @param user that includes username, email and password
     * @return ResponseEntity with a message on success or an error with the appropriate HTTP status:
     *         409 CONFLICT if username/email already exists,
     *         500 INTERNAL_SERVER_ERROR on database error
     */
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signupUser(@RequestBody User user) {
        try {
            //if the username or email already exists in the table - return an error, they must be unique
            if (UserDAO.userExists(user.getUsername(), user.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Username or email already exists."));
            }
            //if the username and email are unique insert into the users table the new user
            UserDAO.insertUser(user);
            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }
    /**
     * End point for login requst 
     * @param username
     * @param password
     * @return ResponseEntity with a message on success (200 ok) or an error with the appropriate HTTP status:
     *         401 UNAUTHORIZED if username/password are invalid,
     *         500 INTERNAL_SERVER_ERROR on database error
     */
    @GetMapping("/login")
    public ResponseEntity<?> registerUser(@RequestParam String username, @RequestParam String password) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);

            //boolean userExists = UserDAO.checkUser(user);
            //System.out.println("User exists? " + userExists);

            //check if the user exists in the table, if not return an error message
            if (UserDAO.checkUser(user)) {
                return ResponseEntity.ok(Map.of("message", "User registered successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid username or password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
            }
    }

    
}
