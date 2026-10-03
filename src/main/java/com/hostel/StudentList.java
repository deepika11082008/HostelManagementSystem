package com.hostel;

public class StudentList {

    // Node represents one student
    private class Node {

        Student student;
        Node next;

        Node(Student student) {
            this.student = student;
            this.next = null;
        }
    }

    // First node of the linked list
    private Node head;

    // Add student
    public void addStudent(Student student) {

        Node newNode = new Node(student);

        if (head == null) {
            head = newNode;
            return;
        }

        Node current = head;

        while (current.next != null) {
            current = current.next;
        }

        current.next = newNode;
    }

    // Search student by register number
    public Student searchStudent(int registerNo) {

        Node current = head;

        while (current != null) {

            if (current.student.getRegisterNo() == registerNo) {
                return current.student;
            }

            current = current.next;
        }

        return null;
    }

    // Delete student
    public boolean deleteStudent(int registerNo) {

        if (head == null) {
            return false;
        }

        if (head.student.getRegisterNo() == registerNo) {
            head = head.next;
            return true;
        }

        Node current = head;

        while (current.next != null) {

            if (current.next.student.getRegisterNo() == registerNo) {
                current.next = current.next.next;
                return true;
            }

            current = current.next;
        }

        return false;
    }

    // Display all students
    public void displayStudents() {

        Node current = head;

        while (current != null) {

            Student s = current.student;

            System.out.println(
                s.getRegisterNo() + " | " +
                s.getName() + " | " +
                s.getDepartment() + " | " +
                s.getRoomNo()
            );

            current = current.next;
        }
    }
}