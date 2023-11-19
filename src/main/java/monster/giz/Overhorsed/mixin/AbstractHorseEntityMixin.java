package monster.giz.Overhorsed.mixin;

import monster.giz.Overhorsed.enchantments.OverhorsedEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends AnimalEntity implements InventoryChangedListener, RideableInventory, Tameable, JumpingMount, Saddleable {

    protected AbstractHorseEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow protected float jumpStrength;

    @Shadow
    public boolean isAngry() {
        return false;
    }

    @Shadow public abstract boolean hasArmorInSlot();

    @Override
    public Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        float strafeSpeedModifier = 1;
        if (this.hasArmorInSlot()) {
            strafeSpeedModifier = getStrafeSpeed(((HorseEntity) ((Object) this)).getArmorType());
        }
        if (this.isOnGround() && this.jumpStrength == 0.0F && this.isAngry() && !this.jumping) {
            return Vec3d.ZERO;
        } else {
            float f = (controllingPlayer.sidewaysSpeed * 0.5F) * strafeSpeedModifier;
            float g = controllingPlayer.forwardSpeed;
            if (g <= 0.0F) {
                g *= 0.25F;
            }

            return new Vec3d(f, 0.0, g);
        }
    }

    @Unique
    private static float getStrafeSpeed(ItemStack armor) {
        int i = EnchantmentHelper.getLevel(OverhorsedEnchantments.STRAFING, armor);
        return (0.25F + i);
    }
}
