package com.example.employeesassignment.addemployee;

import android.graphics.drawable.Drawable;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

enum InputPatterns {
    NAME("First Name", "^[a-zA-Z ,.'-]+$", "Invalid full name", 1, 32),
    PHONE("Phone Number", "^[\\+]?[(]?[0-9]{3}[)]?[- \\.]?[0-9]{3}[- \\.]?[0-9]{4,6}$", "Invalid Phone Number", 0, 32),
    HOURLY_PAY("Hourly Pay", "^[0-9]{1,6}$", "Invalid input, Enter digits only", 1, 4),
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

    public boolean validateAndDisplay(EditText et) {
        String input = et.getText().toString();
        TextInputLayout til = (TextInputLayout) et.getParent().getParent();
        if (input.length() > 0) {
            if (isTooShort(input)) {
                til.setError(String.format("%s too short, it may be %d-%d characters long.", name, minLen, maxLen));
            } else if (isTooLong(input)) {
                til.setError(String.format("%s too long. it may be %d-%d characters long.", name, minLen, maxLen));
            } else if (!isValidInput(input)) {
                til.setError(description);
            } else {          //No errors found
                til.setError(null);
                return true;
            }
        }
        else {               //String is empty
            til.setError(null);
            return true;
        }
        return false;        //Input is invalid
    }
}
