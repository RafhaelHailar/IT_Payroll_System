
package com.app.main;

public class Employee {
    private String name,sex,hiredDate,positionName;
    private int age,positionId,salaryPerDay,daysPresent,
                taxDeduction,sssDeduction,medicareDeduction,
                grossSalary,hoursLate,lateDeduction;
    private float netSalary;
    private boolean isSuspended,isSuccess = false;
        
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHiredDate() {
        return hiredDate;
    }

    public void setHiredDate(String hiredDate) {
        this.hiredDate = hiredDate;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPositionId() {
        return positionId;
    }
    
    public boolean getIsSuccess() {
        return this.isSuccess;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public boolean isIsSuspended() {
        return isSuspended;
    }

    public void setIsSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }
    
    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    
    
    public int getDaysPresent() {
        return daysPresent;
    }

    public void setDaysPresent(int daysPresent) {
        this.daysPresent = daysPresent;
    }

    public int getSalaryPerDay() {
        return salaryPerDay;
    }

    public void setSalaryPerDay(int salaryPerDay) {
        this.salaryPerDay = salaryPerDay;
    }
    
    
    public int getTaxDeduction() {
        return taxDeduction;
    }

    public void setTaxDeduction(int taxDeduction) {
        this.taxDeduction = taxDeduction;
    }

    public int getSSSDeduction() {
        return sssDeduction;
    }

    public void setSSSDeduction(int sssDeduction) {
        this.sssDeduction = sssDeduction;
    }

    public int getMedicareDeduction() {
        return medicareDeduction;
    }

    public void setMedicareDeduction(int medicareDeduction) {
        this.medicareDeduction = medicareDeduction;
    }

    public int getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(int grossSalary) {
        this.grossSalary = grossSalary;
    }

    public float getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(float netSalary) {
        this.netSalary = netSalary;
    }   
    
    public int getHoursLate() {
        return hoursLate;
    }

    public void setHoursLate(int hoursLate) {
        this.hoursLate = hoursLate;
    }
    
    public int getLateDeduction() {
        return lateDeduction;
    }

    public void setLateDeduction(int lateDeduction) {
        this.lateDeduction = lateDeduction;
    }
}
