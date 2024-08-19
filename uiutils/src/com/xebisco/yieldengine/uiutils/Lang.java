package com.xebisco.yieldengine.uiutils;

import java.util.Properties;

public class Lang {
    public final static Properties LANG = new Properties();

    public static String getString(String key) {
        String value = LANG.getProperty(key);
        return value == null ? key : value;
    }
}
