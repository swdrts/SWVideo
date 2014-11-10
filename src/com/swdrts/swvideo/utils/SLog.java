package com.swdrts.swvideo.utils;

import android.util.Log;

import com.swdrts.swvideo.Config;

public class SLog {
    
    private static final String TAG = "SWVideo";
    
    public static void debug(String msg) {
        if (Config.DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void error(String msg) {
        if (Config.DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void info(String msg) {
        if (Config.DEBUG) {
            Log.i(TAG, msg);
        }
    }

    public static void waring(String msg) {
        if (Config.DEBUG) {
            Log.w(TAG, msg);
        }
    }
    
    public static void verbose(String msg) {
        if (Config.DEBUG)
            Log.v(TAG, msg);
    }
    
    public static void debug(String TAG, String strLog) {
        if (Config.DEBUG) {
            Log.d(TAG, strLog);
        }
    }

    public static void waring(String TAG, String strLog) {
        if (Config.DEBUG)
            Log.w(TAG, strLog);
    }

    public static void error(String TAG, String strLog) {
        if (Config.DEBUG)
            Log.e(TAG, strLog);
    }

    public static void info(String TAG, String strLog) {
        if (Config.DEBUG)
            Log.i(TAG, strLog);
    }

    public static void verbose(String TAG, String strLog) {
        if (Config.DEBUG)
            Log.v(TAG, strLog);
    }

}
