package com.app.main;

import com.app.backend.Function;
import com.app.frontend.View;

public class Main {
    // the one who decide whether an application stop.
    private static boolean isQuit = false;
    
    // admin credentials
    private static int adminId = 1;
    private static String adminPass = "admin";
    
    // who currently logged in
    private static int userID = -1;
    
    public static void main(String[] args) {
        // Starting displays
        System.out.println("**** IT PAYROLL SYSTEM ****");
        System.out.println("- a console based payroll system.");
        System.out.println("- to enter a value start the input with (\").");
        System.out.println("- entering any character that doesn't starts with (\") is considered as a command.");
        System.out.println("- enter [q] to quit the program.");
        System.out.println("- enter [l] to logout.");
        System.out.println("- enter [b] to go back to previous state / part of the program.");
        System.out.println("- enter [uu] to undo input.");
        
        View view = new View();     
       // Function function = new Function();
        
        // Application starts
        while (!isQuit) {
            view.checkInput();
        }
        
        System.out.println("\n* Application Quit!");
    }
    
    public static void quit() {
        isQuit = true;
    }
    
    public static void setUserID(int id) {
        userID = id;
    }
    
    public static int getUserID() {
        return userID;
    }
    
    public static int getAdminId() {
        return adminId;
    }
    
    public static String getAdminPass() {
        return adminPass;
    }
}
