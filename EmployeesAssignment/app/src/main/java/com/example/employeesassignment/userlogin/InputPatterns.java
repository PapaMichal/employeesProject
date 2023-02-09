package com.example.employeesassignment.userlogin;

import android.widget.EditText;

import java.util.regex.Pattern;

enum InputPatterns {
    USERNAME("Username", "^[a-zA-Z0-9_]{1,16}$", "Username may contain letters, numbers and underscores.", 1, 16),
    PASSWORD("Password", "^(?=.*[a-z])(?=.*[0-9])(?=.*[A-Z])[a-zA-Z0-9]{8,20}$", "Password must contain a lowercase letter, an uppercase letter, and a number", 8, 20),
    EMAIL("Email", "^(?=.{0,254}$)[\\w]+@[\\w]+(\\.[-\\w]+)*$", "Invalid Email", 0, 254);

    private final String name, pattern, description;
    private final int minLen, maxLen;

    private boolean isTooShort(String str) {
        return str.length() < minLen;
    }

    private boolean isTooLong(String str) {
        return str.length() > maxLen;
    }

    private boolean isFormatValid(String str) {
        return Pattern.matches(pattern, str);
    }

    public boolean isValidInput(String str) {
        return !isTooShort(str) && !isTooLong(str) && isFormatValid(str);
    }

    InputPatterns(String name, final String pattern, String description, int minLen, int maxLen) {
        this.name = name;
        this.pattern = pattern;
        this.description = description;
        this.minLen = minLen;
        this.maxLen = maxLen;
    }

    public boolean validateAndDisplay(EditText et) {
        String input = et.getText().toString();
        if (input.length() > 0) {
            if (isTooShort(input)) {
                et.setError(String.format("%s too short, it may be %d-%d characters long.", name, minLen, maxLen));
            } else if (isTooLong(input)) {
                et.setError(String.format("%s too long. it may be %d-%d characters long.", name, minLen, maxLen));
            } else if (!isValidInput(input)) {
                et.setError(description);
            } else {          //No errors found
                return true;
            }
        }
        else {               //String is empty
            return true;
        }
        return false;        //Input is invalid
    }
}