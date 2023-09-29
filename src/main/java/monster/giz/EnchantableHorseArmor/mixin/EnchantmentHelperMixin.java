package monster.giz.EnchantableHorseArmor.mixin;


import monster.giz.EnchantableHorseArmor.EnchantableHorseArmor;
import monster.giz.EnchantableHorseArmor.enchantments.HorseEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {


    @Inject(method = "getPossibleEntries(ILnet/minecraft/item/ItemStack;Z)Ljava/util/List;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void enchantablehorsearmor$getPossibleEntries(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        if (EnchantableHorseArmor.isHorseArmor(stack) && !stack.isOf(Items.BOOK)) {
            cir.setReturnValue(HorseEnchantments.getPossibleHorseEntries(power, treasureAllowed));
        }
    }

    @Inject(
            method = "getEquipmentLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/entity/LivingEntity;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void enchantablehorsearmor$getEquipmentLevel(Enchantment enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (entity instanceof HorseEntity horse) {
            int i = EnchantmentHelper.getLevel(enchantment, horse.getArmorType());
            if (enchantment == Enchantments.FROST_WALKER) {
                if (i > 0) {
                    cir.setReturnValue(i + 1);
                } else {
                    cir.setReturnValue(0);
                }
            }
            cir.setReturnValue(i);
        }
    }
}
