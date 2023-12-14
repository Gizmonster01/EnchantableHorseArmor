package monster.giz.Overhorsed.mixin;

import monster.giz.Overhorsed.util.OHLogger;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow public abstract DynamicRegistryManager.Immutable getRegistryManager();

    @Inject(at = @At("HEAD"), method = "loadWorld")
    private void init(CallbackInfo info) {
        Iterator<ArmorTrimMaterial> iterator = getRegistryManager().get(RegistryKeys.TRIM_MATERIAL).iterator();

        OHLogger.log("Trim Materials List:");
        while (iterator.hasNext()) {
            ArmorTrimMaterial material = iterator.next();
            OHLogger.log("-" + material.assetName());
            for (String string : material.overrideArmorMaterials().values()) {
                OHLogger.log("-" + string);
            }
        }
    }
}
