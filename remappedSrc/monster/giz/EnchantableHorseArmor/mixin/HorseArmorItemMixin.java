package monster.giz.EnchantableHorseArmor.mixin;


import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(HorseArmorItem.class)
public abstract class HorseArmorItemMixin extends Item {

    public HorseArmorItemMixin(Item.Settings settings) {
        super(settings);
    }

    @Override
    public boolean isEnchantable(ItemStack item) {
        return !item.hasEnchantments();
    }

    @Override
    public int getEnchantability() {
        return 1;
    }

}
