package com.app.frontend;

import java.util.ArrayList;
import com.app.main.*;
import java.time.LocalDate;

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
       VIEWEMPLOYEE,
       TOVIEWSALARIES,
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
    
    // for displaying employee payroll based on year and month
    public static LocalDate currDate = LocalDate.now();
    private static int targetYear = currDate.getYear();
    private static String targetMonth = availableMonths[currDate.getMonthValue() - 1];
    
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
            case LOGIN:
                renderLogin();
                break;
            case MAIN:
                renderMain();
                break;
            case ATTENDANCE:
                renderAttendance();
                break;
            case SUSPEND:
                renderSuspend();
                break;
            case UNSUSPEND:
                renderUnsuspend();
                break;
            case CREATE:
                renderCreate();
                break;
            case DELETE:
                renderDelete();
                break;
            case TOVIEWEMPLOYEE:
                renderToViewEmployee();
                break;
            case VIEWEMPLOYEE:
                renderViewEmployee();
                break;
            case TOVIEWSALARIES:
                renderToViewSalaries();
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
        switch (attendanceInputData.level) {
            case 1:
                System.out.println("**SET EMPLOYEE ATTENDANCE**");
                function.displayEmployeeBasicInfo(currResultRowSpan);
               
                System.out.print("Enter employee id: ");
                break;
            case 2:
                System.out.print("Enter year: ");
                break;
            case 3:
                System.out.print("Enter month id ([1] January - [12] December): ");
                break;
            case 4:
                System.out.print("Enter total days present: ");
                break;
            case 5:
                System.out.print("Enter total days absent: ");
                break;
            case 6:
                System.out.print("Enter total days late: ");
                break;
            case 7:
                System.out.print("Enter total hours late: ");
                break;
            case 8:
                attendanceInputData.clear();
                render();
                break;
        }
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
                targetYear = currDate.getYear();
                targetMonth = availableMonths[currDate.getMonthValue() - 1];
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
        System.out.println("**Viewing Employee**");
        System.out.println("[cd] change date, change the payslip viewing with a given date");
        
        if (changeDateInputData.getActive()) {
            switch (changeDateInputData.level) {
                case 1:
                    System.out.println("**Change Date");
                    System.out.print("Enter year: ");
                    break;
                case 2:
                    System.out.print("Enter month: ");
                    break;
            }
        } else {
            System.out.println("Viewing " + employeeViewingId + " data");
            Employee employeeData = function.getEmployeeData(employeeViewingId,targetYear,targetMonth);

            if (!employeeData.getIsSuccess()) {
               System.out.println("Data can not retrieve!");
               View.setEmployeeViewing(-1);
               setState(State.TOVIEWEMPLOYEE);
               return;
            }

            String name,sex,hiredDate,positionName;
            int age,positionId,salaryPerDay,daysPresent,
                taxDeduction,sssDeduction,medicareDeduction,
                grossSalary,hoursLate,lateDeduction;
            float netSalary;
            boolean isSuspended;

            name = employeeData.getName();
            sex = employeeData.getSex();
            hiredDate = employeeData.getHiredDate();
            positionName = employeeData.getPositionName();
            age = employeeData.getAge();
            positionId = employeeData.getPositionId();
            isSuspended = employeeData.isIsSuspended();
            salaryPerDay = employeeData.getSalaryPerDay();
            daysPresent = employeeData.getDaysPresent();
            taxDeduction = employeeData.getTaxDeduction();
            sssDeduction = employeeData.getSSSDeduction();
            medicareDeduction = employeeData.getMedicareDeduction();
            grossSalary = employeeData.getGrossSalary();
            netSalary = employeeData.getNetSalary();
            hoursLate = employeeData.getHoursLate();
            lateDeduction = employeeData.getLateDeduction();

            int spacing = 50;
            int phpSpacing = 15;

            ArrayList<Integer> lengths = new ArrayList<Integer>();
            int maxLength = 0;

            lengths.add((":EMPLOYEE " + employeeViewingId).length() + spacing); 
            lengths.add((name).length() + spacing);
            lengths.add((positionName).length() + spacing);
            lengths.add(("P"+ salaryPerDay +"/day").length() + spacing);
            lengths.add((targetMonth + " 1-30 " + targetYear).length() + spacing);
            lengths.add((daysPresent + " day").length() + spacing);
            lengths.add(("P"+ formatNumber(grossSalary)).length() + spacing);
            lengths.add(("P" + formatNumber(taxDeduction)).length() + spacing);
            lengths.add(("P" + formatNumber(sssDeduction)).length() + spacing);
            lengths.add(("P" + formatNumber(medicareDeduction)).length() + spacing);
            lengths.add(("P" + formatNumber(netSalary)).length() + spacing);

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
            System.out.printf("%-"+ spacing +"s%s\n", "Date Covered:",targetMonth + " 1-30 " + targetYear);
            System.out.printf("%-"+ spacing +"s%s\n", "Total Number of Days Present:",daysPresent + " day");
            System.out.printf("%-"+ (spacing - phpSpacing) +"s%-"+ phpSpacing +"s%s\n","Gross Income: ","Php:","P" + formatNumber(grossSalary));
            System.out.println("Deductions");
            System.out.printf("%-"+ (spacing - phpSpacing) +"s%-"+ phpSpacing +"s%s\n","    Tax: ","Php:","P" + formatNumber(taxDeduction));
            System.out.printf("%-"+ (spacing - phpSpacing) +"s%-"+ phpSpacing +"s%s\n","    SSS: ","Php:","P" + formatNumber(sssDeduction));
            System.out.printf("%-"+ (spacing - phpSpacing) +"s%-"+ phpSpacing +"s%s\n","    Medicare: ","Php:","P" + formatNumber(medicareDeduction));
            System.out.printf("%-"+ (spacing - phpSpacing) +"s%-"+ phpSpacing +"s%s\n","    Late: ","Php:","P" + formatNumber(lateDeduction * hoursLate) + "(P" + lateDeduction + " x " + hoursLate + "hour/s)");
            drawCharacterNTimes('*',maxLength);
            System.out.printf("%-"+ (spacing - phpSpacing) +"s%-"+ phpSpacing +"s%s\n","Net Income: ","Php:","P" + formatNumber(netSalary));
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
    }
    
        
    public void renderToViewSalaries() {
        System.out.println("CURRENT LEVEL: " + salaryInputData.level);
        switch (salaryInputData.level) {
            case 1:
                System.out.println("**DISPLAY EMPLOYEES SALARY**");
                System.out.print("Enter year: ");
                break;
            case 2:
                System.out.print("Enter month: ");
                break;
            case 3:
                salaryInputData.reDisplaySalaries();
                System.out.print("Enter employee id: ");
                break;
            case 4:
                salaryInputData.clear();
                setState(State.VIEWEMPLOYEE);
                break;
        }
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
            case SUSPEND:
                suspendInputData.clear();
                break;
            case UNSUSPEND:
                unSuspendInputData.clear();
                break;
            case CREATE:
                createInputData.clear();
                break;
            case DELETE:
                deleteInputData.clear();
                break;
            case ATTENDANCE:
                attendanceInputData.clear();
                break;
            case TOVIEWSALARIES:
                salaryInputData.clear();
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
    private String formatNumber(float number) {
        String result = "";
        
        String[] strNumber = String.valueOf(number).split("\\.");
        String strWhole = strNumber[0];
        String strDecimals = "";
        
        if (strNumber.length > 1){
            strDecimals = strNumber[1];
        }
        
        for (int i = strWhole.length() - 1;i >= 0;i--) {
            boolean addComma = false;
            if ((strWhole.length() - i) % 3 == 0 && i > 0) {
                try {
                    // since ascii is 1-255, if the charAt returns below 0 then the character does not found.
                    if (strWhole.charAt(i - 1) > 0) {
                        addComma = true;
                    }
                } catch(Exception e) {}
            }
            
            result = strWhole.charAt(i) + result;
            if (addComma) {
                result = "," + result;
            }
        }
        result += "." + strDecimals;
        return result;
    }
    
    //set the employee we are going to view
    public static void setEmployeeViewing(int employeeId) {
        employeeViewingId = employeeId;
    }
    
    //set the date of the payroll we are going to look from the employee
    public static void setDateViewing(int year,String month) {
        targetYear = year;
        targetMonth = month;
    }
    
    //clear routes history
    public void clearHistory() {
        routeHistory.clear();
    }
}
