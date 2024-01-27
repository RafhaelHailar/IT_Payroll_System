package com.app.frontend;



public class View extends InputHandler{
    // PROGRAM STATES
    static enum State{
       LOGIN,
 
    }
    
    private static State currentState = State.LOGIN;
    
    public View() {
        // initial rendering
        render();
    }
    
    public static boolean setState(State state) {
        if (currentState == state) {
            return false;
        }
   
        currentState = state;
        
        render();
        
        return true;
    }
   
    public static void render() {
        if (currentState == State.LOGIN) {
            System.out.println("Welcome to Summoners Rift!");
            renderLogin();
        }
    }
    
    private static void renderLogin() {
        System.out.println("**LOGIN**");
        
        
    }
    
    private static void renderMain() {
        System.out.println("**MAIN**");
        
        //System.out.println(loginInputData.level);
    }
}
