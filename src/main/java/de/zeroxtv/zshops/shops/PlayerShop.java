package de.zeroxtv.zshops.shops;

import de.zeroxtv.zcore.SQL.MySQL;
import de.zeroxtv.zmenu.Menu;
import de.zeroxtv.zmenu.clickables.ClickButton;
import de.zeroxtv.zshops.ZShops;
import de.zeroxtv.zshops.offers.ItemOffer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by ZeroxTV
 */
public class PlayerShop implements Shop {
    private static ArrayList<PlayerShop> shops = new ArrayList<>();

    private UUID owner;
    private String name;
    private ArrayList<ItemOffer> offers = new ArrayList<>();

    public PlayerShop(UUID owner, String name) {
        this.owner = owner;
        this.name = name;
        saveToSQL();
    }

    public void open(Player player) {
        Menu menu = new Menu(name, 6);
        for (int i = 0; i < 54; i++) {
            if (i < offers.size()) {
                menu.setItem(i, new ClickButton(offers.get(i).getDisplayItem(),
                        inventoryClickEvent -> getItemFromDisplay(inventoryClickEvent.getCurrentItem()).
                                buy(inventoryClickEvent.getWhoClicked().getUniqueId())));
            }
        }
        menu.open(player);
    }

    public ItemOffer getItemFromDisplay(ItemStack itemStack) {
        for (ItemOffer offer : offers) {
            if (itemStack.equals(offer.getDisplayItem())) {
                return offer;
            }
        }
        return null;
    }

    public void addOffer(ItemOffer offer) {
        if (offers.size()<54) {
            offers.add(offer);
        }
    }

    public void setOffers(ArrayList<ItemOffer> offers) {
        this.offers = offers;
    }

    public void saveToSQL() {
        MySQL sql = ZShops.mySQL;
        ArrayList<String> tasks = new ArrayList<>();
        tasks.add(String.format("REPLACE INTO Shops (Name,Owner) VALUES ('%s','%s')", name, owner));
        tasks.add(String.format("CREATE TABLE IF NOT EXISTS %s (Id VARCHAR(3) PRIMARY KEY, Item VARCHAR(200))", name));
        sql.executeArray(tasks);
        for (ItemOffer offer : offers) {
            offer.save();
        }
    }

    public static PlayerShop loadFromSQL(ResultSet resultSet) throws SQLException {
        UUID owner = UUID.fromString(resultSet.getString("Owner"));
        PlayerShop shop = new PlayerShop(owner, resultSet.getString("Name"));
        //shop.setOffers(ItemOffer.load(shop));
        return shop;
    }

    public static PlayerShop getByName(String name) {
        for (PlayerShop shop : shops) {
            if (shop.name.equalsIgnoreCase(name)) {
                return shop;
            }
        }
        return null;
    }

    public static void loadAll() throws SQLException {
        ResultSet rs = ZShops.mySQL.executeQuery("SELECT * FROM Shops");
        while (rs.next()) {
            shops.add(loadFromSQL(rs));
        }
    }

    public static void saveAll() {
        for (PlayerShop shop : shops) {
            shop.saveToSQL();
        }
    }

    @Override
    public UUID getOwner() {
        return owner;
    }

    @Override
    public String getName() {
        return name;
    }
}
