package de.zeroxtv.zshops.configuration;

import de.zeroxtv.zcore.SQL.MySQL;
import de.zeroxtv.zshops.ZShops;

import java.util.ArrayList;

/**
 * Created by ZeroxTV
 */
public class SQLManager {
    public static void initializeDB() {
        MySQL sql = ZShops.mySQL;

        sql.execute("CREATE DATABASE IF NOT EXISTS ZShops");

        sql.switchDatabase("ZShops");

        sql.execute("CREATE TABLE IF NOT EXISTS Shops (" +
                "Name VARCHAR(100) PRIMARY KEY," +
                "Owner VARCHAR(100))");
    }
}
