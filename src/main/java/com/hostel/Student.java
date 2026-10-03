package com.hostel;

public class Student {

    private int registerNo;
    private String name;
    private String department;
    private String roomNo;
    private String phone;
    private String status;

    public Student(int registerNo, String name, String department,
                   String roomNo, String phone, String status) {

        this.registerNo = registerNo;
        this.name = name;
        this.department = department;
        this.roomNo = roomNo;
        this.phone = phone;
        this.status = status;
    }

    public int getRegisterNo() {
        return registerNo;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }
}