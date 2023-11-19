package monster.giz.Overhorsed.enchantments.horse_armor;

import monster.giz.Overhorsed.enchantments.OverhorsedEnchantment;
import net.minecraft.enchantment.Enchantment;

public class StrafingEnchantment extends OverhorsedEnchantment {

    public float speedModifier;
    public StrafingEnchantment(int maxLevel, boolean treasure, float factor) {
        super(Enchantment.Rarity.UNCOMMON, maxLevel, treasure);
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }


}
