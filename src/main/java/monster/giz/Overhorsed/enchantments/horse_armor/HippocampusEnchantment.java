package monster.giz.Overhorsed.enchantments.horse_armor;

import monster.giz.Overhorsed.enchantments.HorseArmorEnchantment;
import net.minecraft.enchantment.Enchantment;

public class HippocampusEnchantment extends HorseArmorEnchantment {

    public HippocampusEnchantment() {
        super(Enchantment.Rarity.UNCOMMON);
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
}
