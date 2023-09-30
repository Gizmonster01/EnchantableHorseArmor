package monster.giz.Overhorsed.util;

import monster.giz.Overhorsed.access.ArmorTrimAccess;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.util.Identifier;

public class HorseArmorTrimHelper {

    public static Identifier getTrimIdentifier(HorseArmorItem item, ArmorTrim trim) {
        OHLogger.log(((ArmorTrimAccess) trim).overhorsed$getHorseTrimModelIdentifier(item).toString());
        return ((ArmorTrimAccess) trim).overhorsed$getHorseTrimModelIdentifier(item);
    }

}
