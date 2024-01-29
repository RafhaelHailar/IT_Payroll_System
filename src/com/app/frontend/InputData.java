
package com.app.frontend;

import com.app.backend.Function;
import com.app.main.Main;
import com.app.main.Employee;

public abstract class InputData {
    int level = 1;
    
    public void clear() {
        this.level = 1;
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
                        System.out.println("Invalid input: " + data + ", please input a valid number!");
                        break;
                    } 
                    this.level++;
                    break;
                case 2:
                    userPassword = data;
                    System.out.println("Logging you in......");
                    
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
                        System.out.println("Invalid input: " + data + ", please input a valid number!");
                        break;
                    } 
                    this.level++;
                    break;
                case 2:
                    try {
                        confirmSuspend = data.toLowerCase().equals("y") || data.toLowerCase().equals("yes");
                        String prefix = (this.toSuspend ? "S" : "Uns");

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

            System.out.println("Suspension " + (result ? "Success" : "Failed") + "!");
        }
        
        public void unSuspend() {
            boolean result = function.unSuspendEmployee(employeeId);
            
            System.out.println("Unsuspension " + (result ? "Success" : "Failed") + "!");
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
                      System.out.println("Please enter a valid input ('M' | 'F')!");  
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
                            System.out.println("Enter a valid age!");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input: " + data + ", please enter a valid number!");
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
                            System.out.println("Enter a number between 1" + "-" + positionIdLimit + "!");
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input: " + data + ", please enter a valid number!");
                        break;
                    }
                    
                    System.out.println("Creating employee...");
                    createEmployee();
                    this.level++;
                    break;
            }
        }
        
                    
        private void createEmployee() {
            boolean creationSuccess = function.addEmployee(employeeName, String.valueOf(employeeSex), employeeAge, positionId, employeePassword);
            
            if (creationSuccess) {
                System.out.println("Creation Success!");
            } else {
                System.out.println("Creation Failed!");
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
                        System.out.println("Invalid input: " + data + ", please input a valid number!");
                        break;
                    } 
                    this.level++;
                    break;
                case 2:
                    try {
                        confirmDelete = data.toLowerCase().equals("y") || data.toLowerCase().equals("yes");

                        if (confirmDelete) {
                            System.out.println("Deleting......");
                            
                            delete();
                            this.level++;
                        } else {
                            System.out.println("Deleting Cancelled!");
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
                    System.out.println("Deletion Success!");
                } else {
                    System.out.println("Deletion Failed!");
                }
            }
    }
    
    //input data for viewing specific employee
    public static class ToViewEmployeeInputData extends InputData {   
        private int employeeId;
        public ToViewEmployeeInputData(Function function) {
            this.function = function;
        }
        
        public void addData(String data) {
            switch (this.level) {
                case 1:  
                    try {
                        employeeId = Integer.parseInt(data);
                    } catch (Exception e ) {
                        System.out.println("Invalid input: " + employeeId + ", please input a valid number!");
                    }

                    boolean employeeExists = function.isEmployeeExists(employeeId);
                    
                    if (!employeeExists) {
                        System.out.println("Employee ID: " + employeeId + " does not exist.");
                    } else {
                        View.setEmployeeViewing(employeeId);
                        this.level++;
                    }
                    break;
            }
        }
        
    }
}
