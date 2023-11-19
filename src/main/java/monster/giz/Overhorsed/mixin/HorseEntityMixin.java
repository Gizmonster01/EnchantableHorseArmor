package monster.giz.Overhorsed.mixin;

import monster.giz.Overhorsed.enchantments.OverhorsedEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HorseEntity.class)
public abstract class HorseEntityMixin extends AbstractHorseEntity {

    @Shadow public abstract ItemStack getArmorType();

    protected HorseEntityMixin(EntityType<? extends AbstractHorseEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public double getJumpStrength() {
        int l = EnchantmentHelper.getLevel(OverhorsedEnchantments.LEAPING, this.getArmorType());
        double multiplier = 1;
        if (l > 0) {
            multiplier = 1 + (0.25 * l);
        }
        return (this.getAttributeValue(EntityAttributes.HORSE_JUMP_STRENGTH) * multiplier);
    }

}
