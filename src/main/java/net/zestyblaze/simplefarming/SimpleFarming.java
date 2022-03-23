package net.zestyblaze.simplefarming;

import net.fabricmc.api.ModInitializer;
import net.zestyblaze.simplefarming.config.SFModConfig;
import net.zestyblaze.simplefarming.registry.SFConfigInit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleFarming implements ModInitializer {
	public static final String MODID = "";
	public static final String MODNAME = "";
	public static final Logger LOGGER = LogManager.getLogger(MODNAME);

	@Override
	public void onInitialize() {
		LOGGER.info(" is installed correctly, loading now! Thanks for installing! <3");
		SFConfigInit.registerConfig();

		if(SFModConfig.get().debugMode) {
			LOGGER.info(" Common: Registry - Mod Fully Loaded!");
		}
	}
}
