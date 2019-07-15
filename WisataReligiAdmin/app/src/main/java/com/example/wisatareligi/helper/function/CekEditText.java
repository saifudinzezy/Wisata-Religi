package com.example.wisatareligi.helper.function;

import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.widget.EditText;

public class CekEditText {
    public static boolean editText(EditText input){
        boolean value = true;
        if (TextUtils.isEmpty(input.getText().toString())){
            input.setError("Tidak boleh kosong");
            value = false;
        }
        return value;
    }

    public static boolean editText(TextInputEditText input){
        boolean value = false;
        if (!TextUtils.isEmpty(input.getText().toString())){
            value = true;
        }
        return value;
    }
}