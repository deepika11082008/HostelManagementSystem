package com.hostel;

import java.util.HashMap;

public class StudentManager {

    private HashMap<Integer, Student> studentMap;

    public StudentManager() {
        studentMap = new HashMap<>();
    }

    // Add student
    public void addStudent(Student student) {
        studentMap.put(student.getRegisterNo(), student);
    }

    // Search student
    public Student searchStudent(int registerNo) {
        return studentMap.get(registerNo);
    }

    // Remove student
    public void removeStudent(int registerNo) {
        studentMap.remove(registerNo);
    }

    // Check whether student exists
    public boolean containsStudent(int registerNo) {
        return studentMap.containsKey(registerNo);
    }

    // Number of students
    public int getStudentCount() {
        return studentMap.size();
    }
}