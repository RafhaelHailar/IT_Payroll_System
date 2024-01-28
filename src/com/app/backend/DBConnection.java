package com.app.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public abstract class DBConnection {
    private final static String URL = "jdbc:mysql://localhost:3306/payroll_system_db";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "";
    private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    public Connection con;
    public Statement state;
    public ResultSet result;
    public PreparedStatement prep;
    
    public void connect() {
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
