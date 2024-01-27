package com.app.main;

import com.app.backend.Function;
import com.app.frontend.View;

public class Main {
    // the one who decide whether an application stop.
    public static boolean isQuit = false;
    
    public static void main(String[] args) {
        View view = new View();     
        //Function function = new Function();
        
        // Starting displays
        System.out.println("IT Payroll System");
        System.out.println("To enter a value start with (\")");
        
        
        // Application starts
        while (!isQuit) {
            view.checkInput();
        }
        
        System.out.println("Application Quit!");
    }
    
    public static void quit() {
        isQuit = true;
    }
}
