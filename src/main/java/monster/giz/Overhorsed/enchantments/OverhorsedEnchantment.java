package monster.giz.Overhorsed.enchantments;

import monster.giz.Overhorsed.Overhorsed;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;


public class OverhorsedEnchantment extends Enchantment {

    public int maxLevel;
    public boolean treasure;

    protected OverhorsedEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    protected OverhorsedEnchantment(Rarity weight, int maxLevel, boolean treasure) {
        super(weight, EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
        this.maxLevel = maxLevel;
        this.treasure = treasure;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return Overhorsed.isHorseArmor(stack);
    }

}
