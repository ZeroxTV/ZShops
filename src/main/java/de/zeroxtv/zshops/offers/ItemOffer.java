package de.zeroxtv.zshops.offers;

import de.zeroxtv.zcore.SQL.MySQL;
import de.zeroxtv.zshops.ZShops;
import de.zeroxtv.zshops.shops.Shop;
import de.zeroxtv.zshops.util.CardboardBox;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by ZeroxTV
 */
public class ItemOffer implements Offer {
    private ItemStack item;
    private int stock = 0;
    private double price = 0;
    private Shop shop;

    public ItemOffer(ItemStack item, Shop shop) {
        this.item = item;
        this.shop = shop;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemStack getDisplayItem() {
        ItemStack display = new ItemStack(item);
        ItemMeta meta = display.getItemMeta();
        meta.getLore().addAll(Arrays.asList("Preis: " + price, "Auf Lager: " + stock, "Linksklick: 1 kaufen", "Shift + Linksklick: 64 kaufen"));
        display.setItemMeta(meta);
        return display;
    }

    public boolean buy(UUID uuid) {
        Economy eco = ZShops.economy;
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        OfflinePlayer owner = Bukkit.getOfflinePlayer(shop.getOwner());
        if (eco.has(player, price)) {
            eco.depositPlayer(owner, price);
            eco.withdrawPlayer(player, price);
            return true;
        }
        return false;
    }

    @Override
    public void save() {
        try {
            MySQL sql = ZShops.mySQL;
            CardboardBox cbb = new CardboardBox(item);
            String serialized = cbb.encode();
            sql.execute(String.format("REPLACE INTO %s (Item,Price,Stock) VALUES ('%s','%d','%d')", shop.getName(), serialized, price, stock));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ItemOffer> load(Shop shop) {
        try {
            MySQL sql = ZShops.mySQL;
            ResultSet rs = sql.executeQuery(String.format("SELECT * FROM %s", shop.getName()));
            ArrayList<ItemOffer> items = new ArrayList<>();
            while (rs.next()) {
                ItemStack itemStack = CardboardBox.decode(rs.getString("Item")).unbox();
                ItemOffer itemOffer = new ItemOffer(itemStack, shop);
                itemOffer.setPrice(rs.getDouble("Price"));
                itemOffer.setStock(rs.getInt("Stock"));
                items.add(itemOffer);
            }
            return items;
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
