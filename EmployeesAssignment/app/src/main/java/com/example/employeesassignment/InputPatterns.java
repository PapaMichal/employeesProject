package com.example.employeesassignment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public enum InputPatterns {
    USERNAME("Username", "^[a-zA-Z0-9_]{1,16}$", "Username may contain letters, numbers and underscores.", 1, 16),
    PASSWORD("Password", "^(?=.*[a-z])(?=.*[0-9])(?=.*[A-Z])[a-zA-Z0-9]{8,20}$", "Password must contain a lowercase letter, an uppercase letter, and a number", 8, 20),
    EMAIL("Email", "^(?=.{0,254}$)[\\w]+@[\\w]+(\\.[-\\w]+)*$", "Invalid Email", 0, 254),
    NAME("Full Name", "^[a-zA-Z ,.'-]+$", "Invalid full name", 1, 32),
    HOURLY_PAY("Hourly Pay", "^[0-9]{1,4}$", "Invalid input, Enter digits only", 1, 4),
    EDUCATION("Education", "^[#.0-9a-zA-Z ,-]+$", "Invalid input, special symbols are not allowed", 1, 64),
    ADDRESS("Address", "^[#.0-9a-zA-Z ,-]+$", "Invalid input, special symbols are not allowed", 1, 64);

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

    private String getErrorMessage(String input) {
        if (input.length() > 0) {
            if (isTooShort(input)) {
                return String.format("%s too short, it may be %d-%d characters long.", name, minLen, maxLen);
            } else if (isTooLong(input)) {
                return String.format("%s too long. it may be %d-%d characters long.", name, minLen, maxLen);
            } else if (!isValidInput(input)) {
                return description;
            } else {          //No errors found
                return null;
            }
        }
        else {               //String is empty
            return null;
        }
    }

    public boolean validateAndDisplay(TextInputLayout textInputLayout) {
        String input = textInputLayout.getEditText().getText().toString();
        String errorMessage = getErrorMessage(input);
        textInputLayout.setError(errorMessage);
        return errorMessage == null;
    }

    public boolean validateAndDisplay(EditText editText) {
        String input = editText.getText().toString();
        String errorMessage = getErrorMessage(input);
        editText.setError(errorMessage);
        return errorMessage == null;
    }
}