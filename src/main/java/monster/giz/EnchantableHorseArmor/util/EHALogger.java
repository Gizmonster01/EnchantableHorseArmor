package monster.giz.EnchantableHorseArmor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EHALogger {

    public static final Logger LOGGER = LoggerFactory.getLogger("enchantablehorsearmor");

    public static void log(String string) { LOGGER.info(string); }
}
