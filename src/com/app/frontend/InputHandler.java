package com.app.frontend;

import com.app.main.Main;
import java.util.Scanner;

public abstract class InputHandler {
    private static Scanner scan = new Scanner(System.in);
    private static String input;
    
    private static LoginInputData loginInputData = new LoginInputData();
    
    private static void getInput() {
        input = scan.nextLine();
    }
    
    public static void checkInput() {
        boolean isCommand;     
        
        getInput();
        
        if (input.length() > 0) {
           isCommand = !input.startsWith("\"");  
            
           if (isCommand) {
               runCommand(input.charAt(0));
           } else {
               checkValue();
           }
        }
        
    }
    
    private static void checkValue() {
        String value = input.substring(1);
        System.out.println("The value you enter is: " + value);
    }
    
    private static void runCommand(char c) {
        boolean isInvalid = false;
        switch (c) {
            case 'q':
               Main.quit();
               break;  
            case 'a':
               if (!View.setState(View.State.LOGIN)) {
                   isInvalid = true;
               }
               break;
            default: 
                isInvalid = true;
        }
        
        if (isInvalid) {
            System.out.println("Invalid command: '" + c + "'");
        }
    }
}

// inputs data
class LoginInputData {
    private int userId = -1;
    private String userPassword = "";
    static int level = 1;
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    
    public void addData(String data) {
        switch (level) {
            case 1:
                try {
                    this.userId = Integer.parseInt(data);
                } catch (NumberFormatException e ) {
                    System.out.println("Invalid input: " + data + ", please input a valid number!");
                } catch (Exception e) {
                    System.out.println(e);
                }
        }
    }
    
    public void clear() {
        this.userId = -1;
        this.userPassword = "";
        level = 1;
    }
}