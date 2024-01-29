package com.app.frontend;

import com.app.main.Main;
import com.app.backend.Function;
import com.app.frontend.View.State;
import java.util.Scanner;

public abstract class InputHandler extends InputData {
    public static Function function = new Function();
    private static Scanner scan = new Scanner(System.in);
    private static String input;
    
    //track previous command
    private String prevCommand = "";
    
    //input data holders
    public static LoginInputData loginInputData = new LoginInputData(function);
    public static SuspendInputData suspendInputData = new SuspendInputData(function,true);
    public static SuspendInputData unSuspendInputData = new SuspendInputData(function,false);
    public static CreateInputData createInputData = new CreateInputData(function);
    public static DeleteInputData deleteInputData = new DeleteInputData(function);
    public static ToViewEmployeeInputData toViewEmployeeInputData = new ToViewEmployeeInputData(function);
    
    // for pagination
    public static int currResultRowSpan = 0;
    
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
        boolean toRender = true;
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
            case View.State.TOVIEWEMPLOYEE:
                toViewEmployeeInputData.addData(value);
                break;
        }
        
        if (toRender) {
            render();
        }
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
        if (isUserAdmin() && (Main.getUserID() != -1)) {
            
            // resetting page, when switching state.     
            if (!prevCommand.equals(c) && !(c.equals("m") || c.equals("M"))) {
                currResultRowSpan = 0;
                prevCommand = c;
            }
            
            switch(c) {
                //attendance setting
                case "a":
                    isInvalid = !setState(View.State.ATTENDANCE); // if already in the attendance setting, then the command is invalid.
                    showMore = true;
                    break;

                //suspend employee
                case "s":
                    isInvalid = !setState(View.State.SUSPEND); // same above.
                    showMore = true;
                    break;
                    
                //unsuspend employee
                case "u":
                    if (getCurrentState() == View.State.SUSPEND) {
                        isInvalid = !setState(View.State.UNSUSPEND);
                        showMore = true;
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
                    if (function.dataDisplaying) {
                        if (currResultRowSpan + 1 < Function.totalPage) {
                            currResultRowSpan++;
                        }
                        function.callLastDisplayMethod(currResultRowSpan);
                        isInvalid = false;
                        
                        showMore = true;
                    }
                    break;
                    
                // MORE, the opposite of 'm' go back to previous results
                case "M":
                    if (function.dataDisplaying) {
                        if (currResultRowSpan > 0) {
                            currResultRowSpan--;
                        }

                        function.callLastDisplayMethod(currResultRowSpan);
                        isInvalid = false;
                        
                        showMore = true;
                    }
                    break;
                    
                // create employee
                case "c":
                    setState(View.State.CREATE);
                    isInvalid = false;
                    break;
                    
                //display emploees basic info
                case "d":
                    isInvalid = !setState(View.State.TOVIEWEMPLOYEE);
                    showMore = true;
                    break;
                    
               //display employees payroll
                case "dp":
                    System.out.println("********************************************************************");
                    System.out.println("*\t\t\t\t\t\t\t\t   *");
                    System.out.println("*\t\t     Payroll for the month of December\t           *");
                    System.out.println("*\t\t\t\t\t\t\t\t   *");
                    System.out.println("********************************************************************");
                    System.out.println("Employee ID\t  Employee Name\t\tNet Salary\tGross Salary");
                    function.displayEmployeeSalary(0);
                    
                    showMore = true;
                    isInvalid = false;
                    break;
               
               //delete employee
                case "de":
                    setState(View.State.DELETE);
                    showMore = true;
                    isInvalid = false;
                    break;
            } 
        }
        
        // set to true if we want to continue displaying data's, that are limit.
        function.dataDisplaying = showMore;
        
        if (!showMore) {
            currResultRowSpan = 0;
        } 
            
        if (isInvalid) {
            System.out.println("Invalid command: '" + c + "'");
            render();
        }
    }
    
}
