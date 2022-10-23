package monster.giz.EnchantableHorseArmor.enchantments.horse_armor;

import monster.giz.EnchantableHorseArmor.enchantments.HorseArmorEnchantment;

public class HippocampusEnchantment extends HorseArmorEnchantment {

    public HippocampusEnchantment() {
        super(Rarity.UNCOMMON);
    }

    @Override
    public boolean isTreasure() {
        return false;
    }
}
