package com.example.utils.data;

public class Tools {
    public static boolean isNotNull(String str){
        if (str==null || str.equals("")){
            return false;
        }
        return true;
    }
}