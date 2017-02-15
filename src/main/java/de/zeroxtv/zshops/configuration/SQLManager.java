package de.zeroxtv.zshops.configuration;

import de.zeroxtv.zshops.ZShops;

import java.util.ArrayList;

/**
 * Created by ZeroxTV
 */
public class SQLManager {
    public static void initializeDB() {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("CREATE TABLE IF NOT EXISTS Shops (" +
                "Owner VARCHAR(100), " +
                "Name VARCHAR(100) PRIMARY KEY)");
        ZShops.sqlLibrary.executeArray(commands);
    }
}
