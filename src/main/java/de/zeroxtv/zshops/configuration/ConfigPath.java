package de.zeroxtv.zshops.configuration;

import java.io.File;

/**
 * Created by ZeroxTV
 */
public class ConfigPath {
    public static void createPath() {
        File dir = new File("plugins/ZShops");
        if (!dir.exists()) dir.mkdirs();
    }
}
