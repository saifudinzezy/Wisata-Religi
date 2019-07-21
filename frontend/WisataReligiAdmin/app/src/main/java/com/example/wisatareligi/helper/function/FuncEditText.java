package com.example.wisatareligi.helper.function;

import android.widget.EditText;

public class FuncEditText {
    public static String getEditText(EditText editText) {
        return editText.getText().toString();
    }

    public static void setEditText(EditText editText, String value) {
        editText.setText(value);
    }
}
