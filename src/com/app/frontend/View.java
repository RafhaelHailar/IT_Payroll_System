package com.app.frontend;

import java.util.ArrayList;
import com.app.main.*;

public class View extends InputHandler{
    // PROGRAM STATES
    enum State{
       LOGIN,
       MAIN,
       ATTENDANCE,
       SUSPEND,
       UNSUSPEND,
       CREATE,
       DELETE,
       TOVIEWEMPLOYEE,
       VIEWEMPLOYEE
    }
    
    enum UserType {
        ADMIN,
        EMPLOYEE
    }
    
    private State currentState = State.MAIN;
    private UserType loggedAs = UserType.ADMIN; 
    private ArrayList<State> routeHistory = new ArrayList<State>();
    
    //employee viewing
    private static int employeeViewingId = -1;
    
    public View() {
        routeHistory.add(currentState);
        
        // initial rendering
        render();
    }
    
    public boolean setState(State state) {
        if (currentState == state) {
            return false;
        }
        
        // resetting the current level of the input
        resetInputData();
        
        // to not allow user to go back to login, when they are already logged in.
        if (currentState == State.LOGIN && state == State.MAIN) {
            routeHistory.remove(0); 
        }
        
        currentState = state; // update the current state
        
        routeHistory.add(currentState); // add the current route
        
        // remove the in between states of the similars state in the history.
        int startStateIndex = routeHistory.indexOf(currentState);
        ArrayList<State> newHistory = new ArrayList(routeHistory.subList(0,startStateIndex + 1));
        
        routeHistory = newHistory;
       
        render();
        return true;
    }
   
    public void render() {
        System.out.println(routeHistory + " current state is: " + currentState);
        switch (currentState) {
            case State.LOGIN:
                renderLogin();
                break;
            case State.MAIN:
                renderMain();
                break;
            case State.ATTENDANCE:
                renderAttendance();
                break;
            case State.SUSPEND:
                renderSuspend();
                break;
            case State.UNSUSPEND:
                renderUnsuspend();
                break;
            case State.CREATE:
                renderCreate();
                break;
            case State.DELETE:
                renderDelete();
                break;
            case State.TOVIEWEMPLOYEE:
                renderToViewEmployee();
                break;
            case State.VIEWEMPLOYEE:
                renderViewEmployee();
                break;
        }
        
    }
    
    private void renderLogin() {
        int userID = Main.getUserID();
        
        switch (loginInputData.level) {
            case 1: 
                System.out.println("**LOGIN**");
                System.out.print("Enter your user id: ");
                break;
            case 2:
                System.out.print("Enter your user password: ");
                break;
            case 3:
                loginInputData.clear();
                
                if (userID > 0) {
                    System.out.println("Welcome " + userID);
                    
                    if (userID == Main.getAdminId()) {
                        setUserType(UserType.ADMIN);
                    } else {
                        setUserType(UserType.EMPLOYEE);
                    }
                    
                    System.out.println("Logged in!");
                    
                    setState(State.MAIN);
                }  else {
                    System.out.println("Username or Password is incorrect!");
                    render();
                }
                break;
        }
    }
   
    private void renderMain() {
        int userID = Main.getUserID();
        System.out.println("**MAIN**");
        System.out.println("USER: " + (userID == 1 ? "admin" : userID));
        System.out.println("* Type (l) to logout!");
        if (isUserAdmin()) {
            System.out.println("ENTER YOUR CHOICE: ");
            System.out.println("[d] DISPLAY EMPLOYEES BASIC INFO");
            System.out.println("[a] SET EMPLOYEES ATTENDANCE");
            System.out.println("[p <position id>] DISPLAY EMPLOYEES BASIC INFO FILTERED BY GIVEN POSITION ID");
            System.out.println("[s] SUSPEND EMPLOYEE");
            System.out.println("[de] DELETE EMPLOYEE");
            System.out.println("[c] CREATE EMPLOYEE");
            System.out.println("[dp] DISPLAY EMPLOYEES PAYROLL");
            System.out.print("TYPE NUMBER : ");
        } else {
            System.out.println("EMPLOYEE DETAILS");
            
        }
    }
    
    private void renderAttendance() {
        System.out.println("**SET EMPLOYEE ATTENDANCE**");
        function.displayEmployeeBasicInfo(currResultRowSpan);
       
        System.out.print("Enter employee id: ");
    }
    
    
    public void renderSuspend() {
        
        switch (suspendInputData.level) {
            case 1:
                System.out.println("**SUSPEND EMPLOYEE**");
                System.out.println("Type [u]Unsuspend, to unsuspend suspended employees");
                function.displayAllUnsuspendEmployeeInfo(currResultRowSpan);
                displayingMoreText();
                System.out.print("Enter employee id: ");
                break;
            case 2:
                System.out.print("Confirm Suspend [(Y) YES, input other than (Y) is considered false]: ");
                break;
            case 3:
                System.out.println("Suspension Success!");
                suspendInputData.clear();
                render();
                break;
        }
    }
    
    public void renderUnsuspend() {
        
        switch (unSuspendInputData.level) {
            case 1:
                System.out.println("**UNSUSPEND EMPLOYEE**");
                function.displaySuspendedEmployee(currResultRowSpan);
                displayingMoreText();
                System.out.print("Enter suspended employee id: ");
                break;
            case 2:
                System.out.print("Confirm Unsuspend [(Y) YES, input other than (Y) is considered false]: ");
                break;
            case 3:
                System.out.println("Unsuspension Success!");
                unSuspendInputData.clear();
                render();
                break;
        }
    }
    
    public void renderCreate() {
        
        switch (createInputData.level) {
            case 1:
                System.out.println("**CREATE EMPLOYEE**");
                System.out.print("Enter employee name: ");
                break;
            case 2:
                System.out.print("Enter employee password: ");
                break;
            case 3:
                System.out.print("Enter employee sex ('M' | 'F'): ");
                break;
            case 4:
                System.out.print("Enter employee age: ");
                break;
            case 5:
                System.out.println("Positions id and name: ");
                createInputData.positionIdLimit = function.displayPositionsName();
                System.out.print("Enter employee position id: ");
                break;        
        }
    }
    
    public void renderDelete() {
        
        switch (deleteInputData.level) {
            case 1:
                System.out.println("**DELETE EMPLOYEE**");
                function.displayEmployeeBasicInfo(currResultRowSpan);
                displayingMoreText();
                System.out.print("Enter employee id: ");
                break;
            case 2:
                System.out.print("Confirm Deletion [(Y) YES, input other than (Y) is considered false]: ");
                break;
            case 3:
                deleteInputData.clear();
                break;
        }
    }
    
    public void renderToViewEmployee() {
        switch (toViewEmployeeInputData.level) {
            case 1:
                System.out.println("------EMPLOYEES ");
                function.displayEmployeeBasicInfo(currResultRowSpan);
                System.out.print("Enter employee id: ");
                break;
            case 2:
                toViewEmployeeInputData.clear();
                if (employeeViewingId > -1) { 
                    setState(State.VIEWEMPLOYEE);
                }
                break;
        }
    }
    
    public void renderViewEmployee() {
        System.out.println("Viewing Employee");
        System.out.println("Viewing " + employeeViewingId + " data");
        Employee employeeData = function.getEmployeeData(employeeViewingId);
        
        if (!employeeData.getIsSuccess()) {
           System.out.println("Data can not retrieve!");
           View.setEmployeeViewing(-1);
           setState(State.TOVIEWEMPLOYEE);
           return;
        }
        
        String name,sex,hiredDate,positionName;
        int age,positionId,salaryPerDay;
        boolean isSuspended;
        
        name = employeeData.getName();
        sex = employeeData.getSex();
        hiredDate = employeeData.getHiredDate();
        positionName = employeeData.getPositionName();
        age = employeeData.getAge();
        positionId = employeeData.getPositionId();
        isSuspended = employeeData.isIsSuspended();
        salaryPerDay = employeeData.getSalaryPerDay();
        
        int spacing = 50;
        int phpSpacing = 15;
        
        ArrayList<Integer> lengths = new ArrayList<Integer>();
        int maxLength = 0;
        
        lengths.add((":EMPLOYEE " + employeeViewingId).length() + spacing); 
        lengths.add((name).length() + spacing);
        lengths.add((positionName).length() + spacing);
        lengths.add(("P"+ salaryPerDay +"/day").length() + spacing);
        lengths.add(("May 1-30 2023").length() + spacing);
        lengths.add(("20 day").length() + spacing);
        lengths.add(("P"+ 2300500 +"/day").length() + spacing);
        lengths.add(("P"+ 2300500 +"/day").length() + spacing);
        lengths.add(("P"+ 2300500 +"/day").length() + spacing);
        lengths.add(("P"+ 2300500 +"/day").length() + spacing);
        lengths.add(("P"+ 2300500 +"/day").length() + spacing);
        
        for (Integer length: lengths) {
            if (length > maxLength) {
                maxLength = length;
            }
        }
        
        System.out.printf("%-"+ spacing +"s%s\n"," ",":EMPLOYEE " + employeeViewingId);
        System.out.println(" ");
        System.out.printf("%-"+ spacing +"s%s\n","Employee Name:",name);
        System.out.printf("%-"+ spacing +"s%s\n", "Company Position:",positionName);
        System.out.printf("%-"+ (spacing - phpSpacing) +"s%-"+ phpSpacing +"s%s\n","Salary Rate: ","Php:","P" + formatNumber(salaryPerDay) + "/day");
        drawCharacterNTimes('*',maxLength);
        System.out.printf("%-"+ spacing +"s%s\n", "Date Covered:","May 1-30 2023");
        System.out.printf("%-"+ spacing +"s%s\n", "Total Number of Days Present:", "20 day");
        System.out.printf("%-"+ (spacing - phpSpacing) +"s%-"+ phpSpacing +"s%s\n","Gross Income: ","Php:","P" + formatNumber(2300500));
        System.out.println("Deductions");
        System.out.printf("%-"+ (spacing - phpSpacing) +"s%-"+ phpSpacing +"s%s\n","    Tax: ","Php:","P" + formatNumber(2300500));
        System.out.printf("%-"+ (spacing - phpSpacing) +"s%-"+ phpSpacing +"s%s\n","    SSS: ","Php:","P" + formatNumber(2300500));
        System.out.printf("%-"+ (spacing - phpSpacing) +"s%-"+ phpSpacing +"s%s\n","    Medicare: ","Php:","P" + formatNumber(2300500));
        drawCharacterNTimes('*',maxLength);
        System.out.printf("%-"+ (spacing - phpSpacing) +"s%-"+ phpSpacing +"s%s\n","Net Income: ","Php:","P" + formatNumber(2300500));
        /*
                                          :EMPLOYEE 22313
  
  Employee Name:                          Marco Pol. Leo
  Salary Rate:                    Php:    P1,500/day
  *******************************************************
  Date Covered:                           May 1-30 2023
  Total Number of Days Present:           20 day         
  Gross Income:                   Php:    P2,300,500
  Deductions:            
      Tax:                        Php:    P5,000
      SSS:                        Php:    P6,300
      Medicare:                   Php:    P300
  *******************************************************
  Net Income:                     Php:    P3,000,000 */
    }
    
    public void displayLogout() {
        clearHistory();
        System.out.println("Logged out!");
        setState(View.State.LOGIN);
    }
    
    public boolean isState(State state) {
        return currentState == state;
    }
    
    public State getCurrentState() {
        return this.currentState;
    }
    
    public boolean isUserAdmin() {
        return loggedAs == UserType.ADMIN;
    }
    
    private void setUserType(UserType type) {
        loggedAs = type;
    }
    
    private void resetInputData() {
        switch (currentState) {
            case State.SUSPEND:
                suspendInputData.clear();
                break;
            case State.UNSUSPEND:
                unSuspendInputData.clear();
                break;
            case State.CREATE:
                createInputData.clear();
                break;
            case State.DELETE:
                deleteInputData.clear();
                break;
        }         
    }
    
    private void displayingMoreText() {
        if (function.dataDisplaying) {
            System.out.println("\nType [m]More to show more results...");
        }
    }
    
    // return to state before the current state, return true if returng to previous state is success.
    public boolean returnPrevState() {
        boolean result = false;
        int prevStateIndex = routeHistory.size() - 2;
        
        // to check whether there's a target element state.
        if (prevStateIndex > -1) {
            State targetState = routeHistory.get(prevStateIndex); // go back to prev state before current state
            
            routeHistory.remove(routeHistory.size() - 1);
            setState(targetState);
            
            result = true;
        }
        
        return result;
    }
    
    //draw character n times
    private void drawCharacterNTimes(char c,int n) {
        for (int i = 0;i < n;i++) {
            System.out.print(c);
        }
        System.out.print("\n");
    }
    
    //number formatter
    private String formatNumber(int number) {
        String result = "";
        
        String strNumber = String.valueOf(number);
        
        for (int i = strNumber.length() - 1;i >= 0;i--) {
            boolean addComma = false;
            if ((strNumber.length() - i) % 3 == 0 && i > 0) {
                try {
                    // since ascii is 1-255, if the charAt returns below 0 then the character does not found.
                    if (strNumber.charAt(i - 1) > 0) {
                        addComma = true;
                    }
                } catch(Exception e) {}
            }
            
            result = strNumber.charAt(i) + result;
            if (addComma) {
                result = "," + result;
            }
        }
        
        return result;
    }
    
    //set the employee we are going to view
    public static void setEmployeeViewing(int employeeId) {
        employeeViewingId = employeeId;
    }
    
    //clear routes history
    public void clearHistory() {
        routeHistory.clear();
    }
}
