package monster.giz.Overhorsed.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class OHLogger {

    public static final Logger LOGGER = LoggerFactory.getLogger("overhorsed");
    private static final Set<String> loggedOnce = new HashSet<>();

    public static void log(String string) { LOGGER.info(string); }

    public static void logOnce(String string) {
        if (!loggedOnce.contains(string)) {
            LOGGER.info(string);
            loggedOnce.add(string);
        }
    }
}
