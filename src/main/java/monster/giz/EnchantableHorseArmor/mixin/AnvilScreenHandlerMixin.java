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

import java.util.Iterator;
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

    public void horseArmorHandler(ItemStack item, ItemStack ingredient) {
        this.levelCost.set(1);
        int i = 0;
        int j = 0;
        int k = 0;
        if (item.isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
            this.levelCost.set(0);
        } else {
            ItemStack resultStack = item.copy();
            Map<Enchantment, Integer> map = EnchantmentHelper.get(resultStack);
            j += item.getRepairCost() + (ingredient.isEmpty() ? 0 : ingredient.getRepairCost());
            this.repairItemUsage = 0;
            if (!ingredient.isEmpty()) {
                boolean book = ingredient.isOf(Items.ENCHANTED_BOOK) && !EnchantedBookItem.getEnchantmentNbt(ingredient).isEmpty();
                if (!book && (!resultStack.isOf(ingredient.getItem()))) {
                    this.output.setStack(0, ItemStack.EMPTY);
                    this.levelCost.set(0);
                    return;
                }
                Map<Enchantment, Integer> ingredientEnchants = EnchantmentHelper.get(ingredient);
                boolean bl2 = false;
                boolean bl3 = false;
                Iterator var23 = ingredientEnchants.keySet().iterator();

                label155:
                while(true) {
                    Enchantment enchantment;
                    do {
                        if (!var23.hasNext()) {
                            if (bl3 && !bl2) {
                                this.output.setStack(0, ItemStack.EMPTY);
                                this.levelCost.set(0);
                                return;
                            }
                            break label155;
                        }

                        enchantment = (Enchantment)var23.next();
                    } while(enchantment == null);

                    int q = (Integer)map.getOrDefault(enchantment, 0);
                    int r = (Integer)ingredientEnchants.get(enchantment);
                    r = q == r ? r + 1 : Math.max(r, q);
                    boolean acceptableEnchantment = HorseEnchantments.isAcceptableHorseEnchantment(enchantment);
                    if (this.player.getAbilities().creativeMode || item.isOf(Items.ENCHANTED_BOOK)) {
                        acceptableEnchantment = true;
                    }

                    Iterator ingredIterator = map.keySet().iterator();

                    while(ingredIterator.hasNext()) {
                        Enchantment enchantment2 = (Enchantment)ingredIterator.next();
                        if (enchantment2 != enchantment && !enchantment.canCombine(enchantment2)) {
                            acceptableEnchantment = false;
                            ++i;
                        }
                    }

                    if (!acceptableEnchantment) {
                        bl3 = true;
                    } else {
                        bl2 = true;
                        if (r > enchantment.getMaxLevel()) {
                            r = enchantment.getMaxLevel();
                        }

                        map.put(enchantment, r);
                        int s = 0;
                        switch (enchantment.getRarity()) {
                            case COMMON:
                                s = 1;
                                break;
                            case UNCOMMON:
                                s = 2;
                                break;
                            case RARE:
                                s = 4;
                                break;
                            case VERY_RARE:
                                s = 8;
                        }
                        if (book) {
                                s = Math.max(1, s / 2);
                            }

                            i += s * r;
                            if (item.getCount() > 1) {
                                i = 40;
                            }
                        }
                    }
            }

            if (StringUtils.isBlank(this.newItemName)) {
                if (item.hasCustomName()) {
                    k = 1;
                    i += k;
                    resultStack.removeCustomName();
                }
            } else if (!this.newItemName.equals(item.getName().getString())) {
                k = 1;
                i += k;
                resultStack.setCustomName(Text.literal(this.newItemName));
            }

            this.levelCost.set(j + i);
            if (i <= 0) {
                resultStack = ItemStack.EMPTY;
            }

            if (k == i && k > 0 && this.levelCost.get() >= 40) {
                this.levelCost.set(39);
            }

            if (this.levelCost.get() >= 40 && !this.player.getAbilities().creativeMode) {
                resultStack = ItemStack.EMPTY;
            }

            if (!resultStack.isEmpty()) {
                int t = resultStack.getRepairCost();
                if (!ingredient.isEmpty() && t < ingredient.getRepairCost()) {
                    t = ingredient.getRepairCost();
                }

                if (k != i || k == 0) {
                    t = getNextCost(t);
                }

                resultStack.setRepairCost(t);
                EnchantmentHelper.set(map, resultStack);
            }

            this.output.setStack(0, resultStack);
            this.sendContentUpdates();
        }
    }

    @Inject(method = "updateResult()V", at = @At("HEAD"), cancellable = true)
    public void updateResult(CallbackInfo ci) {
        ItemStack item = this.input.getStack(0);
        ItemStack ingredient = this.input.getStack(1);
        if (item.isIn(EnchantableHorseArmor.HORSE_ARMOR)) {
            horseArmorHandler(item, ingredient);
            ci.cancel();
        }
    }
}
