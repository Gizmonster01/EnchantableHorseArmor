package monster.giz.Overhorsed.mixin;

import monster.giz.Overhorsed.Overhorsed;
import monster.giz.Overhorsed.config.OverhorsedConfig;
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
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ForgingScreen<SmithingScreenHandler> {
    @Shadow @Final private CyclingSlotIcon templateSlotIcon;
    @Shadow @Final private CyclingSlotIcon baseSlotIcon;
    @Shadow @Final private CyclingSlotIcon additionsSlotIcon;
    @Shadow @Final private static Vector3f field_45497;

    @Shadow @Nullable private ArmorStandEntity armorStand;
    @Unique private static Quaternionf initialHorseRotation;
    @Unique private static final float ROTATION_AMOUNT = OverhorsedConfig.getFloat("client.smithing_table.rotation_increment", -0.005f);
    @Unique private static final float HOVER_MODIFIER = OverhorsedConfig.getFloat("client.smithing_table.hover_modifier", 0.5f);

    private static Quaternionf rotationIncrement;
    private static Quaternionf hoverIncrement;

    @Unique private HorseEntity horse;
    @Unique private HorseColor horseColor = HorseColor.BROWN;

    private static final int OFFSET_X = OverhorsedConfig.getInt("client.smithing_table.horse_offset_x", 151);
    private static final int OFFSET_Y = OverhorsedConfig.getInt("client.smithing_table.horse_offset_y", 65);
    private static final int BASE_SIZE = OverhorsedConfig.getInt("client.smithing_table.horse_scale", 25);
    private static final int HOVER_SIZE = OverhorsedConfig.getInt("client.smithing_table.hover_scale", 35);
    private static final int FRAME_COUNT = OverhorsedConfig.getInt("client.smithing_table.frame_count", 7);

    private int horseSize = BASE_SIZE;
    private int frameCount = 0;

    public SmithingScreenMixin(SmithingScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Inject(method = "setup()V", at = @At("TAIL"))
    public void setup(CallbackInfo ci) {
        this.horse = new HorseEntity(EntityType.HORSE, Objects.requireNonNull(armorStand).getEntityWorld());
        this.horse.setVariant(horseColor);
        this.horse.bodyYaw = 210.0F;
        this.horse.setPitch(25.0F);
        initialHorseRotation = new Quaternionf().rotationXYZ(0.43633232F, 0.0F, 3.1415927F);
        rotationIncrement = new Quaternionf().rotateAxis(ROTATION_AMOUNT, 0, 1, 0);
        hoverIncrement = new Quaternionf().rotateAxis(ROTATION_AMOUNT * HOVER_MODIFIER, 0, 1, 0);
    }

    @Inject(method = "equipArmorStand(Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"), cancellable = true)
    private void equipArmorStand(ItemStack stack, CallbackInfo ci) {
        if (this.horse != null && Overhorsed.isHorseArmor(stack)) {
            horse.setArmorTypeFromStack(stack);
        } else {
            ci.cancel();
        }
    }

    @Inject(method = "drawBackground(Lnet/minecraft/client/gui/DrawContext;FII)V", at = @At("HEAD"), cancellable = true)
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        if (horse.isHorseArmor(handler.getSlot(3).getStack())) {
            super.drawBackground(context, delta, mouseX, mouseY);
            this.templateSlotIcon.render(this.handler, context, delta, this.x, this.y);
            this.baseSlotIcon.render(this.handler, context, delta, this.x, this.y);
            this.additionsSlotIcon.render(this.handler, context, delta, this.x, this.y);
            drawHorse(context, mouseX, mouseY);
            ci.cancel();
        }
    }

    @Unique
    private void drawHorse(DrawContext context, int mouseX, int mouseY) {
        boolean isMouseCurrentlyOver = isMouseOverHorse(mouseX, mouseY);
        updateSizeAndFrameCount(isMouseCurrentlyOver);

        InventoryScreen.drawEntity(context, (float)(this.x + OFFSET_X), (float)(this.y + OFFSET_Y), horseSize, field_45497, initialHorseRotation, (Quaternionf)null, this.horse);
        initialHorseRotation = isMouseCurrentlyOver ? initialHorseRotation.mul(hoverIncrement) : initialHorseRotation.mul(rotationIncrement);
    }

    @Unique
    private void updateSizeAndFrameCount(boolean isMouseCurrentlyOver) {
        frameCount = isMouseCurrentlyOver ? Math.min(frameCount + 1, FRAME_COUNT) : Math.max(frameCount - 1, 0);
        horseSize = BASE_SIZE + (int)((HOVER_SIZE - BASE_SIZE) * ((float)frameCount / FRAME_COUNT));
    }

    @Unique
    public boolean isMouseOverHorse(int mouseX, int mouseY) {
        int horseCenterX = this.x + OFFSET_X;
        int horseCenterY = this.y + OFFSET_Y - 20;

        int squareLeft = horseCenterX - 25;
        int squareRight = horseCenterX + 20;
        int squareTop = horseCenterY - 25;
        int squareBottom = horseCenterY + 25;

        return mouseX >= squareLeft && mouseX <= squareRight && mouseY >= squareTop && mouseY <= squareBottom;
    }
}