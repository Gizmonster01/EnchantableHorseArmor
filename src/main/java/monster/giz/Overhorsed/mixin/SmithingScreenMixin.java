package monster.giz.Overhorsed.mixin;

import monster.giz.Overhorsed.Overhorsed;
import monster.giz.Overhorsed.util.OHLogger;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CyclingSlotIcon;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.HorseColor;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ForgingScreen<SmithingScreenHandler> {

    @Shadow @Final
    private CyclingSlotIcon templateSlotIcon;
    @Shadow @Final
    private CyclingSlotIcon baseSlotIcon;
    @Shadow @Final
    private CyclingSlotIcon additionsSlotIcon;
    @Shadow @Final
    private static Vector3f field_45497;

    @Shadow @Final
    private static Quaternionf ARMOR_STAND_ROTATION;

    @Unique
    private ArmorStandEntity armorStand;
    private HorseEntity horse;

    public SmithingScreenMixin(SmithingScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Inject(method = "setup()V",at = @At("TAIL"))
    public void setup(CallbackInfo ci) {
        this.horse = new HorseEntity(EntityType.HORSE, this.client.world);
        this.horse.setVariant(HorseColor.BROWN);
        this.horse.bodyYaw = 210.0F;
        this.horse.setPitch(25.0F);
        OHLogger.log("Horse initialized?");
    }

    @Inject(method = "equipArmorStand(Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"), cancellable = true)
    private void equipArmorStand(ItemStack stack, CallbackInfo ci) {
        if (this.horse != null) {
            if (!(Overhorsed.isHorseArmor(stack))) {
                ci.cancel();
            }
            horse.setArmorTypeFromStack(stack);
        }
    }

    @Inject(method = "drawBackground(Lnet/minecraft/client/gui/DrawContext;FII)V", at = @At("HEAD"), cancellable = true)
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (horse.isHorseArmor(handler.getSlot(3).getStack())) {
            super.drawBackground(context, delta, mouseX, mouseY);
            this.templateSlotIcon.render(this.handler, context, delta, this.x, this.y);
            this.baseSlotIcon.render(this.handler, context, delta, this.x, this.y);
            this.additionsSlotIcon.render(this.handler, context, delta, this.x, this.y);
            InventoryScreen.drawEntity(context, (float)(this.x + 151), (float)(this.y + 65), 25, field_45497, ARMOR_STAND_ROTATION, (Quaternionf)null, this.horse);
            ci.cancel();
        }
    }

}
