package monster.giz.EnchantableHorseArmor.mixin;

import net.minecraft.client.render.model.BakedModelManager;

public interface HorseArmorFeatureAccess {
    void defineAtlas(BakedModelManager bakery);
}
