package monster.giz.Overhorsed.mixin;

import monster.giz.Overhorsed.Overhorsed;
import monster.giz.Overhorsed.enchantments.OverhorsedEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static net.minecraft.screen.AnvilScreenHandler.getNextCost;


@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler{

    @Shadow
    private String newItemName;
    @Shadow @Final
    private Property levelCost;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Unique
    public void horseArmorHandler(ItemStack item, ItemStack ingredient) {
        this.levelCost.set(1);
        int totalCost = 0;
        boolean canApplyEnchantment = true;

        if (item.isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
            this.levelCost.set(0);
            return;
        }

        ItemStack resultStack = item.copy();
        Map<Enchantment, Integer> itemEnchantments = EnchantmentHelper.get(resultStack);

        totalCost += item.getRepairCost() + (ingredient.isEmpty() ? 0 : ingredient.getRepairCost());

        if (!ingredient.isEmpty() && !(ingredient.isOf(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantmentNbt(ingredient).isEmpty()) && !resultStack.isOf(ingredient.getItem())) {
            this.output.setStack(0, ItemStack.EMPTY);
            this.levelCost.set(0);
            return;
        }

        Map<Enchantment, Integer> ingredientEnchants = EnchantmentHelper.get(ingredient);

        for (Enchantment enchantment : ingredientEnchants.keySet()) {
            if (enchantment == null) continue;

            int existingLevel = itemEnchantments.getOrDefault(enchantment, 0);
            int ingredientLevel = ingredientEnchants.get(enchantment);

            int newLevel = (existingLevel == ingredientLevel) ? existingLevel + 1 : Math.max(existingLevel, ingredientLevel);

            if (!OverhorsedEnchantments.isAcceptableHorseEnchantment(enchantment) && !(this.player.getAbilities().creativeMode || item.isOf(Items.ENCHANTED_BOOK))) {
                canApplyEnchantment = false;
            }

            for (Enchantment existingEnchantment : itemEnchantments.keySet()) {
                if (existingEnchantment != enchantment && !enchantment.canCombine(existingEnchantment)) {
                    canApplyEnchantment = false;
                    break;
                }
            }

            if (canApplyEnchantment) {
                newLevel = Math.min(newLevel, enchantment.getMaxLevel());
                itemEnchantments.put(enchantment, newLevel);

                int enchantmentCost = getEnchantmentCost(enchantment);

                if (item.getCount() > 1) {
                    enchantmentCost = 40;
                }

                totalCost += enchantmentCost * newLevel;
            }
        }

        if (StringUtils.isBlank(this.newItemName)) {
            if (item.hasCustomName()) {
                totalCost++;
                resultStack.removeCustomName();
            }
        } else if (!this.newItemName.equals(item.getName().getString())) {
            totalCost++;
            resultStack.setCustomName(Text.literal(this.newItemName));
        }

        this.levelCost.set(totalCost);

        if (totalCost <= 0) {
            resultStack = ItemStack.EMPTY;
        }

        if (totalCost >= 40 && !this.player.getAbilities().creativeMode) {
            resultStack = ItemStack.EMPTY;
        }

        if (!resultStack.isEmpty()) {
            int repairCost = resultStack.getRepairCost();
            if (!ingredient.isEmpty() && repairCost < ingredient.getRepairCost()) {
                repairCost = ingredient.getRepairCost();
            }

            if (totalCost == 0) {
                repairCost = getNextCost(repairCost);
            }

            resultStack.setRepairCost(repairCost);
            EnchantmentHelper.set(itemEnchantments, resultStack);
        }

        this.output.setStack(0, resultStack);
        this.sendContentUpdates();
    }

    @Unique
    private int getEnchantmentCost(Enchantment enchantment) {
        return switch (enchantment.getRarity()) {
            case UNCOMMON -> 2;
            case RARE -> 4;
            case VERY_RARE -> 8;
            default -> 1;
        };
    }

    @Inject(method = "updateResult()V", at = @At("HEAD"), cancellable = true)
    public void updateResult(CallbackInfo ci) {
        ItemStack item = this.input.getStack(0);
        ItemStack ingredient = this.input.getStack(1);
        if (Overhorsed.isHorseArmor(item)) {
            horseArmorHandler(item, ingredient);
            ci.cancel();
        }
    }
}
