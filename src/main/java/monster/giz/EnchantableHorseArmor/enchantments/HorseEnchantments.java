package monster.giz.EnchantableHorseArmor.enchantments;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import monster.giz.EnchantableHorseArmor.EnchantableHorseArmor;
import monster.giz.EnchantableHorseArmor.enchantments.horse_armor.HippocampusEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class HorseEnchantments {

    public static HorseArmorEnchantment AQUA_EQUINE;

    private static List<Enchantment> horseEnchantments;

    private static HashSet<Enchantment> horseEnchantmentsSet;


    static {
        horseEnchantments = new ArrayList<>();
        horseEnchantments.add(Enchantments.PROTECTION);
        horseEnchantments.add(Enchantments.PROJECTILE_PROTECTION);
        horseEnchantments.add(Enchantments.BLAST_PROTECTION);
        horseEnchantments.add(Enchantments.FIRE_PROTECTION);
        horseEnchantments.add(Enchantments.FEATHER_FALLING);
        horseEnchantments.add(Enchantments.FROST_WALKER);
        horseEnchantments.add(Enchantments.DEPTH_STRIDER);
        horseEnchantments.add(Enchantments.SOUL_SPEED);
        horseEnchantments.add(Enchantments.THORNS);

        horseEnchantmentsSet = new HashSet<Enchantment>();
    }
    public static void initialize() {
        AQUA_EQUINE = Registry.register(
                Registry.ENCHANTMENT,
                new Identifier(EnchantableHorseArmor.NAMESPACE, "aqua_equine"),
                new HippocampusEnchantment()
        );
        horseEnchantments.add(AQUA_EQUINE);

        horseEnchantmentsSet = Sets.newHashSet(horseEnchantments);
    }

    public static boolean isAcceptableHorseEnchantment(Enchantment enchantment) {
        return horseEnchantmentsSet.contains(enchantment);
    }

    public static List<EnchantmentLevelEntry> getPossibleHorseEntries(int power, boolean treasureAllowed) {
        List<EnchantmentLevelEntry> list = Lists.newArrayList();
        Iterator var6 = horseEnchantments.iterator();

        while(true) {
            while(true) {
                Enchantment enchantment;
                do {
                    do {
                        if (!var6.hasNext()) {
                            return list;
                        }
                        enchantment = (Enchantment)var6.next();
                    } while(enchantment.isTreasure() && !treasureAllowed);
                } while(!enchantment.isAvailableForRandomSelection());

                for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                    if (power >= enchantment.getMinPower(i) && power <= enchantment.getMaxPower(i)) {
                        list.add(new EnchantmentLevelEntry(enchantment, i));
                        break;
                    }
                }
            }
        }
    }

}
