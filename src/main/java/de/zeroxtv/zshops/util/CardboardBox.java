package de.zeroxtv.zshops.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZeroxTV
 */
public class CardboardBox implements Serializable{
    private static final long serialVersionUID = 729890133797629669L;

    private final int type, amount;
    private final short damage;

    private final Map<CardboardEnchantment, Integer> enchants;

    public CardboardBox(ItemStack item) {
        this.type = item.getTypeId();
        this.amount = item.getAmount();
        this.damage = item.getDurability();

        this.enchants = new HashMap<CardboardEnchantment, Integer>();

        Map<Enchantment, Integer> enchantments = item.getEnchantments();

        for(Enchantment enchantment : enchantments.keySet()) {
            this.enchants.put(new CardboardEnchantment(enchantment), enchantments.get(enchantment));
        }
    }

    public ItemStack unbox() {
        ItemStack item = new ItemStack(type, amount, damage);

        HashMap<Enchantment, Integer> map = new HashMap<Enchantment, Integer>();

        for(CardboardEnchantment cEnchantment : enchants.keySet()) {
            map.put(cEnchantment.unbox(), this.enchants.get(cEnchantment));
        }

        item.addUnsafeEnchantments(map);

        return item;
    }

    public String encode() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject(this);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static CardboardBox decode(String encoded) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(encoded);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o  = ois.readObject();
        ois.close();
        return (CardboardBox) o;
    }
}
