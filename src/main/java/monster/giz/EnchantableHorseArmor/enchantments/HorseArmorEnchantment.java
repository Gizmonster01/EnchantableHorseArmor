package monster.giz.EnchantableHorseArmor.enchantments;

import monster.giz.EnchantableHorseArmor.EnchantableHorseArmor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class HorseArmorEnchantment extends Enchantment {

    protected HorseArmorEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    protected HorseArmorEnchantment(Rarity weight) {
        super(weight, EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getRegistryEntry().isIn(EnchantableHorseArmor.HORSE_ARMOR);
    }

}
