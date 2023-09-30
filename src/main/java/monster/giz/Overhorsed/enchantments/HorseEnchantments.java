package monster.giz.Overhorsed.enchantments;

import monster.giz.Overhorsed.Overhorsed;
import monster.giz.Overhorsed.enchantments.horse_armor.StrafingEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HorseEnchantments {

    //public static HorseArmorEnchantment AQUA_EQUINE;
    public static HorseArmorEnchantment STRAFING;
    private static final Set<Enchantment> horseEnchantmentsSet = new HashSet<>();

    public static void initialize() {

        STRAFING = Registry.register(
                Registries.ENCHANTMENT,
                new Identifier(Overhorsed.NAMESPACE, "strafing"),
                new StrafingEnchantment()
        );

        addEnchantment(Enchantments.PROTECTION);
        addEnchantment(Enchantments.PROJECTILE_PROTECTION);
        addEnchantment(Enchantments.BLAST_PROTECTION);
        addEnchantment(Enchantments.FIRE_PROTECTION);
        addEnchantment(Enchantments.FEATHER_FALLING);
        addEnchantment(Enchantments.FROST_WALKER);
        addEnchantment(Enchantments.DEPTH_STRIDER);
        addEnchantment(Enchantments.SOUL_SPEED);
        addEnchantment(Enchantments.THORNS);
        addEnchantment(STRAFING);
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

    /*
    public static boolean hasHorseSwim(LivingEntity entity) {
        if (entity instanceof HorseEntity) {
            HorseEntity horse = ((HorseEntity) entity);
            if (!horse.hasArmorSlot()) {
                return false;
            }
            int i = EnchantmentHelper.getLevel(AQUA_EQUINE, horse.getArmorType());
            if (i > 0) {
                EHALogger.log("Has horse swim enchantment! SWIM HORSEY, SWIM!");
                return true;
            } else {
                EHALogger.log("No dice");
            }
        }
        return false;
    }
     */

}
