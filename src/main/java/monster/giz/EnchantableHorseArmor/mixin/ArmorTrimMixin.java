package monster.giz.EnchantableHorseArmor.mixin;

import monster.giz.EnchantableHorseArmor.access.ArmorTrimAccess;
import monster.giz.EnchantableHorseArmor.access.HorseArmorItemAccess;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ArmorTrim.class)
public abstract class ArmorTrimMixin implements ArmorTrimAccess {

    @Shadow protected abstract String getMaterialAssetNameFor(ArmorMaterial armorMaterial);
    @Shadow @Final private RegistryEntry<ArmorTrimMaterial> material;

    // TODO: Pattern support
    public String getHorseMaterialAssetName(HorseArmorItem armor) {
        if (((HorseArmorItemAccess) armor).getMaterial() == null) {
            return "test_" + this.material.value();
        } else {
            return "test_" + this.getMaterialAssetNameFor(((HorseArmorItemAccess) armor).getMaterial());
        }
    }
}
