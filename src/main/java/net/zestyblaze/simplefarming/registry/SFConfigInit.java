package net.zestyblaze.simplefarming.registry;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.zestyblaze.simplefarming.SimpleFarming;
import net.zestyblaze.simplefarming.config.SFModConfig;

public class SFConfigInit {
    public static void registerConfig() {
        AutoConfig.register(SFModConfig.class, GsonConfigSerializer::new);

        if(SFModConfig.get().debugMode) {
            SimpleFarming.LOGGER.info("ExampleMod Client/Common: Registry - Config Registered");
        }
    }
}
