package monster.giz.EnchantableHorseArmor.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin  {

    /**
     * @author
     * @reason
     */
    /*
    @Overwrite
    public boolean canBeRiddenInWater() {
        LivingEntity instance = (LivingEntity) ((Object) this);
        return HorseEnchantments.hasHorseSwim(instance);
    }

     */

}
