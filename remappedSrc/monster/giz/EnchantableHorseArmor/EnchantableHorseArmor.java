package monster.giz.EnchantableHorseArmor;

import monster.giz.EnchantableHorseArmor.enchantments.HorseEnchantments;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EnchantableHorseArmor implements ModInitializer {
	public static final String NAMESPACE = "enchantablehorsearmor";
	public static final net.minecraft.registry.tag.TagKey<Item> HORSE_ARMOR = net.minecraft.registry.tag.TagKey.of(Registries.ITEM.getKey(), Identifier.of(Identifier.DEFAULT_NAMESPACE, "horse_armor"));

	@Override
	public void onInitialize() {
		HorseEnchantments.initialize();
	}

}
