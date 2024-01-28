package com.app.frontend;

import com.app.main.Main;
import com.app.backend.Function;
import com.app.frontend.View.State;
import java.util.Scanner;

public abstract class InputHandler extends InputData {
    public static Function function = new Function();
    private static Scanner scan = new Scanner(System.in);
    private static String input;
    
    //input data holders
    public static LoginInputData loginInputData = new LoginInputData(function);
    public static SuspendInputData suspendInputData = new SuspendInputData(function,true);
    public static SuspendInputData unSuspendInputData = new SuspendInputData(function,false);
    public static CreateInputData createInputData = new CreateInputData(function);
    public static DeleteInputData deleteInputData = new DeleteInputData(function);
    
    // for pagination
    public int currResultRowSpan = 0;
    
    // abstract methods for view
    public abstract void render();
    public abstract boolean isState(State state);
    public abstract boolean returnPrevState();
    public abstract boolean isUserAdmin();
    public abstract boolean setState(State state);
    public abstract void displayLogout();
    public abstract View.State getCurrentState();
    
    private void getInput() {
        input = scan.nextLine();
    }
    
    public void checkInput() {
        boolean isCommand;     
        
        getInput();
        
        if (input.length() > 0) {
           isCommand = !input.startsWith("\"");  
            
           if (isCommand) {
               String[] c = input.split(" ");
               runCommand(c[0]);
           } else {
               checkValue();
           }
        } 
  
    }
    
    private void checkValue() {
        String value = input.substring(1);
        System.out.println("The value you enter is: " + value);
        
        switch (getCurrentState()) {
            case View.State.LOGIN:
                loginInputData.addData(value);
                break;
            case View.State.SUSPEND:
                suspendInputData.addData(value);
                break;
            case View.State.UNSUSPEND:
                unSuspendInputData.addData(value);
                break;
            case View.State.CREATE:
                createInputData.addData(value);
                break;
            case View.State.DELETE:
                deleteInputData.addData(value);
                break;
        }
        render();
    }
    
    private void runCommand(String c) {
        boolean isInvalid = true;
        boolean showMore = false; // for checking if paginating continues.
        
        switch (c) {
            //quitting
            case "q":
               Main.quit();
               break;  
             
            //logging out
            case "l":
                if (!isState(View.State.LOGIN)) {
                    System.out.println("Logging you out...");
                    Main.setUserID(-1);
                    displayLogout();
                    isInvalid = false;
                } 
                break;
            
            //backing
            case "b":
                // if returning to previous state failed then the command is invalid
                if (returnPrevState()) {
                    isInvalid = false;
                }
                break;
                
            // invalid command
            default: 
                isInvalid = true;
        }
        
        //admin only commands
        if (isUserAdmin()) {
            switch(c) {
                //attendance setting
                case "a":
                    isInvalid = !setState(View.State.ATTENDANCE); // if already in the attendance setting, then the command is invalid.
                    break;

                //suspend employee
                case "s":
                    isInvalid = !setState(View.State.SUSPEND); // same above.
                    break;
                    
                //unsuspend employee
                case "u":
                    if (getCurrentState() == View.State.SUSPEND) {
                        isInvalid = !setState(View.State.UNSUSPEND);
                    }
                    break;
                    
                //display by position
                case "p":
                    try {
                        int position_id = Integer.parseInt(String.valueOf(input.charAt(2)));
                        function.displayPositionsName();
                        function.displayEmployeeByPosition(position_id,currResultRowSpan);
                        isInvalid = false;
                    } catch (NumberFormatException e) {
                        System.out.println("Input a valid number");
                        isInvalid = true;
                    } catch (Exception e) {
                        isInvalid = true;
                        System.out.println(e);
                        System.out.println("Please enter a valid format: 'p [position id]'");
                    }
                    System.out.print("TYPE NUMBER: ");
                    break;
                
                // more, show more results from the data returned
                case "m": 
                    showMore = true;
                    currResultRowSpan++;
                    function.callLastDisplayMethod(currResultRowSpan);
                    isInvalid = false;
                    break;
                    
                // MORE, the opposite of 'm' go back to previous results
                case "M":
                    showMore = true;
                    
                    if (currResultRowSpan > 0) {
                        currResultRowSpan--;
                    }
                    
                    function.callLastDisplayMethod(currResultRowSpan);
                    isInvalid = false;
                    break;
                    
                // create employee
                case "c":
                    setState(View.State.CREATE);
                    isInvalid = false;
                    break;
                    
                //display emploees basic info
                case "d":
                    function.displayEmployeeBasicInfo(currResultRowSpan);      
                    
                    if (function.dataDisplaying) {
                        System.out.println("Type [m]More to show more results...");
                    }
                    
                    isInvalid = false;
                    break;
                    
               //display employees payroll
                case "dp":
                    function.displayEmployeePayroll();
                    
                    isInvalid = false;
                    break;
               
               //delete employee
                case "de":
                    setState(View.State.DELETE);
                    isInvalid = false;
                    break;
            } 
        }
        
        if (isInvalid) {
            System.out.println("Invalid command: '" + c + "'");
            render();
        }
        
        // set to true if we want to continue displaying data's, that are limit.
        function.dataDisplaying = showMore;
        
        if (!showMore) {
            currResultRowSpan = 0;
        } else {
            System.out.println("Type [m]More to show more results...");
        }
        
    }
    
}
