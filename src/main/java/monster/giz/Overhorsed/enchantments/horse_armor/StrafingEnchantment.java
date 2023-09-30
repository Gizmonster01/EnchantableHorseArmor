package monster.giz.Overhorsed.enchantments.horse_armor;

import monster.giz.Overhorsed.enchantments.HorseArmorEnchantment;
import net.minecraft.enchantment.Enchantment;

public class StrafingEnchantment extends HorseArmorEnchantment {
    public StrafingEnchantment() {
        super(Enchantment.Rarity.UNCOMMON);
    }

    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }
}
