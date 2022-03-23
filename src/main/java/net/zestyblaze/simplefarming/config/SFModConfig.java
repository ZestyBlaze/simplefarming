package net.zestyblaze.simplefarming.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.zestyblaze.simplefarming.SimpleFarming;

@Config(name = SimpleFarming.MODID)
public class SFModConfig implements ConfigData {
    public boolean debugMode = false;

    public static SFModConfig get() {
        return AutoConfig.getConfigHolder(SFModConfig.class).getConfig();
    }
}
