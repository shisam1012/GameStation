package com.example.server;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Queue;

@Component
public class DBUtils {

    // Maximum number of connections in the pool
    private static final int POOL_SIZE = 10;// 1; //for checking :)
    // Thread-safe queue to hold available DB connections
    private static final Queue<Connection> connectionPool = new ArrayDeque<>();

    //Loading the driver one time
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < POOL_SIZE; i++) { // Initialize the connection pool
                connectionPool.offer(createNewConnection());
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to initialize DB connection pool", e);
        }
    }

    //Creating a new connection to the DB 
    //Using JDBC for the connection
    private static Connection createNewConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/gamestation?useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "SD!123sa";
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Retrieves a connection from the pool.
     * If no connections are available, the calling thread waits until one is released.
     * Synchronized to ensure thread safety.
     */
    public static synchronized Connection getConnection() {
        System.out.println("getting connection");
        while (connectionPool.isEmpty()) {
            try {
                DBUtils.class.wait();
                System.out.println("waiting for a connection");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for a DB connection", e);
            }
        }
        return connectionPool.poll();
    }

    /**
     * Releases a connection back to the pool.
     * Notifies any waiting threads that a connection is available.
     */
    public static synchronized void releaseConnection(Connection conn) {
        if (conn != null) {
            connectionPool.offer(conn);
            System.out.println("released connection");
            DBUtils.class.notifyAll();
        }
    }
    
    /**
     * Closes all connections in the pool.
     * Should be called when shutting down the application to release resources.
     */
    public static synchronized void closeAllConnections() throws SQLException { 
        for (Connection conn : connectionPool) {
            if (!conn.isClosed()) {
                conn.close();
            }
        }
        connectionPool.clear();
    }
}
