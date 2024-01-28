package com.app.frontend;

import java.util.ArrayList;
import com.app.main.Main;


public class View extends InputHandler{
    // PROGRAM STATES
    enum State{
       LOGIN,
       MAIN,
       ATTENDANCE,
       SUSPEND,
       UNSUSPEND
    }
    
    enum UserType {
        ADMIN,
        EMPLOYEE
    }
    
    private State currentState = State.MAIN;
    private UserType loggedAs = UserType.ADMIN; 
    private ArrayList<State> routeHistory = new ArrayList<State>();
    
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
                    
                    if (userID == 1) {
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
            System.out.println("[5] SEE ALL EMPLOYEE");   
            System.out.print("TYPE NUMBER : ");
        } else {
            System.out.println("EMPLOYEE DETAILS");
        }
    }
    
    private void renderAttendance() {
        System.out.println("**SET EMPLOYEE ATTENDANCE**");
        function.displayEmployeeBasicInfo(currResultRowSpan);
        displayingMoreText();
        System.out.print("Enter employee id: ");
    }
    
    
    public void renderSuspend() {
        
        switch (suspendInputData.level) {
            case 1:
                System.out.println("**SUSPEND EMPLOYEE**");
                function.displayEmployeeBasicInfo(currResultRowSpan);
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
    
    //clear routes history
    public void clearHistory() {
        routeHistory.clear();
    }
}
