package monster.giz.Overhorsed.enchantments.horse_armor;

import monster.giz.Overhorsed.enchantments.OverhorsedEnchantment;

public class LeapingEnchantment extends OverhorsedEnchantment {

    public float jumpStrengthModifier;
    public LeapingEnchantment(int maxLevel, boolean treasure, float factor) {
        super(Rarity.VERY_RARE, maxLevel, treasure);
    }

    public int getMaxLevel() {
        return maxLevel;
    }


}
