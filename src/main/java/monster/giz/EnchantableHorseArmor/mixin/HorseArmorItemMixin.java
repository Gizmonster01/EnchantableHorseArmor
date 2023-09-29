package monster.giz.EnchantableHorseArmor.mixin;


import monster.giz.EnchantableHorseArmor.access.HorseArmorItemAccess;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;


@Mixin(HorseArmorItem.class)
public abstract class HorseArmorItemMixin extends Item implements HorseArmorItemAccess {

    @Unique
    private ArmorMaterial material;

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

    public void enchantableHorseArmor$setMaterial(ArmorMaterial material) { this.material = material; }

    public ArmorMaterial enchantableHorseArmor$getMaterial() { return material; }

}
