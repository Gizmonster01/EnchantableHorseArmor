package monster.giz.Overhorsed.enchantments.horse_armor;

import monster.giz.Overhorsed.enchantments.OverhorsedEnchantment;
import net.minecraft.enchantment.Enchantment;

public class HippocampusEnchantment extends OverhorsedEnchantment {

    public HippocampusEnchantment(int maxLevel, boolean treasure) {
        super(Enchantment.Rarity.UNCOMMON, maxLevel, treasure);
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
}
