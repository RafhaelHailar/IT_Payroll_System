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
     
 	 private final int SSS = 1000;
     private final int MEDICARE = 500;

     private float netSalary = 0;
     private int grossSalary = 0;
     private int noOfHoursLate;
     
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
     
     /**
 	 * Retrieves and displays information about all active employees.
 	 * It also filters the employees that are not suspended
 	 * Displays employee ID, name, gender, age, and their respective position.
 	 * 
 	 */ 
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
     
     /**
 	 * Displays the salary information for employees for the specified month.
 	 */

     public void displayEmployeeSalary() {
         String query = "SELECT attendances.*, positions.position_pay_per_day, employees.employee_name "
                 + "FROM attendances "
                 + "INNER JOIN employees ON attendances.employee_id = employees.employee_id "
                 + "INNER JOIN positions ON positions.position_id = employees.position_id "
                 + "ORDER BY employee_id";

         System.out.println("********************************************************************");
         System.out.println("*\tEmployee Salary for the month of December\t           *");
         System.out.println("********************************************************************");
         System.out.println("Employee ID\t  Employee Name\t\tGross Salary\tNet Salary");

         try {
             connect();
             state = con.createStatement();
             result = state.executeQuery(query);

             while (result.next()) {
                 int noOfPresent = result.getInt("no_of_present");
                 noOfHoursLate = result.getInt("no_of_hours_late"); 
                 int payPerDay = result.getInt("position_pay_per_day");
                 int employeeId = result.getInt("employee_id");
                 String employeeName = result.getString("employee_name");

                 calculateSalaries(noOfPresent, payPerDay);
                 netSalary = getNetSalary();

                 System.out.printf("%-20d%-20s%-15d%-15.2f\n", employeeId, employeeName, grossSalary, netSalary);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     private void calculateSalaries(int noOfPresent, int payPerDay) {
         grossSalary = payPerDay * noOfPresent;
     }

     private float getNetSalary() {
         int lateDeductions = 50;
         int tax = calculateTax(grossSalary);
         return grossSalary - (tax + SSS+ MEDICARE + (noOfHoursLate * lateDeductions));
     }

     private int calculateTax(float grossSalary) {
         if (grossSalary <= 20000) {
             return 0;
         } else if (grossSalary <= 30000) {
             return 1000;
         } else if (grossSalary <= 40000) {
             return 2000;
         } else {
             return 3000;
         }
     }
     
     /**
      * Displays employee payroll details based on the specific employeeId
      * @param employeeId
      */
     public void displayEmployeePayroll(int employeeId) {
     	
     	String query = "SELECT attendances.*, positions.position_pay_per_day, positions.position_name, employees.employee_name "
     	        + "FROM attendances "
     	        + "INNER JOIN employees ON attendances.employee_id = employees.employee_id "
     	        + "INNER JOIN positions ON positions.position_id = employees.position_id "
     	        + "WHERE employees.employee_id = " + employeeId + " "
     	        + "ORDER BY employees.employee_id";
     	
     	if (!isEmployeeExists(employeeId)) {
 	        System.out.println("Employee with ID " + employeeId + " does not exist.");
 	        return;
 	    }
     	
     	
         System.out.println("*************************************");
         System.out.println("\t\tEmployee Payroll");
         System.out.println("*************************************");

         try {
             connect();
             state = con.createStatement();
             result = state.executeQuery(query);

             while (result.next()) {
                 int noOfPresent = result.getInt("no_of_present");
                 noOfHoursLate = result.getInt("no_of_hours_late");
                 int payPerDay = result.getInt("position_pay_per_day");
                 String employeeName = result.getString("employee_name");
                 String positionName = result.getString("position_name");

                 calculateSalaries(noOfPresent, payPerDay);
                 netSalary = getNetSalary();

                 displayPayrollDetails(employeeId, employeeName, positionName, payPerDay, noOfPresent);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     private void displayPayrollDetails(int employeeId, String employeeName, String positionName, int payPerDay, int noOfPresent) {
         System.out.println("EmployeeName: " + employeeName);
         System.out.println("Employee ID: " + employeeId);
         System.out.println("Position: " + positionName);
         System.out.println("Salary Rate: " + payPerDay);
         System.out.println("***********************************************\n");

         System.out.println("Date Covered: December 1-30 2023");
         System.out.println("Total No. of Days Present: " + noOfPresent);
         System.out.println("Gross Salary: " + grossSalary);
         System.out.println("Deductions: \n"
                 + "\nTax: " + calculateTax(netSalary)
                 + "\nSSS: " + SSS
                 + "\nMedicare: " + MEDICARE);
         System.out.println("***********************************************\n");
         System.out.println("Net Salary: " + netSalary);
     }

 	
	/**
	 * API for adding a new employee to the system.
	 * Inserts employee information into the 'employees' table, generates an employee ID, 
	 * adds employee credentials, and initializes attendance records.
	*/
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

 	
 	/**
	 * Adds credentials for a new employee to the 'users' table.
	 *
	 **/
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
 	
 	/**
     * Adds a new attendance record for the employee with its given ID.
    */
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
 	
 	
 	/**
     * Checks if an employee with the given ID exists in the database.
     * return True if the employee exists, False if not.
    */
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
 	
 	/**
     * API for deleting an employee with the given ID.
    */
 	public void deleteEmployee(int employeeId) {

 		//Check if the employeeId exist in our Database
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
