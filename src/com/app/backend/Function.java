package com.app.backend;

public class Function extends DBConnection {

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
}
