package org.github.melodiccougar7.immersivefixes.lib;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class IFLib {
    public static final Logger IF_LOGGER = LogUtils.getLogger();

    private static final Set<String> ACTIVE_LOGGED = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static void logMixinActive(String mixinName) {
        if (ACTIVE_LOGGED.add(mixinName)) { IF_LOGGER.info("Mixin applied and active: {}", mixinName); }
    }
}
