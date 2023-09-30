package monster.giz.Overhorsed.mixin;

import monster.giz.Overhorsed.access.ArmorTrimAccess;
import monster.giz.Overhorsed.access.HorseArmorItemAccess;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ArmorTrim.class)
public abstract class ArmorTrimMixin implements ArmorTrimAccess {

    @Shadow protected abstract String getMaterialAssetNameFor(ArmorMaterial armorMaterial);
    @Shadow @Final private RegistryEntry<ArmorTrimMaterial> material;

    @Shadow @Final private RegistryEntry<ArmorTrimPattern> pattern;

    // TODO: Pattern support
    public Identifier overhorsed$getHorseTrimModelIdentifier(HorseArmorItem armor) {
        Identifier identifier = (pattern.value().assetId());
        if (((HorseArmorItemAccess) armor).overhorsed$getMaterial() == null) {
            return identifier.withPath((path) -> "trims/models/horse/" + path + "_" + material.value().assetName());
        } else {
            return identifier.withPath((path) -> "trims/models/horse/" + path + "_" + this.getMaterialAssetNameFor(((HorseArmorItemAccess) armor).overhorsed$getMaterial()));
        }
    }
}
