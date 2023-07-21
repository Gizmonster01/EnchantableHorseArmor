package monster.giz.EnchantableHorseArmor;

import monster.giz.EnchantableHorseArmor.access.HorseArmorItemAccess;
import monster.giz.EnchantableHorseArmor.enchantments.HorseEnchantments;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class EnchantableHorseArmor implements ModInitializer {
	public static final String NAMESPACE = "enchantablehorsearmor";

	public static final TagKey<Item> HORSE_ARMOR = TagKey.of(Registries.ITEM.getKey(), Identifier.of(Identifier.DEFAULT_NAMESPACE, "horse_armor"));

	public static boolean isHorseArmor(ItemStack stack) {
		return stack.getRegistryEntry().isIn(EnchantableHorseArmor.HORSE_ARMOR);
	}
	public static boolean isHorseArmor(Item item) {
		return item.getDefaultStack().getRegistryEntry().isIn(EnchantableHorseArmor.HORSE_ARMOR);
	}
	@Override
	public void onInitialize() {
		HorseEnchantments.initialize();
		((HorseArmorItemAccess) Items.IRON_HORSE_ARMOR).setMaterial(ArmorMaterials.LEATHER);
		((HorseArmorItemAccess) Items.DIAMOND_HORSE_ARMOR).setMaterial(ArmorMaterials.DIAMOND);
		((HorseArmorItemAccess) Items.GOLDEN_HORSE_ARMOR).setMaterial(ArmorMaterials.GOLD);
	}


}
