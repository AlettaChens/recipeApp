package com.example.graduate.findingcooking.web;

import android.os.Environment;

public class Constant {
    public static final String PATH;
    public static final String CODE_SUCCESS = "200";
    public static final String BASEURL = "http://123.207.117.247:8080/food-0.0.1-SNAPSHOT/";
    //public static final String BASEURL = "http://192.168.43.83:8080";
    public static final String WEBDIR = "/log_web";

    static {
        PATH = Environment.getExternalStorageDirectory().getPath() + "/recipe/phone";
    }
}
