package monster.giz.Overhorsed;

import monster.giz.Overhorsed.access.HorseArmorItemAccess;
import monster.giz.Overhorsed.enchantments.OverhorsedEnchantments;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class Overhorsed implements ModInitializer {
	public static final String NAMESPACE = "overhorsed";

	public static final TagKey<Item> HORSE_ARMOR = TagKey.of(Registries.ITEM.getKey(), Identifier.of(Identifier.DEFAULT_NAMESPACE, "horse_armor"));

	public static boolean isHorseArmor(ItemStack stack) {
		return stack.getRegistryEntry().isIn(Overhorsed.HORSE_ARMOR);
	}
	public static boolean isHorseArmor(Item item) {
		return item.getDefaultStack().getRegistryEntry().isIn(Overhorsed.HORSE_ARMOR);
	}
	@Override
	public void onInitialize() {

		OverhorsedEnchantments.initialize();
		((HorseArmorItemAccess) Items.IRON_HORSE_ARMOR).overhorsed$setMaterial(ArmorMaterials.IRON);
		((HorseArmorItemAccess) Items.DIAMOND_HORSE_ARMOR).overhorsed$setMaterial(ArmorMaterials.DIAMOND);
		((HorseArmorItemAccess) Items.GOLDEN_HORSE_ARMOR).overhorsed$setMaterial(ArmorMaterials.GOLD);

	}


}
