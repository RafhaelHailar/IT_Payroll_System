
package com.app.frontend;

import com.app.backend.Function;
import com.app.main.Main;
import com.app.main.Employee;

public abstract class InputData {
    int level = 1;
     
    public static final String[] availableMonths = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    
    
    public void clear() {
        this.level = 1;
    }
    
    public void prevLevel() {
        int currLevel = this.level;
        
        currLevel--;
        if (currLevel < 1) currLevel = 1;
        
        this.level = currLevel;
    }
    
    Function function;
    
    // login input data
    public static class LoginInputData extends InputData {
        private int userId;
        private String userPassword;
        
        public LoginInputData(Function function) {
            this.function = function;
        }
        

        public void addData(String data) {
            switch (this.level) {
                case 1:
                    try {
                        userId = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    } 
                    this.level++;
                    break;
                case 2:
                    userPassword = data;
                    System.out.println("\n* Logging you in......");
                    
                    if (Main.getAdminId() == userId) {
                       if (userPassword.equals(Main.getAdminPass())) {
                          Main.setUserID(userId);
                       }
                    } else {
                       this.login();
                    }
                   
                    this.level++;
                    break;
            }
        }
        
        public int getUserId() {
            return this.userId;
        }
        
        public String getUserPassword() {
            return this.userPassword;
        }

        public void login() {
            boolean result = function.login(userId,userPassword);
            if (result) {
                Main.setUserID(userId);
            }
        }
    }
    
    // suspend input data
    public static class SuspendInputData extends InputData {
        private int employeeId;
        private boolean confirmSuspend;
        private boolean toSuspend;

        public SuspendInputData(Function function,boolean toSuspend) {
            this.function = function;
            this.toSuspend = toSuspend;
        }

        public void addData(String data) {
            switch (this.level) {
                case 1:
                    try {
                        employeeId = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    } 
                    this.level++;
                    break;
                case 2:
                    try {
                        confirmSuspend = data.toLowerCase().equals("y") || data.toLowerCase().equals("yes");
                        String prefix = "* " + (this.toSuspend ? "S" : "Uns");

                        if (confirmSuspend) {
                            System.out.println(prefix + "uspending......");
                            
                            if (this.toSuspend) {
                               this.suspend();
                            } else {
                               this.unSuspend();
                            }
                            this.level++;
                        } else {
                            System.out.println(prefix + "uspending Cancelled!");
                            this.clear();
                        }
                        
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
            }
        }

        public void suspend() {
            boolean result = function.suspendEmployee(employeeId);

            System.out.println("* Suspension " + (result ? "Success" : "Failed") + "!");
        }
        
        public void unSuspend() {
            boolean result = function.unSuspendEmployee(employeeId);
            
            System.out.println("* Unsuspension " + (result ? "Success" : "Failed") + "!");
        }
    }
    
    // create employee input data
    public static class CreateInputData extends InputData {
        private String employeeName;
        private String employeePassword;
        private char employeeSex;
        private int employeeAge;
        private int positionId;
        public int positionIdLimit;

        public CreateInputData(Function function) {
            this.function = function;
        }

        public void addData(String data) {
            switch (this.level) {
                case 1:
                    employeeName = data;
                    this.level++;
                    break;
                case 2:
                    employeePassword = data;
                    this.level++;
                    break;
                case 3:
                    char sex = data.charAt(0);
                    
                    if (sex != 'M' && sex != 'F') {
                      System.out.println("*--*:: Please enter a valid input ('M' | 'F')!");  
                    } else {
                        employeeSex = data.charAt(0);
                        this.level++;
                    }
                    
                    break;
                case 4:
                    try {
                        int age = Integer.parseInt(data);
                        employeeAge = age;
                        
                        if (age < 0) {
                            System.out.println("*--*:: Enter a valid age!");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("*--*:: Invalid input: " + data + ", please enter a valid number!");
                        break;
                    }
                    this.level++;
                    break;
                case 5:
                    try {
                        int id = Integer.parseInt(data);
                        positionId = id;
                        
                        // check if the input id is in range
                        if (id < 1 || id > positionIdLimit) {
                            System.out.println("*--*:: Enter a number between 1" + "-" + positionIdLimit + "!");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("*--*:: Invalid input: " + data + ", please enter a valid number!");
                        break;
                    }
                    
                    System.out.println("* Creating employee...");
                    createEmployee();
                    this.level++;
                    break;
            }
        }
        
                    
        private void createEmployee() {
            boolean creationSuccess = function.addEmployee(employeeName, String.valueOf(employeeSex), employeeAge, positionId, employeePassword);
            
            if (creationSuccess) {
                System.out.println("* Creation Success!");
            } else {
                System.out.println("* Creation Failed!");
            }
        }
    }
    
    //input data for deleting
    public static class DeleteInputData extends InputData {
        private int employeeId;
        private boolean confirmDelete;
        
        public DeleteInputData(Function function) {
            this.function = function;
        }
        
        public void addData(String data) {
            switch (this.level) {
               case 1:
                    try {
                        employeeId = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    } 
                    this.level++;
                    break;
                case 2:
                    try {
                        confirmDelete = data.toLowerCase().equals("y") || data.toLowerCase().equals("yes");

                        if (confirmDelete) {
                            System.out.println("* Deleting......");
                            
                            delete();
                            this.level++;
                        } else {
                            System.out.println("* Deleting Cancelled!");
                            this.clear();
                        }
                        
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                }
            }
        
            public void delete() {
                boolean isSuccess = function.deleteEmployee(employeeId);
                
                if (isSuccess) {
                    System.out.println("* Deletion Success!");
                } else {
                    System.out.println("* Deletion Failed!");
                }
            }
    }
    
    //input data for setting employee attendance
    public static class AttendanceInputData extends InputData {
        private int employeeId,year,daysPresent,
                daysAbsent,daysLate,hoursLate;
        private String month;

        public AttendanceInputData(Function function) {
            this.function = function;
        }
        
        public void addData(String data) {
            switch (this.level) {
                case 1:
                    try {
                        employeeId = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + employeeId + ", please input a valid number!");
                        break;
                    }
                    this.level++;
                    break;
                case 2:
                    try {
                        year = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    }
                    this.level++;
                    break;
                case 3:
                   
                    try {
                        int index  = Integer.parseInt(data);
                        month = availableMonths[index - 1];
                    } catch (NumberFormatException e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("*--*:: Please only enter a number between 1 - 12");
                        break;
                    } catch (Exception e) {
                        System.out.println(e);
                        break;
                    }
                       
                    this.level++;

                    break; 
                case 4:
                    try {
                        daysPresent = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    }
                    this.level++;
                    break;
                case 5:
                    try {
                        daysAbsent = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    }
                    this.level++;
                    break;
                case 6:
                    try {
                        daysLate = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    }
                    this.level++;
                    break;
                case 7:
                    try {
                        hoursLate = Integer.parseInt(data);
                        setAttendance();
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    }
                    this.level++;
                    break;
            }
        }
        
        public void setAttendance() {
            boolean isSuccess = function.setAttendance(employeeId, year, month, daysPresent, daysAbsent, daysLate, hoursLate);
            
            if (isSuccess) {
                System.out.println("* Setting attendance success!...");
            } else {
                System.out.println("* Setting attendance failed!...");
            }
            
        }
    }
    
    public static class SalaryInputData extends InputData {
        private int employeeId;
        private int year;
        private String month;
        
        public SalaryInputData(Function function) {
            this.function = function;
        }
        
        public void addData(String data) {
            switch (this.level) {
                case 1:
                    try {
                        year = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    }
                    this.level++;
                    break;
                case 2:
                    try {
                        int index  = Integer.parseInt(data);
                        month = availableMonths[index - 1];
                    } catch (NumberFormatException e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("*--*:: Please only enter a number between 1 - 12");
                        break;
                    } catch (Exception e) {
                        System.out.println(e);
                        break;
                    }
                       
                    this.level++;
                    break;
                case 3:
                    try {
                        employeeId = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    }
                    
                    displayEmployeeDetails();
                    this.level++;
                    break;
            }
        }
        
        public boolean displaySalaries() {
            System.out.println(" "); // for spacing
            System.out.println(" "); // for spacing
            System.out.println(" "); // for spacing
            System.out.println("********************************************************************");
            System.out.println("*\t\t\t\t\t\t\t\t   *");
            System.out.println("*\t\t     Payroll for "+ month + " " + year + "\t\t            *");
            System.out.println("*\t\t\t\t\t\t\t\t   *");
            System.out.println("********************************************************************");
            
            boolean hasResult = function.displayEmployeeSalary(0, year, month);
            
            if (!hasResult) {
                System.out.println("* Payroll for " + month + " " + year + " have no data!");
                System.out.println(" "); // for spacing
                System.out.println(" "); // for spacing
            }
            
            return hasResult;
        }
        
        public void reDisplaySalaries() {
            displaySalaries();
        }
        
        public void displayEmployeeDetails() {
            View.setEmployeeViewing(employeeId);
            View.setDateViewing(year, month);
        }
    }
    
    public static class ChangeDateInputData extends InputData {
        private int year;
        private String month;
        private boolean active = false;
        
        public ChangeDateInputData(Function function) {
            this.function = function;
        }
        
        public void addData(String data) {
            if (!active) return;
            
            switch (this.level) {
                case 1:
                    try {
                        year = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    }
                    this.level++;
                    break;
                case 2:
                    try {
                        int index  = Integer.parseInt(data);
                        month = availableMonths[index - 1];
                    } catch (NumberFormatException e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("*--*:: Please only enter a number between 1 - 12");
                        break;
                    } catch (Exception e) {
                        System.out.println(e);
                        break;
                    }
                    changeDate();
                    break;
            }
        }
        
        public void setActive(boolean active) {
            this.active = active;
        }
       
        public boolean getActive() {
            return this.active;
        }
        
        public void changeDate() {
            this.active = false;
            this.clear();
            View.setDateViewing(year, month);
        }
    }
    
    public static class UpdateEmployeeInputData extends InputData {
        private int employeeId;
        private boolean confirmUpdate;
        private int toUpdate = 0;
        private String employeeName,employeeSex;
        private int employeeAge,positionId,positionIdLimit;
        
        public UpdateEmployeeInputData(Function function) {
            this.function = function;
        }
        
        public void addData(String data) {
            switch (this.level) {
                case 1:
                    try {
                        employeeId = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    }
                    this.level++;
                    break;
                case 3:
                    employeeName = data;
                    setLevel(8);
                    break;
                case 4:
                    char sex = data.charAt(0);
                    
                    if (sex != 'M' && sex != 'F') {
                      System.out.println("*--*:: Please enter a valid input ('M' | 'F')!");  
                    } else {
                        employeeSex = String.valueOf(data.charAt(0));
                        setLevel(8);
                    }
                    break;
                case 5:
                    try {
                        employeeAge = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("*--*:: Invalid input: " + data + ", please input a valid number!");
                        break;
                    }
                    setLevel(8);
                    break;
                case 6:
                    try {
                        int id = Integer.parseInt(data);
                        positionId = id;
                        
                        // check if the input id is in range
                        if (id < 1 || id > positionIdLimit) {
                            System.out.println("*--*:: Enter a number between 1" + "-" + positionIdLimit + "!");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("*--*:: Invalid input: " + data + ", please enter a valid number!");
                        break;
                    }
                    setLevel(8);
                    break;
                case 8:
                    try {
                        confirmUpdate = data.toLowerCase().equals("y") || data.toLowerCase().equals("yes");

                        if (confirmUpdate) {
                            System.out.println("* Updating......");
                            
                            update();
                            this.level++;
                        } else {
                            System.out.println("* Updating Cancelled!");
                            this.clear();
                        }
                        
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
            }
        }
        
        public void update() {
            String data = "";
            
            switch (toUpdate) {
                case 1:
                    data = employeeName;
                    break;
                case 2:
                    data = employeeSex;
                    break;
                case 3:
                    data = String.valueOf(employeeAge);
                    break;
                case 4:
                    data = String.valueOf(positionId);
                    break;
            }
            
            function.updateEmployeeBasicInfo(employeeId,toUpdate,data);
        }
        
        public Employee getEmployeeBasicInfo() {
            return function.getEmployeeBasicInfo(employeeId);
        }
        
        public void displayEmployeeNotFound() {
            System.out.println("* Employee with employee id: " + employeeId + " is not found.");
            this.clear();
        }
        
        public void setLevel(int level) {
            switch (level) {
                case 3:
                case 4:
                case 5:
                case 6:
                    toUpdate = level - 2;
                    break;
            }
            this.level = level;
        }
        
        public void setPositionIdLimit(int limit) {
            positionIdLimit = limit;
        }
        
        public void setToUpdate(int toUpdate) {
            this.toUpdate = toUpdate;
        }
    }
}
