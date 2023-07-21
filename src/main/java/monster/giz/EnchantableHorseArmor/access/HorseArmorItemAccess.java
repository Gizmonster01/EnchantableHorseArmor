package monster.giz.EnchantableHorseArmor.access;

import net.minecraft.item.ArmorMaterial;

public interface HorseArmorItemAccess {

    void setMaterial(ArmorMaterial material);

    ArmorMaterial getMaterial();
}
