package monster.giz.EnchantableHorseArmor.util;

import monster.giz.EnchantableHorseArmor.access.ArmorTrimAccess;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.util.Identifier;

public class HorseArmorTrimHelper {

    public static Identifier getTrimIdentifier(HorseArmorItem item, ArmorTrim trim) {
        EHALogger.log(item.getTranslationKey());
        return Identifier.of("minecraft","trims/models/horse/" + ((ArmorTrimAccess)trim).getHorseMaterialAssetName(item));
    }

}
