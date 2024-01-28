package com.app.backend;

import java.util.ArrayList;

public class Function extends DBConnection {
     // for pagination
     private int rowLimit = 5; //the row limit. 
     private static int funcId = -1; //remembering the last display method called.
     private int positionIdEntered;
     public static boolean dataDisplaying = false;
     
     // read api's
     public void display() {
         String query = "Select * FROM users";
         
         try {
             connect();
             state = con.createStatement();
             result = state.executeQuery(query);
             
             while (result.next()) {
                 int id = result.getInt("user_id");
                 System.out.println(id);
             }
             
             con.close();
         } catch (Exception e) {
             System.out.println(e);
         }
     }
     
     public boolean login(int userId,String password) {
         String query = "SELECT * FROM users WHERE user_id = " + userId;
         boolean isLogged = false;
         
         try {
             connect();
             
             state = con.createStatement();
             result = state.executeQuery(query);
             result.next();
             
             String userPassword = result.getString("user_password");
             
             if (userPassword.equals(password)) {
                 isLogged = true;
             }
             
             con.close();
         } catch (Exception e) {
             System.out.println(e);
         }
         
         return isLogged;
     }
     
          
     public void displayEmployeeBasicInfo(int rowSpan){
       String query = "SELECT * FROM employees INNER JOIN positions "
              + "ON employees.position_id = positions.position_id LIMIT " + (rowLimit * rowSpan) + ", " + rowLimit;
       
       try {
           connect();
           state = con.createStatement();
           result = state.executeQuery(query);
           System.out.println("********************");
          
           while(result.next()){
               int id = result.getInt("employee_id");
               String name = result.getString("employee_name");
               String sex = result.getString("employee_sex");
               int age = result.getInt("employee_age");
               String position = result.getString("position_name");
               String hiredDate = result.getString("employee_hired_date");
               boolean isSuspended = result.getBoolean("employee_is_suspended");
               
               System.out.printf("[%d] | %-20s  | %s | %d | %s | %s | %s\n",id,name,sex,age,position,hiredDate, (isSuspended ? "SUSPENDED" : ""));
           }
           
           con.close();
       } catch (Exception e) {
           System.out.println(e);
       }
       
       // for pagination
       funcId = 0;
       dataDisplaying = true;
     }
     
     public void displaySuspendedEmployee(int rowSpan) {
         String query = "SELECT * FROM employees INNER JOIN positions "
                 + "ON employees.position_id = positions.position_id WHERE employee_is_suspended = 1 "
                 + "LIMIT " + (rowLimit * rowSpan) + ", " + rowLimit;
         
         try {
             connect();
             
             state = con.createStatement();
             result = state.executeQuery(query);
             
             while (result.next()) {
               int id = result.getInt("employee_id");
               String name = result.getString("employee_name");
               String sex = result.getString("employee_sex");
               int age = result.getInt("employee_age");
               String position = result.getString("position_name");
               String hiredDate = result.getString("employee_hired_date");
               boolean isSuspended = result.getBoolean("employee_is_suspended");
               System.out.printf("[%d] | %-20s  | %s | %d | %s | %s | %s\n",id,name,sex,age,position,hiredDate, (isSuspended ? "SUSPENDED" : ""));
               
             }
             
             con.close();
         } catch (Exception e) {
             System.out.println(e);
         }
         
         //for pagination
         funcId = 2;
         dataDisplaying = true;
     }
     
     public void displayEmployeeByPosition(int positionId,int rowSpan) {
         String query = "SELECT * FROM employees INNER JOIN positions "
                 + "ON employees.position_id = positions.position_id WHERE employees.position_id = " + positionId
                 + "LIMIT " + (rowLimit * rowSpan) + ", " + rowLimit;
         
         try {
             connect();
             
             state = con.createStatement();
             result = state.executeQuery(query);
             
             System.out.printf("ID | %-20s | SEX | AGE | POSITION | HIRED DATE | STATUS\n","NAME");
             
             while (result.next()) {
               int id = result.getInt("employee_id");
               String name = result.getString("employee_name");
               String sex = result.getString("employee_sex");
               int age = result.getInt("employee_age");
               String position = result.getString("position_name");
               String hiredDate = result.getString("employee_hired_date");
               boolean isSuspended = result.getBoolean("employee_is_suspended");
               System.out.printf("[%d] | %-20s  | %s | %d | %s | %s | %s\n",id,name,sex,age,position,hiredDate, (isSuspended ? "SUSPENDED" : ""));
               
             }
             
             con.close();
         } catch (Exception e) {
             System.out.println(e);
         }
          
         // for pagination
         positionIdEntered = positionId;
         funcId = 3;
         dataDisplaying = true;
     }
     
     public void displayPositionsName() {
         String query = "SELECT position_id,position_name FROM positions";
         String display = "";
         
         try {
             connect();
             
             state = con.createStatement();
             result = state.executeQuery(query);
             
             while (result.next()) {
                 String name = result.getString("position_name");
                 int id = result.getInt("position_id");
                 
                 System.out.printf("[%d]%s ", id, name);
             }
             
             con.close();
         } catch (Exception e) {
             System.out.println(e);
         }
         
         System.out.println(" "); // for spacing
     }
     
     public void expi() {
         String query = "SELECT * FROM employees LIMIT 5,5";
         
         try {
             connect();
             
             state = con.createStatement();
             result = state.executeQuery(query);
             //while (result.next()) {
                 result.absolute(5);
               //  String name = result.getString("employee_name");
                 int row = result.getRow();
                 System.out.println(row);
               //  System.out.printf("%s index:  %d", name, row);
           //  }
             
             con.close();
         } catch (Exception e) {
             System.out.println(e);
         }
     }
     
     public void callLastDisplayMethod(int currRowSpan) {
         switch (funcId) {
             case 0:
                 displayEmployeeBasicInfo(currRowSpan);
                 break;
             case 1:
                 break;
             case 2:
                 displaySuspendedEmployee(currRowSpan);
                 break;
             case 3:
                 displayEmployeeByPosition(positionIdEntered,currRowSpan);
                 break;
         }
         
        if (dataDisplaying) {
            System.out.println("\nType [m]More to show more results or [M]more to go back to previous results...");
        }
     }
     
     // update api's
     public boolean suspendEmployee(int employee_id) {
         String query = "UPDATE employees SET employee_is_suspended = 1 "
         + "WHERE employee_id = ?";
         
         try {
             connect();
             prep = con.prepareStatement(query);
             prep.setInt(1,employee_id);
             prep.executeUpdate();
             
             con.close();
             
             return true;
         } catch (Exception e) {
             System.out.println(e);
         }
         
         return false;
     }
     
     public boolean unSuspendEmployee(int employee_id) {
         String query = "UPDATE employees SET employee_is_suspended = 0 "
         + "WHERE employee_id = ?";
         
         try {
             connect();
             prep = con.prepareStatement(query);
             prep.setInt(1,employee_id);
             prep.executeUpdate();
             
             con.close();
             
             return true;
         } catch (Exception e) {
             System.out.println(e);
         }
         
         return false;
     }
}
