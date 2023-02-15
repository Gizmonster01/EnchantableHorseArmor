package monster.giz.EnchantableHorseArmor.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HorseArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.DyeableHorseArmorItem;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(HorseArmorFeatureRenderer.class)
public class HorseArmorFeatureRendererMixin extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {
    @Shadow @Final
    private HorseEntityModel<HorseEntity> model;

    public HorseArmorFeatureRendererMixin(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
        super(context);
    }

    /**
     * @author gizmonster
     * @reason Simply adds enchantment glint support for horse armor.
     */

    @Overwrite
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, HorseEntity horseEntity, float f, float g, float h, float j, float k, float l) {
        ItemStack itemStack = horseEntity.getArmorType();
        boolean glint = false;
        if (itemStack.getItem() instanceof HorseArmorItem) {
            HorseArmorItem horseArmorItem = (HorseArmorItem)itemStack.getItem();
            if (itemStack.hasGlint()) {
                glint = true;
            }
            ((HorseEntityModel)this.getContextModel()).copyStateTo(this.model);
            this.model.animateModel(horseEntity, f, g, h);
            this.model.setAngles(horseEntity, f, g, j, k, l);
            float n;
            float o;
            float p;
            if (horseArmorItem instanceof DyeableHorseArmorItem) {
                int m = ((DyeableHorseArmorItem)horseArmorItem).getColor(itemStack);
                n = (float)(m >> 16 & 255) / 255.0F;
                o = (float)(m >> 8 & 255) / 255.0F;
                p = (float)(m & 255) / 255.0F;
            } else {
                n = 1.0F;
                o = 1.0F;
                p = 1.0F;
            }
            //VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(horseArmorItem.getEntityTexture()));
            VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(horseArmorItem.getEntityTexture()), false, glint);
            this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, n, o, p, 1.0F);
        }
    }
}
