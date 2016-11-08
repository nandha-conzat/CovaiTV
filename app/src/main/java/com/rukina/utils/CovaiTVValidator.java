package com.rukina.utils;

/**
 * Created by Cube Reach 06 on 07-11-2016.
 */

public class CovaiTVValidator {

    public static boolean checkNullString(String value) {
        if (value == null)
            return false;
        else
            return value.trim().length() > 0;
    }

    public static boolean checkSpinnerString(String value) {
        if ((value == "Please Select")||(value.equalsIgnoreCase("Please Select")))
            return false;
        else
            return value.trim().length() > 0;
    }

    public static boolean withinPermittedLength(String password){
        return (password.length() > 6) && (password.length() <= 200);
    }

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
