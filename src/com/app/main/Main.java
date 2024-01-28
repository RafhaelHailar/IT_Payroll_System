package com.app.main;

import com.app.backend.Function;
import com.app.frontend.View;

public class Main {
    // the one who decide whether an application stop.
    private static boolean isQuit = false;
    
    // who currently logged in
    private static int userID = 1;
    
    public static void main(String[] args) {
        // Starting displays
        System.out.println("IT Payroll System");
        System.out.println("*To enter a value start with (\")");
//        System.out.println("*To quit the application press 'q'");
        
        View view = new View();     
       // Function function = new Function();
        
        // Application starts
        while (!isQuit) {
            view.checkInput();
        }
        
        System.out.println("\nApplication Quit!");
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
}
