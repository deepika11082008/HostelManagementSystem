package com.hostel;

import java.util.Stack;

public class ActionStack {

    private Stack<String> actions;

    public ActionStack() {
        actions = new Stack<>();
    }

    // Add latest action
    public void pushAction(String action) {
        actions.push(action);
    }

    // Remove and return latest action
    public String undoAction() {

        if (actions.isEmpty()) {
            return "No action to undo";
        }

        return actions.pop();
    }

    // View latest action
    public String peekAction() {

        if (actions.isEmpty()) {
            return "No actions available";
        }

        return actions.peek();
    }

    // Check whether stack is empty
    public boolean isEmpty() {
        return actions.isEmpty();
    }

    // Number of actions
    public int size() {
        return actions.size();
    }
}