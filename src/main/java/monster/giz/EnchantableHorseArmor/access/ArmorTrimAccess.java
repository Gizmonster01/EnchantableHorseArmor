package monster.giz.EnchantableHorseArmor.access;

import net.minecraft.item.HorseArmorItem;
import net.minecraft.util.Identifier;

public interface ArmorTrimAccess {
    Identifier getHorseTrimModelIdentifier(HorseArmorItem armor);
}