package monster.giz.Overhorsed.mixin;


import monster.giz.Overhorsed.Overhorsed;
import monster.giz.Overhorsed.enchantments.OverhorsedEnchantments;
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
    private static void overhorsed$getPossibleEntries(int power, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
        if (Overhorsed.isHorseArmor(stack) && !stack.isOf(Items.BOOK)) {
            cir.setReturnValue(OverhorsedEnchantments.getPossibleHorseEntries(power, treasureAllowed));
        }
    }

    @Inject(
            method = "getEquipmentLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/entity/LivingEntity;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void overhorsed$getEquipmentLevel(Enchantment enchantment, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
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
