package monster.giz.Overhorsed.enchantments;

import monster.giz.Overhorsed.Overhorsed;
import monster.giz.Overhorsed.config.OverhorsedConfig;
import monster.giz.Overhorsed.enchantments.horse_armor.LeapingEnchantment;
import monster.giz.Overhorsed.enchantments.horse_armor.StrafingEnchantment;
import monster.giz.Overhorsed.util.OHLogger;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class OverhorsedEnchantments {

    //public static HorseArmorEnchantment AQUA_EQUINE;
    public static OverhorsedEnchantment STRAFING;
    public static OverhorsedEnchantment LEAPING;
    private static final Set<Enchantment> horseEnchantmentsSet = new HashSet<>();

    public static void initialize() {
        if (OverhorsedConfig.getBoolean("enchanting.enchantments.strafing.enabled")) {
            STRAFING = Registry.register(
                    Registries.ENCHANTMENT,
                    new Identifier(Overhorsed.NAMESPACE, "strafing"),
                    new StrafingEnchantment(
                            OverhorsedConfig.getInt("enchanting.enchantments.strafing.max_level", 3),
                            OverhorsedConfig.getBoolean("enchanting.enchantments.strafing.treasure"),
                            OverhorsedConfig.getFloat("enchanting.enchantments.strafing.factor", 0.25f)
                    )
            );
        }
        if (OverhorsedConfig.getBoolean("enchanting.enchantments.leaping.enabled")) {
            LEAPING = Registry.register(
                    Registries.ENCHANTMENT,
                    new Identifier(Overhorsed.NAMESPACE, "leaping"),
                    new LeapingEnchantment(
                            OverhorsedConfig.getInt("enchanting.enchantments.leaping.max_level", 2),
                            OverhorsedConfig.getBoolean("enchanting.enchantments.leaping.treasure"),
                            OverhorsedConfig.getFloat("enchanting.enchantments.leaping.factor", 0.5f)
                    )
            );
        }
        loadEnchantments();
    }

    private static void loadEnchantments() {
        List<String> enchantmentPool = OverhorsedConfig.getStringList("enchanting.horse-armor-enchantment-pool");
        for (String string : enchantmentPool) {
            Enchantment enchantment = Registries.ENCHANTMENT.get(Identifier.tryParse(string));
            if (enchantment == null) {
                OHLogger.log("Enchantment " + string + " not found.");
                continue;
            }
            OHLogger.log("Enchantment " + enchantment.getTranslationKey() + " recognized! Max Level: " + enchantment.getMaxLevel());
            addEnchantment(enchantment);
        }
    }

    private static void addEnchantment(Enchantment enchantment) {
        horseEnchantmentsSet.add(enchantment);
    }

    public static boolean isAcceptableHorseEnchantment(Enchantment enchantment) {
        return horseEnchantmentsSet.contains(enchantment);
    }

    public static List<EnchantmentLevelEntry> getPossibleHorseEntries(int power, boolean treasureAllowed) {
        List<EnchantmentLevelEntry> list = new ArrayList<>();
        for (Enchantment ench : horseEnchantmentsSet) {
            if (!ench.isTreasure() || treasureAllowed) {
                if (ench.isAvailableForRandomSelection()) {
                    for (int i = ench.getMaxLevel(); i > ench.getMinLevel() - 1; --i) {
                        if (power >= ench.getMinPower(i) && power <= ench.getMaxPower(i)) {
                            list.add(new EnchantmentLevelEntry(ench, i));
                            break;
                        }
                    }
                }
            }
        }
        return list;
    }

}
