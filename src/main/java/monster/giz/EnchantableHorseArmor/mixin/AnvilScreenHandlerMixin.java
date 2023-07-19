package monster.giz.EnchantableHorseArmor.mixin;

import monster.giz.EnchantableHorseArmor.EnchantableHorseArmor;
import monster.giz.EnchantableHorseArmor.enchantments.HorseEnchantments;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static net.minecraft.screen.AnvilScreenHandler.getNextCost;


@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler{

    @Shadow
    private int repairItemUsage;
    @Shadow
    private String newItemName;
    @Shadow @Final
    private Property levelCost;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    public void horseArmorAnvilHandler(ItemStack item, ItemStack ingredient) {
        this.levelCost.set(1);
        int totalCost = 0;
        int enchantabilitySum = 0;

        if (item.isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
            this.levelCost.set(0);
            return;
        }

        ItemStack resultStack = item.copy();
        Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.get(resultStack);
        int itemRepairCost = item.getRepairCost();
        int ingredientRepairCost = ingredient.isEmpty() ? 0 : ingredient.getRepairCost();
        totalCost += itemRepairCost + ingredientRepairCost;
        this.repairItemUsage = 0;

        if (!ingredient.isEmpty()) {
            boolean isBook = ingredient.isOf(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantmentNbt(ingredient).isEmpty();

            if (!isBook && (!resultStack.isOf(ingredient.getItem()))) {
                this.output.setStack(0, ItemStack.EMPTY);
                this.levelCost.set(0);
                return;
            }

            Map<Enchantment, Integer> ingredientEnchantments = EnchantmentHelper.get(ingredient);

            for (Enchantment enchantment : ingredientEnchantments.keySet()) {
                if (enchantment == null) {
                    continue;
                }

                int existingLevel = existingEnchantments.getOrDefault(enchantment, 0);
                int ingredientLevel = ingredientEnchantments.get(enchantment);
                ingredientLevel = existingLevel == ingredientLevel ? ingredientLevel + 1 : Math.max(ingredientLevel, existingLevel);

                boolean isAcceptableEnchantment = HorseEnchantments.isAcceptableHorseEnchantment(enchantment);

                if (this.player.getAbilities().creativeMode || item.isOf(Items.ENCHANTED_BOOK)) {
                    isAcceptableEnchantment = true;
                }

                for (Enchantment existingEnchantment : existingEnchantments.keySet()) {
                    if (existingEnchantment != enchantment && !enchantment.canCombine(existingEnchantment)) {
                        isAcceptableEnchantment = false;
                        break;
                    }
                }

                if (!isAcceptableEnchantment) {

                    if (ingredientLevel > enchantment.getMaxLevel()) {
                        ingredientLevel = enchantment.getMaxLevel();
                    }

                    existingEnchantments.put(enchantment, ingredientLevel);
                    int enchantability = getEnchantabilityWeight(enchantment);

                    switch (enchantment.getRarity()) {
                        case COMMON -> enchantabilitySum += enchantability;
                        case UNCOMMON -> enchantabilitySum += enchantability * 2;
                        case RARE -> enchantabilitySum += enchantability * 4;
                        case VERY_RARE -> enchantabilitySum += enchantability * 8;
                    }

                    if (isBook) {
                        enchantabilitySum = Math.max(1, enchantabilitySum / 2);
                    }

                    totalCost += enchantabilitySum * ingredientLevel;

                    if (item.getCount() > 1) {
                        totalCost = 40;
                    }
                }
            }

            if (StringUtils.isBlank(this.newItemName)) {
                if (item.hasCustomName()) {
                    totalCost++;
                    existingEnchantments.remove(Enchantment.Rarity.COMMON);
                    resultStack.removeCustomName();
                }
            } else if (!this.newItemName.equals(item.getName().getString())) {
                totalCost++;
                existingEnchantments.remove(Enchantment.Rarity.COMMON);
                resultStack.setCustomName(Text.literal(this.newItemName));
            }

            this.levelCost.set(totalCost);

            if (totalCost <= 0) {
                resultStack = ItemStack.EMPTY;
            }

            if (totalCost == 1 && this.levelCost.get() >= 40) {
                this.levelCost.set(39);
            }

            if (this.levelCost.get() >= 40 && !this.player.getAbilities().creativeMode) {
                resultStack = ItemStack.EMPTY;
            }

            if (!resultStack.isEmpty()) {
                int repairCost = resultStack.getRepairCost();

                if (!ingredient.isEmpty() && repairCost < ingredient.getRepairCost()) {
                    repairCost = ingredient.getRepairCost();
                }

                if (totalCost != 1) {
                    repairCost = getNextCost(repairCost);
                }

                resultStack.setRepairCost(repairCost);
                EnchantmentHelper.set(existingEnchantments, resultStack);
            }

            this.output.setStack(0, resultStack);
            this.sendContentUpdates();
        }
    }

    private int getEnchantabilityWeight(Enchantment enchantment) {
        return switch (enchantment.getRarity()) {
            case COMMON -> 1;
            case UNCOMMON -> 2;
            case RARE -> 4;
            case VERY_RARE -> 8;
        };
    }

    @Inject(method = "updateResult()V", at = @At("HEAD"), cancellable = true)
    public void updateResult(CallbackInfo ci) {
        ItemStack item = this.input.getStack(0);
        ItemStack ingredient = this.input.getStack(1);
        if (EnchantableHorseArmor.isHorseArmor(item)) {
            horseArmorAnvilHandler(item, ingredient);
            ci.cancel();
        }
    }
}
