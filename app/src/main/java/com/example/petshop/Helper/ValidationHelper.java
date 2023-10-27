package com.example.petshop.Helper;

import android.text.TextUtils;

public class ValidationHelper {
    public static boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$");
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() >= 10 && phoneNumber.length() <= 12;
    }

    public static boolean isNotEmpty(String text) {
        return !TextUtils.isEmpty(text);
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
}

