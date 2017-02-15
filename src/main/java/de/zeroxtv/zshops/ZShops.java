package de.zeroxtv.zshops;

import de.zeroxtv.zshops.configuration.ConfigPath;
import de.zeroxtv.zshops.configuration.SQLLibrary;
import de.zeroxtv.zshops.configuration.SQLManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ZShops extends JavaPlugin {

    public static Economy economy;
    public static Logger logger;
    public static SQLLibrary sqlLibrary;

    @Override
    public void onEnable() {
        logger = this.getLogger();
        if (!setupEconomy() ) {
            logger.severe("ERROR - No Vault Found. Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        ConfigPath.createPath();
        sqlLibrary = new SQLLibrary();
        sqlLibrary.connect();
        SQLManager.initializeDB();
    }

    @Override
    public void onDisable() {
        sqlLibrary.disconnect();
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            logger.severe("No Vault found");
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            logger.severe("No Service Provider found");
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}
