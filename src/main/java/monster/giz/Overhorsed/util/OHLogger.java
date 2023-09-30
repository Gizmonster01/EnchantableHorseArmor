package monster.giz.Overhorsed.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OHLogger {

    public static final Logger LOGGER = LoggerFactory.getLogger("enchantablehorsearmor");

    public static void log(String string) { LOGGER.info(string); }
}
