package com.app.backend;

import java.time.LocalDate;

import com.app.frontend.View;
import java.sql.ResultSet;
import java.sql.Statement;
import com.app.main.Employee;

public class Function extends DBConnection {
     // for pagination
     public int rowLimit = 5; //the row limit. 
     private static int funcId = -1; //remembering the last display method called.
     private int positionIdEntered;
     public static boolean dataDisplaying = false;
     public static int totalPage = 0; //limit of pagination.
     
     // for sorting
     private final String[] availableToSort = {"employees.employee_id","employees.employee_name","employees.employee_age","employees.employee_hired_date","positions.position_pay_per_day"};
     private int sortBy = 0;
     private boolean isSortAscending = false; 
     
     // for name filtering
     private String nameFilter = "";
     
     int yearTarget ; // remembering the entered year previously for pagination
     String monthTarget; // same as above but month.
     
     // benefits
     private final int SSS = 1000;
     private final int MEDICARE = 500;
     
     // late
     private final int lateDeductions = 50;

     private float netSalary = 0;
     private int grossSalary = 0;
     private int noOfHoursLate;
     
     // create api
     public boolean addEmployee(String employeeName, String employeeSex, int employeeAge, int positionId, String password) {
 	    String query = "INSERT INTO employees (employee_name, employee_sex, employee_age, position_id) VALUES (?, ?, ?, ?)";
 	    boolean isSuccess = false;
 	    	
 	    try {
 	        connect();
 	        prep = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

 	        prep.setString(1, employeeName);
 	        prep.setString(2, employeeSex);
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
 	        displayEmployeeBasicInfo(0);
                
                isSuccess = true;
 	    } catch (Exception e) {
 	        System.out.println("Connection Error" + e.getMessage());
 	    }
            
            return isSuccess;
 	}

 	
 	/**
	 * Adds credentials for a new employee to the 'users' table.
	 *  
	 **/
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
 	
 	
        /**
        * Adds a new attendance record for the employee with its given ID.
       */
 	public void addToAttendances(int employeeId) {
            LocalDate date = LocalDate.now();
            int year = date.getYear();
            String unformatMonth = String.valueOf(date.getMonth()).toLowerCase();
            String month = unformatMonth.substring(0,1).toUpperCase() + unformatMonth.substring(1,unformatMonth.length());

             String attendanceQuery = "INSERT INTO attendances (employee_id,year,month) VALUES (?,?,?)";

             try {
                     connect();
                     prep = con.prepareStatement(attendanceQuery);
                     prep.setInt(1, employeeId);
                     prep.setInt(2, year);
                     prep.setString(3, month);
                     prep.executeUpdate();
                     con.close();
             }catch(Exception e) {
                     System.out.println(e);
             }
 	}
     
     
     // read api's
    public Employee getEmployeeBasicInfo(int employeeId) {
        String query = "SELECT * FROM employees INNER JOIN positions ON employees.position_id = positions.position_id "
                + "WHERE employee_id = " + employeeId;
        Employee employee = new Employee();
        
        try {
            connect();
            state = con.createStatement();
            result = state.executeQuery(query);
            while (result.next()) {
                String name = result.getString("employee_name");
                String sex = result.getString("employee_sex");
                int age = result.getInt("employee_age");
                int positionId = result.getInt("position_id");
                String positionName = result.getString("position_name");
                String hiredDate = result.getString("employee_hired_date");
                
                employee.setName(name);
                employee.setSex(sex);
                employee.setAge(age);
                employee.setPositionId(positionId);
                employee.setPositionName(positionName);
                employee.setHiredDate(hiredDate);
                employee.setIsSuccess(true);
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return employee;
    }
    
    // get employee id, by user id
    public int getEmployeeIdByUserId(int userId) {
        String query = "SELECT employee_id FROM USERS WHERE user_id = " + userId;
        int employeeId = -1;
        
        try {
            connect();
            
            state = con.createStatement();
            result = state.executeQuery(query);
            
            while (result.next()) {
                int id = result.getInt("employee_id");
                employeeId = id;
            }
            
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return employeeId;
    }
        
    // get specific employee data
    public Employee getEmployeeData(int employeeId,int year,String month) {
        String query = "SELECT * FROM employees " 
                + "INNER JOIN positions ON " 
                + "employees.position_id = positions.position_id INNER JOIN attendances "
                + "ON employees.employee_id = attendances.employee_id WHERE employees.employee_id = ? "
                + "AND year = " + year + " AND " + "month = '" + month + "'";
        Employee employeeData = new Employee();
        
        try {
            connect();
            
            prep = con.prepareStatement(query);
            prep.setInt(1,employeeId);
            
            result = prep.executeQuery();
          
            while (result.next()) {
                String name = result.getString("employee_name");
                String sex = result.getString("employee_sex");
                int age = result.getInt("employee_age");
                int positionId = result.getInt("position_id");
                String positionName = result.getString("position_name");
                String hiredDate = result.getString("employee_hired_date");
                boolean isSuspended = result.getBoolean("employee_is_suspended");
                int salaryPerDay = result.getInt("position_pay_per_day");
                int daysPresent = result.getInt("no_of_present");
                int hoursLate = result.getInt("no_of_hours_late");

                employeeData.setName(name);
                employeeData.setSex(sex);
                employeeData.setAge(age);
                employeeData.setPositionId(positionId);
                employeeData.setPositionName(positionName);
                employeeData.setHiredDate(hiredDate);
                employeeData.setIsSuspended(isSuspended);
                employeeData.setIsSuccess(true);
                employeeData.setSalaryPerDay(salaryPerDay);
                employeeData.setDaysPresent(daysPresent);
                employeeData.setHoursLate(hoursLate);
                employeeData.setLateDeduction(lateDeductions);

                calculateSalaries(daysPresent, salaryPerDay);

                employeeData.setTaxDeduction(calculateTax(grossSalary));
                employeeData.setSSSDeduction(SSS);
                employeeData.setMedicareDeduction(MEDICARE);
                employeeData.setGrossSalary(grossSalary);
                employeeData.setNetSalary(getNetSalary());
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return employeeData;
    }
      
    // FOR READING TOTAL ROW
    private void getTotalPage(String table,String condition) {
        String query = "SELECT COUNT(*) FROM " + table + " WHERE " + condition;
        int total = 0;
        
        try {
            connect();
          
            state = con.createStatement();
            result = state.executeQuery(query);
            
            result.next();
            total = result.getInt(1);
            
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        
        totalPage = (int) Math.ceil(((double) total) / rowLimit);
        
        int space = 70;
        System.out.printf("%-" + space + "s%-10s\n"," ","PAGE: " + (View.currResultRowSpan + 1) + "/" + (Function.totalPage));
        
        String[] sortName = availableToSort[sortBy].split("\\.");
        String[] sortAtName = sortName[1].toUpperCase().split("_");
        String sortAt = sortAtName[1];
        System.out.printf("%-" + space + "s%-10s\n"," ","SORTED BY: " + sortAt + " " + (isSortAscending? "Ascending" : "Descending"));
        
        System.out.printf("%-" + space + "s%-10s\n"," ","NAME FILTERED BY: " + nameFilter);
    }
       
    /**
     * Checks if an employee with the given ID exists in the database.
     * return True if the employee exists, False if not.
    */
    //METHOD FOR CHECKING IF THE EMPLOYEE EXISTS IN OUR DATABASE
    public boolean isEmployeeExists(int employeeId) {
        String checkQuery = "SELECT COUNT(*) FROM employees WHERE employee_id = ?";
        try {
            connect();
            prep = con.prepareStatement(checkQuery);
            prep.setInt(1, employeeId);
            result = prep.executeQuery();
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
      * Displays the salary information for employees for the specified month.
     */
     public boolean displayEmployeeSalary(int rowSpan,int year,String month) {
        boolean hasResult = false;
        String query = "SELECT attendances.*, positions.position_pay_per_day, employees.employee_name "
                + "FROM attendances "
                + "INNER JOIN employees ON attendances.employee_id = employees.employee_id "
                + "INNER JOIN positions ON positions.position_id = employees.position_id "
                + "WHERE attendances.year = " + year + " AND attendances.month = '" + month + "' "
                + "AND employees.employee_name LIKE '%" + nameFilter + "%' "
                + "ORDER BY " + availableToSort[sortBy] + " " + (isSortAscending ? "ASC" : "DESC")
                + " LIMIT " + (rowLimit * rowSpan) + ", " + rowLimit;
        
        getTotalPage("attendances INNER JOIN employees ON attendances.employee_id = employees.employee_id","year = " + year +" AND month = '" + month + "' AND employees.employee_name LIKE '%" + nameFilter + "%'"); // get the total page, pagination can move.
        yearTarget = year;
        monthTarget = month;
        
        try {
            connect();
            state = con.createStatement();
            result = state.executeQuery(query);
            
            System.out.printf("%-20s | %-20s | %-20s | %-20s\n","Employee ID","Employee Name","Gross Salary","Net Salary");
            
            while (result.next()) {
                int noOfPresent = result.getInt("no_of_present");
                int noOfAbsent = result.getInt("no_of_absent");
                int noOfHoursLate = result.getInt("no_of_hours_late");
                int payPerDay = result.getInt("position_pay_per_day");
                int employeeId = result.getInt("employee_id");
                String employeeName = result.getString("employee_name");

                calculateSalaries(noOfPresent, payPerDay);
                netSalary = getNetSalary();

                System.out.printf("%-20d | %-20s | %-20s | %-20s",employeeId,employeeName, "P " + View.formatNumber(grossSalary), "P " + View.formatNumber(netSalary));
                System.out.println(noOfPresent > 0 ? "" : "--JUST HIRED!");
                
                hasResult = true;
            }
            
             System.out.println(" "); // for spacing
             
             con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        funcId = 5;
        dataDisplaying = true;
        
        return hasResult;
    }
     
     private void calculateSalaries(int noOfPresent, int payPerDay) {
         grossSalary = payPerDay * noOfPresent;
     }

     private float getNetSalary() {
         int tax = calculateTax(grossSalary);
         return grossSalary - (tax + SSS + MEDICARE + (noOfHoursLate * lateDeductions));
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
       * Retrieves and displays information about all active employees.
       * It also filters the employees that are not suspended
       * Displays employee ID, name, gender, age, and their respective position.
       * 
     */ 
     public void displayAllUnsuspendEmployeeInfo(int rowSpan) {
        String query = "SELECT * FROM employees "
                     + "INNER JOIN positions ON employees.position_id = positions.position_id "
                     + "WHERE employee_is_suspended = 0 "
                     + "AND employees.employee_name LIKE '%" + nameFilter + "%' "
                     + "ORDER BY " + availableToSort[sortBy] + " " + (isSortAscending ? "ASC" : "DESC")
                     + " LIMIT " + (rowLimit * rowSpan) + ", " + rowLimit;
        
        getTotalPage("employees","employee_is_suspended = 0 AND employees.employee_name LIKE '%" + nameFilter + "%'"); // get the total page, pagination can move.
        
        try {
            connect();
            state = con.createStatement();
            result = state.executeQuery(query);

            System.out.printf("%-5s | %-20s  | %s | %-4s | %-20s | %-15s | %s\n","ID","NAME","SEX","AGE","POSITION","HIRED DATE", "STATUS");

            while (result.next()) {
                int id = result.getInt("employee_id");
                String name = result.getString("employee_name");
                String sex = result.getString("employee_sex");
                int age = result.getInt("employee_age");
                String position = result.getString("position_name");
                String hiredDate = result.getString("employee_hired_date");
                boolean isSuspended = result.getBoolean("employee_is_suspended");

                System.out.printf("%-4s | %-20s  | %-3s | %-4d | %-20s | %-15s | %s\n","[" + id + "]",name,sex,age,position,hiredDate, (isSuspended ? "SUSPENDED" : ""));
            }
        } catch (Exception e) {
            System.out.println("Connection Error: " + e.getMessage());
        }
        
        funcId = 3;
        dataDisplaying = true;
    }
     
     public boolean login(int userId,String password) {
         String query = "SELECT * FROM users WHERE user_id = " + userId;
         boolean isLogged = false;
         
         try {
             connect();
             
             state = con.createStatement();
             result = state.executeQuery(query);
             
             while (result.next()) {
                String userPassword = result.getString("user_password");
             
                if (userPassword.equals(password)) {
                    isLogged = true;
                }
             }
             
             con.close();
         } catch (Exception e) {
             System.out.println(e);
         }
         
         return isLogged;
     }
     
          
     public void displayEmployeeBasicInfo(int rowSpan){
       String query = "SELECT * FROM employees INNER JOIN positions "
              + "ON employees.position_id = positions.position_id "
              + "WHERE employees.employee_name LIKE '%" + nameFilter + "%' "
              + "ORDER BY " + availableToSort[sortBy] + " " + (isSortAscending ? "ASC" : "DESC")
              + " LIMIT " + (rowLimit * rowSpan) + ", " + rowLimit;
       
       getTotalPage("employees","employees.employee_name LIKE '%" + nameFilter + "%'"); // get the total page, pagination can move.
       
       try {
           connect();
           state = con.createStatement();
           result = state.executeQuery(query);
          
           System.out.printf("%-5s | %-20s  | %s | %-4s | %-20s | %-15s | %s\n","ID","NAME","SEX","AGE","POSITION","HIRED DATE", "STATUS");
           
           while(result.next()){
               int id = result.getInt("employee_id");
               String name = result.getString("employee_name");
               String sex = result.getString("employee_sex");
               int age = result.getInt("employee_age");
               String position = result.getString("position_name");
               String hiredDate = result.getString("employee_hired_date");
               boolean isSuspended = result.getBoolean("employee_is_suspended");
               
               System.out.printf("%-4s | %-20s  | %-3s | %-4d | %-20s | %-15s | %s\n","[" + id + "]",name,sex,age,position,hiredDate, (isSuspended ? "SUSPENDED" : ""));
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
                 + "AND employees.employee_name LIKE '%" + nameFilter + "%' "
                 + "ORDER BY " + availableToSort[sortBy] + " " + (isSortAscending ? "ASC" : "DESC")
                 + " LIMIT " + (rowLimit * rowSpan) + ", " + rowLimit;
         
         getTotalPage("employees","employee_is_suspended = 1 AND employees.employee_name LIKE '%" + nameFilter + "%'"); // get the total page, pagination can move.
         
         try {
             connect();
             
             state = con.createStatement();
             result = state.executeQuery(query);
             
             System.out.printf("%-5s | %-20s  | %s | %-4s | %-20s | %-15s | %s\n","ID","NAME","SEX","AGE","POSITION","HIRED DATE", "STATUS");
             
             while (result.next()) {
               int id = result.getInt("employee_id");
               String name = result.getString("employee_name");
               String sex = result.getString("employee_sex");
               int age = result.getInt("employee_age");
               String position = result.getString("position_name");
               String hiredDate = result.getString("employee_hired_date");
               boolean isSuspended = result.getBoolean("employee_is_suspended");
               System.out.printf("%-4s | %-20s  | %-3s | %-4d | %-20s | %-15s | %s\n","[" + id + "]",name,sex,age,position,hiredDate, (isSuspended ? "SUSPENDED" : ""));
               
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
                 + " AND employees.employee_name LIKE '%" + nameFilter + "%'"
                 + " ORDER BY " + availableToSort[sortBy] + " " + (isSortAscending ? "ASC" : "DESC")
                 + " LIMIT " + (rowLimit * rowSpan) + ", " + rowLimit;
         
         getTotalPage("employees","position_id = " + positionId + " AND employees.employee_name LIKE '%" + nameFilter + "%'"); // get the total page, pagination can move.
         try {
             connect();
             
             state = con.createStatement();
             result = state.executeQuery(query);
             
             System.out.printf("%-5s | %-20s  | %s | %-4s | %-20s | %-15s | %s\n","ID","NAME","SEX","AGE","POSITION","HIRED DATE", "STATUS");
             
             while (result.next()) {
               int id = result.getInt("employee_id");
               String name = result.getString("employee_name");
               String sex = result.getString("employee_sex");
               int age = result.getInt("employee_age");
               String position = result.getString("position_name");
               String hiredDate = result.getString("employee_hired_date");
               boolean isSuspended = result.getBoolean("employee_is_suspended");
               System.out.printf("%-4s | %-20s  | %-3s | %-4d | %-20s | %-15s | %s\n","[" + id + "]",name,sex,age,position,hiredDate, (isSuspended ? "SUSPENDED" : ""));
               
             }
             
             con.close();
         } catch (Exception e) {
             System.out.println(e);
         }
          
         // for pagination
         positionIdEntered = positionId;
         funcId = 4;
         dataDisplaying = true;
     }
     
     public int displayPositionsName() {
         String query = "SELECT position_id,position_name FROM positions";
         String display = "";
         int positionIdLimit = 0;
         try {
             connect();
             
             state = con.createStatement();
             result = state.executeQuery(query);
             
             while (result.next()) {
                 String name = result.getString("position_name");
                 int id = result.getInt("position_id");
                 
                 System.out.printf("[%d]%s ", id, name);
                 positionIdLimit++;
             }
             
             con.close();
         } catch (Exception e) {
             System.out.println(e);
         }
         
         System.out.println(" "); // for spacing
         
         return positionIdLimit;
     }
     
     public void callLastDisplayMethod(int currRowSpan) {
         if (currRowSpan + 1 <= Function.totalPage) {
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
                    displayAllUnsuspendEmployeeInfo(currRowSpan);
                    break;
                case 4:
                    displayEmployeeByPosition(positionIdEntered,currRowSpan);
                    break;
                case 5:
                    displayEmployeeSalary(currRowSpan,yearTarget,monthTarget);
                    break;
            }
         }
         
        if (dataDisplaying) {
            System.out.println("\n--Type [m]More to show more results or [M]more to go back to previous results...");
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
     
     private int getAttendanceId(int employeeId,int year,String month) {
         String query = "SELECT attendance_id FROM attendances WHERE employee_id = ? AND year = ? AND month = ?";
         int id = -1;
         
         try {
             connect();
             
             prep = con.prepareStatement(query);
             prep.setInt(1, employeeId);
             prep.setInt(2, year);
             prep.setString(3, month);
             result = prep.executeQuery();
             
             while (result.next()) {
                id = result.getInt("attendance_id");
             }
             
             con.close();
         } catch (Exception e) {
             System.out.println(e);
         }
         
         return id;
     }
    
     public boolean setAttendance(int employeeId,int year,String month,int daysPresent,int daysAbsent,int daysLate,int hoursLate) {
         int attendanceId = getAttendanceId(employeeId,year,month);
         
         boolean isSuccess = false;
         
         String query = "INSERT INTO attendances (employee_id,year,month,no_of_present,no_of_absent,no_of_late,no_of_hours_late) "
                 + "VALUES (?,?,?,?,?,?,?)";
         System.out.println("ATTENDANCE ID: " + attendanceId);
         try {
             connect();
             
             if (attendanceId > -1) {
                 query = "UPDATE attendances SET no_of_present = ?,no_of_absent = ?,no_of_late = ?,no_of_hours_late = ? WHERE attendance_id = " + attendanceId;
                 
                 prep = con.prepareStatement(query);
                 prep.setInt(1, daysPresent);
                 prep.setInt(2, daysAbsent);
                 prep.setInt(3, daysLate);
                 prep.setInt(4, hoursLate);
                 
                 prep.executeUpdate();
             } else {
                 prep = con.prepareStatement(query);
                 prep.setInt(1, employeeId);
                 prep.setInt(2, year);
                 prep.setString(3, month);
                 prep.setInt(4, daysPresent);
                 prep.setInt(5, daysAbsent);
                 prep.setInt(6, daysLate);
                 prep.setInt(7, hoursLate);
                 
                 prep.executeUpdate();
             }
             
             con.close();
             
             isSuccess = true;
         } catch (Exception e) {
             System.out.println(e);
         }
         
         return isSuccess;
     }
     
    public void updateEmployeeBasicInfo(int employeeId,int toUpdate,String data) {
        String query = "UPDATE employees SET ";
        
        switch (toUpdate) {
            case 0:
                System.out.println("Invalid data field!");
                return;
            case 1:
                query += "employee_name = ?";
                break;
            case 2:
                query += "employee_sex = ?";
                break;
            case 3:
                query += "employee_age = ?";
                break;
            case 4:
                query += "position_id = ?";
                break;
        }
        
        query += " WHERE employee_id = " + employeeId;
        
        try {
            connect();
            
            prep = con.prepareStatement(query);
            
            switch (toUpdate) {
                case 1:
                case 2:
                    prep.setString(1,data);
                    break;
                case 3:
                case 4:
                    prep.setInt(1, Integer.parseInt(data));
                    break;
            }
            
            prep.executeUpdate();
            
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
     
     //delete api's
     
     /**
     * API for deleting an employee with the given ID.
    */
     //API FOR DELETING EMPLOYEE
    public boolean deleteEmployee(int employeeId) {
        boolean isSuccess = false;
        
        if (!isEmployeeExists(employeeId)) {
            System.out.println("Employee with ID " + employeeId + " does not exist.");
            return isSuccess;
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

                displayEmployeeBasicInfo(0);
                    
                isSuccess = true;
        }catch(Exception e) {
                e.printStackTrace();
        }
        
        return isSuccess;
    }
    
    public void setSortBy(int index,boolean isAscending) {
        int limit = availableToSort.length - 1;
        if (index <= limit) {
            sortBy = index;
            isSortAscending = isAscending;
            System.out.println("SORTED BY SET TO " + availableToSort[sortBy] + "! " + (isAscending? "Ascending" : "Descending"));
        } else {
            System.out.println("Input enter exceed the limit " + limit + ", please only enter a number between 0-" + limit);
        }
    }
    
    public void setFilterName(String filter) {
        nameFilter = filter;
    }
    
}
