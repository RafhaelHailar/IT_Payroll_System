package com.app.backend;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Statement;


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
     
     //MY CHANGES BY: dperrr
     public void displayAllEmployeeInfo() {
 	    String query = "SELECT * FROM employees "
 	                 + "INNER JOIN positions ON employees.position_id = positions.position_id "
 	                 + "WHERE employee_is_suspended = 0";

 	    try {
 	        connect();
 	        state = con.createStatement();
 	        result = state.executeQuery(query);

 	        System.out.printf("%-15s%-25s%-10s%-5s%-20s\n",
 	                "EMPLOYEE ID", "Employee Name", "Gender", "Age", "Position Name");

 	        while (result.next()) {
 	            int employeeId = result.getInt("employee_id");
 	            String employeeName = result.getString("employee_name");
 	            String gender = result.getString("employee_sex");
 	            int employeeAge = result.getInt("employee_age");
 	            String positionName = result.getString("position_name");

 	            System.out.printf("[%d]\t\t%-25s%-10s%-5d%-20s\n",
 	                    employeeId, employeeName, gender, employeeAge, positionName);
 	        }
 	    } catch (Exception e) {
 	        System.out.println("Connection Error: " + e.getMessage());
 	    }
 	}
 	
 	public void displayEmployeePayroll() {
 	    String query = "SELECT attendances.*, positions.position_pay_per_day, employees.employee_name "
 	            + "FROM attendances "
 	            + "INNER JOIN employees ON attendances.employee_id = employees.employee_id "
 	            + "INNER JOIN positions ON positions.position_id = employees.position_id "
 	            + "ORDER BY employee_id";

 	    int netSalary = 0;
 	    float grossSalary = 0;
 	    int lateDeductions = 50;
 	    System.out.println("********************************************************************");
 	    System.out.println("*\t\t\t\t\t\t\t\t   *");
 	    System.out.println("*\t\t     Payroll for the month of December\t           *");
 	    System.out.println("*\t\t\t\t\t\t\t\t   *");
 	    System.out.println("********************************************************************");
 	    System.out.println("Employee ID\t  Employee Name\t\tNet Salary\tGross Salary");

 	    try {
 	        connect();
 	        state = con.createStatement();
 	        result = state.executeQuery(query);

 	        while (result.next()) {
 	            int noOfPresent = result.getInt("no_of_present");
 	            int noOfAbsent = result.getInt("no_of_absent");
 	            int noOfHoursLate = result.getInt("no_of_hours_late");
 	            int payPerDay = result.getInt("position_pay_per_day");
 	            int employeeId = result.getInt("employee_id");
 	            String employeeName = result.getString("employee_name");

 	            netSalary = payPerDay * noOfPresent;
 	            grossSalary = netSalary - (noOfAbsent * payPerDay + (noOfHoursLate * lateDeductions));

 	            System.out.printf("%-20d%-20s%-15d%-15.2f\n", employeeId, employeeName, netSalary, grossSalary);
 	           
 	        }
 	    } catch (Exception e) {
 	        e.printStackTrace();
 	    }
 	}
 	
 	//API FOR ADDING EMPLOYEE
 	public void addEmployee(String employeeName, String gender, int employeeAge, int positionId, String password) {
 	    String query = "INSERT INTO employees (employee_name, employee_sex, employee_age, position_id) VALUES (?, ?, ?, ?)";
 	    
 	    	
 	    try {
 	        connect();
 	        prep = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

 	        prep.setString(1, employeeName);
 	        prep.setString(2, gender);
 	        prep.setInt(3, employeeAge);
 	        prep.setInt(4, positionId);
 	        prep.executeUpdate();

 	        // Retrieve the auto increment employeeId
 	        ResultSet generatedKeys = prep.getGeneratedKeys();
 	        if (generatedKeys.next()) {
 	            int generatedEmployeeId = generatedKeys.getInt(1);
 	            addEmployeeCredentials(generatedEmployeeId, password);
 	            addToAttendances(generatedEmployeeId);
 	        }

 	        con.close();
 	        System.out.println("\n------------------------------");
 	        System.out.println("Successfully added an employee");
 	        System.out.println("------------------------------\n");
 	        displayAllEmployeeInfo();

 	    } catch (Exception e) {
 	        System.out.println("Connection Error" + e.getMessage());
 	    }
 	}

 	
 	
 	// METHOD FOR ADDING EMPLOYEE CREDENTIALS
 	public void addEmployeeCredentials(int employeeId, String password) {
 	    String query = "INSERT INTO users (user_password, employee_id) VALUES (?, ?)";

 	    try {
 	        connect();
 	        prep = con.prepareStatement(query);
 	        prep.setString(1, password);
 	        prep.setInt(2, employeeId);
 	        prep.executeUpdate();
 	        con.close();
 	    } catch (Exception e) {
 	        e.printStackTrace();
 	    }
 	}
 	
 	
 	public void addToAttendances(int employeeId) {
 		 String attendanceQuery = "INSERT INTO attendances (employee_id) VALUES (?)";
 		 
 		 try {
 			 connect();
 			 prep = con.prepareStatement(attendanceQuery);
 			 prep.setInt(1, employeeId);
 			 prep.executeUpdate();
 			 con.close();
 		 }catch(Exception e) {
 			 System.out.println(e);
 		 }
 	}
 	
 	
 	//METHOD FOR CHECKING IF THE EMPLOYEE EXISTS IN OUR DATABASE
 	private boolean isEmployeeExists(int employeeId) {
 	    String checkQuery = "SELECT COUNT(*) FROM employees WHERE employee_id = ?";
 	    try {
 	        connect();
 	        prep = con.prepareStatement(checkQuery);
 	        prep.setInt(1, employeeId);
 	        ResultSet result = prep.executeQuery();
 	        result.next();
 	        int count = result.getInt(1);
 	        con.close();
 	        return count > 0;
 	    } catch (Exception e) {
 	        e.printStackTrace();
 	        return false; 
 	    }
 	}
 	
 	//API FOR DELETING EMPLOYEE
 	public void deleteEmployee(int employeeId) {
 		

 		if (!isEmployeeExists(employeeId)) {
 	        System.out.println("Employee with ID " + employeeId + " does not exist.");
 	        return;
 	    }
 		String query = "DELETE FROM employees "
 				+"WHERE employee_id = ?" ;
 		
 		try {
 			
 			connect();
 			prep = con.prepareStatement(query);
 			prep.setInt(1, employeeId);
 			System.out.println("\nEmployee with  " + employeeId + " is sucessfully deleted...\n");
 			prep.executeUpdate();
 			con.close();
 			
 			displayAllEmployeeInfo();
 			
 			
 		}catch(Exception e) {
 			e.printStackTrace();
 		}
 		
 	}
}
