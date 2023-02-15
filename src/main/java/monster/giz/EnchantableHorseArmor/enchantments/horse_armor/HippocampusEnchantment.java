package monster.giz.EnchantableHorseArmor.enchantments.horse_armor;

import monster.giz.EnchantableHorseArmor.enchantments.HorseArmorEnchantment;
import net.minecraft.enchantment.Enchantment;

public class HippocampusEnchantment extends HorseArmorEnchantment {

    public HippocampusEnchantment() {
        super(Enchantment.Rarity.UNCOMMON);
    }

    @Override
    public boolean isTreasure() {
        return false;
    }
}
