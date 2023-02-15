package monster.giz.EnchantableHorseArmor.mixin;

import monster.giz.EnchantableHorseArmor.enchantments.HorseEnchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin  {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean canBeRiddenInWater() {
        LivingEntity instance = (LivingEntity) ((Object) this);
        return HorseEnchantments.hasHorseSwim(instance);
    }
}
