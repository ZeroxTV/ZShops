package de.zeroxtv.zshops.util;

import org.bukkit.enchantments.Enchantment;

/**
 * Created by ZeroxTV
 */
public class CardboardEnchantment {
    private static final long serialVersionUID = 8973856768102665381L;

    private final int id;

    public CardboardEnchantment(Enchantment enchantment) {
        this.id = enchantment.getId();
    }

    public Enchantment unbox() {
        return Enchantment.getById(this.id);
    }
}
