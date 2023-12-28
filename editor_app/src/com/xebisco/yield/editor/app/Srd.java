package com.xebisco.yield.editor.app;

import java.net.*;
import java.util.Properties;

public class Srd {
    public static final Properties LANG = new Properties();
    public static final String VERSION = "INFDEV", TITLE = "Yield Editor " + VERSION;
    public static String yieldEngineURL = "https://github.com/yield-proj/YieldEngine/releases/latest/download/YieldEngine.jar";
    public static ClassLoader yieldEngineClassLoader;

    static {
        try {
            yieldEngineClassLoader = new URLClassLoader(new URL[]{new URI(yieldEngineURL).toURL()});
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
