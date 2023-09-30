package monster.giz.Overhorsed.mixin;

import monster.giz.Overhorsed.access.ArmorTrimAccess;
import monster.giz.Overhorsed.access.HorseArmorFeatureAccess;
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
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.minecraft.client.render.TexturedRenderLayers.ARMOR_TRIMS_ATLAS_TEXTURE;

@Environment(EnvType.CLIENT)
@Mixin(HorseArmorFeatureRenderer.class)
public abstract class HorseArmorFeatureRendererMixin extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> implements HorseArmorFeatureAccess {
    @Shadow @Final
    private HorseEntityModel<HorseEntity> model;
    @Unique
    private SpriteAtlasTexture armorTrimsAtlas;

    public HorseArmorFeatureRendererMixin(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, HorseEntity horseEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci, ItemStack armorStack, HorseArmorItem horseArmorItem, float n, float o, float p, VertexConsumer vertexConsumer) {
        ArmorTrim.getTrim(horseEntity.getWorld().getRegistryManager(), armorStack).ifPresent(trim -> {
            this.renderArmorTrim(horseArmorItem, matrices, vertexConsumerProvider, light, trim);
        });
        if (armorStack.hasGlint()) {
            this.renderGlint(matrices, vertexConsumerProvider, light);
        }
    }

    @Unique
    private void renderArmorTrim(HorseArmorItem item, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorTrim trim) {
        Identifier trimIdentifier = ((ArmorTrimAccess) trim).overhorsed$getHorseTrimModelIdentifier(item);
        Sprite sprite = this.armorTrimsAtlas.getSprite(trimIdentifier);
        VertexConsumer vertexConsumer = sprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(ARMOR_TRIMS_ATLAS_TEXTURE, false)));
        model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderGlint(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityGlint()), light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void overhorsed$defineAtlas(BakedModelManager bakery) {
        this.armorTrimsAtlas = bakery.getAtlas(ARMOR_TRIMS_ATLAS_TEXTURE);
    }

}

