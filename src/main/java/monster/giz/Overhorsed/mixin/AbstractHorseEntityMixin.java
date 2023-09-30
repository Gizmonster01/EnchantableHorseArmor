package monster.giz.Overhorsed.mixin;

import monster.giz.Overhorsed.enchantments.HorseEnchantments;
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

    @Shadow boolean isAngry() {
        return false;
    }

    @Shadow public abstract boolean hasArmorInSlot();

    @Override
    public Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        float strafeSpeed = 0.5F;
        if (this.hasArmorInSlot()) {
            strafeSpeed = getStrafeSpeed(((HorseEntity) ((Object) this)).getArmorType());
        }
        if (this.isOnGround() && this.jumpStrength == 0.0F && this.isAngry() && !this.jumping) {
            return Vec3d.ZERO;
        } else {
            float f = controllingPlayer.sidewaysSpeed * strafeSpeed;
            float g = controllingPlayer.forwardSpeed;
            if (g <= 0.0F) {
                g *= 0.25F;
            }

            return new Vec3d((double)f, 0.0, (double)g);
        }
    }

    @Unique
    private static float getStrafeSpeed(ItemStack armor) {
        int i = EnchantmentHelper.getLevel(HorseEnchantments.STRAFING, armor);
        return (0.5F + i);
    }
}
