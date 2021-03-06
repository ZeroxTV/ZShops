package de.zeroxtv.zshops;

import de.zeroxtv.zcore.SQL.MySQL;
import de.zeroxtv.zshops.configuration.ConfigPath;
import de.zeroxtv.zshops.configuration.SQLManager;
import de.zeroxtv.zshops.shops.PlayerShop;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Logger;

public final class ZShops extends JavaPlugin {

    public static Economy economy;
    public static Logger logger;
    public static MySQL mySQL;

    @Override
    public void onEnable() {
        logger = this.getLogger();
        if (!setupEconomy() ) {
            logger.severe("ERROR - Vault initialization failed. Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        ConfigPath.createPath();
        mySQL = new MySQL();
        mySQL.connect("localhost", "root", "Admin123");
        SQLManager.initializeDB();

        try {
            PlayerShop.loadAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        PlayerShop.saveAll();
        mySQL.disconnect();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            logger.severe("ERROR - No Vault found");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            logger.severe("ERROR - No Service Provider found");
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}
