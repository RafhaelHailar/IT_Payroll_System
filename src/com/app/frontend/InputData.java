
package com.app.frontend;

import com.app.backend.Function;
import com.app.main.Main;

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
                        this.level++;
                    } catch (NumberFormatException e ) {
                        System.out.println("Invalid input: " + data + ", please input a valid number!");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 2:
                    userPassword = data;
                    System.out.println("Logging you in......");
                    this.login();
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
                        this.level++;
                    } catch (NumberFormatException e ) {
                        System.out.println("Invalid input: " + data + ", please input a valid number!");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
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

}
