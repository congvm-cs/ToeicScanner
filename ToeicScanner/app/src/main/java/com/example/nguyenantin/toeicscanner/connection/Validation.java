package com.example.nguyenantin.toeicscanner.connection;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by nguyenantin on 12/26/17.
 */

public class Validation {

    public static boolean validateMade(String string) {

        if (TextUtils.isEmpty(string)) {

            return false;

        } else {

            return  true;
        }
    }
}
