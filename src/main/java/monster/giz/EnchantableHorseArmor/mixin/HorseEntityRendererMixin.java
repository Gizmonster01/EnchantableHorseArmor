package monster.giz.EnchantableHorseArmor.mixin;

import monster.giz.EnchantableHorseArmor.util.EHALogger;
import net.minecraft.client.render.entity.AbstractHorseEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.HorseEntityRenderer;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.entity.passive.HorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseEntityRenderer.class)
public abstract class HorseEntityRendererMixin extends AbstractHorseEntityRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {

    public HorseEntityRendererMixin(EntityRendererFactory.Context ctx, HorseEntityModel<HorseEntity> model, float scale) {
        super(ctx, model, scale);
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    public void enchantablehorsearmor$horseEntityRendererConstructorInjector(EntityRendererFactory.Context context, CallbackInfo ci) {
        EHALogger.log("Made it to that point you're looking for ------");
        //((HorseArmorFeatureAccess) features.get(1)).defineAtlas(context.getModelManager());
    }
}
