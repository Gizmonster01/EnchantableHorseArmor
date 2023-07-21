package monster.giz.EnchantableHorseArmor.mixin;

import monster.giz.EnchantableHorseArmor.access.ArmorTrimAccess;
import monster.giz.EnchantableHorseArmor.access.HorseArmorFeatureAccess;
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
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.DyeableHorseArmorItem;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.client.render.TexturedRenderLayers.ARMOR_TRIMS_ATLAS_TEXTURE;

@Environment(EnvType.CLIENT)
@Mixin(HorseArmorFeatureRenderer.class)
public class HorseArmorFeatureRendererMixin extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> implements HorseArmorFeatureAccess {
    @Shadow @Final
    private HorseEntityModel<HorseEntity> model;
    private SpriteAtlasTexture armorTrimsAtlas;

    public HorseArmorFeatureRendererMixin(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
        super(context);
    }

    /**
     * @author gizmonster
     * @reason To be fully replaced with better practice once proof of concept is complete
     */
    @Overwrite
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, HorseEntity horseEntity, float entityYaw, float partialTicks, float headPitch, float scale, float tickDelta, float animationProgress) {
        ItemStack armorStack = horseEntity.getArmorType();
        if (!(armorStack.getItem() instanceof HorseArmorItem))
            return;

        HorseArmorItem horseArmorItem = (HorseArmorItem) armorStack.getItem();
        boolean hasGlint = armorStack.hasGlint();

        this.getContextModel().copyStateTo(this.model);
        this.model.animateModel(horseEntity, entityYaw, partialTicks, headPitch);
        this.model.setAngles(horseEntity, entityYaw, partialTicks, scale, tickDelta, animationProgress);

        int color = 0xFFFFFF; // Default color (white)
        if (horseArmorItem instanceof DyeableHorseArmorItem) {
            color = ((DyeableHorseArmorItem) horseArmorItem).getColor(armorStack);
        }

        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        // TODO: Modify consumer to support enchantment glint
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(horseArmorItem.getEntityTexture()));

        this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, 1.0F);

        ArmorTrim.getTrim(horseEntity.getWorld().getRegistryManager(), armorStack).ifPresent(trim -> {
            this.renderArmorTrim(horseArmorItem, matrices, vertexConsumerProvider, light, trim);
        });
    }

    private void renderArmorTrim(HorseArmorItem item, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorTrim trim) {
        Identifier trimIdentifier = ((ArmorTrimAccess) trim).getHorseTrimModelIdentifier(item);
        Sprite sprite = this.armorTrimsAtlas.getSprite(trimIdentifier);
        VertexConsumer vertexConsumer = sprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(ARMOR_TRIMS_ATLAS_TEXTURE, false)));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void defineAtlas(BakedModelManager bakery) {
        this.armorTrimsAtlas = bakery.getAtlas(ARMOR_TRIMS_ATLAS_TEXTURE);
    }

}

