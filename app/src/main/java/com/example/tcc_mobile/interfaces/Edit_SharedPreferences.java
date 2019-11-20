package com.example.tcc_mobile.interfaces;

public final class Edit_SharedPreferences {
    private static final Edit_SharedPreferences EDITOR = new Edit_SharedPreferences();

    public Edit_SharedPreferences() {

    }
    public static Edit_SharedPreferences getInstance(){
        return EDITOR;
    }
}
