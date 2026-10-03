package com.hostel;

import java.util.LinkedList;
import java.util.Queue;

public class RequestQueue {

    private Queue<String> requests;

    public RequestQueue() {
        requests = new LinkedList<>();
    }

    // Add a new request
    public void addRequest(String request) {
        requests.offer(request);
    }

    // Process the oldest request
    public String processRequest() {

        if (requests.isEmpty()) {
            return "No pending requests";
        }

        return requests.poll();
    }

    // View the first request
    public String frontRequest() {

        if (requests.isEmpty()) {
            return "No pending requests";
        }

        return requests.peek();
    }

    // Check whether queue is empty
    public boolean isEmpty() {
        return requests.isEmpty();
    }

    // Number of pending requests
    public int size() {
        return requests.size();
    }
}