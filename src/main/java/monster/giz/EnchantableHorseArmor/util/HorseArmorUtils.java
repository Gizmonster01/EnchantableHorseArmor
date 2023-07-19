package monster.giz.EnchantableHorseArmor.util;

import net.minecraft.item.Item;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.util.Identifier;

public class HorseArmorUtils {

    public static Identifier getHorseTrimIdentifier(ArmorTrim trim, Item item) {
        Identifier identifier = (trim.getPattern().value()).assetId();
        return identifier.withPath((path) -> {
            return "trims/models/horse/" + path + "_leggings_" + getHorseArmorTypeOrName(item);
        });
    }

    public static String getHorseArmorTypeOrName(Item item) {
        // Do a little lookup thing here later, registry for custom horse armor or something idk
        return switch (item.getName().getString()) {
            case "leather_horse_armor" -> "leather";
            case "iron_horse_armor" -> "iron";
            case "golden_horse_armor" -> "gold";
            case "diamond_horse_armor" -> "diamond";
            default -> "oopsie";
        };
    }


}
