package io.github.thecodecrafters.imcgui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class ImcGUI implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("ImcGUI");

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info( "Hello Quilt world from ImcGUI v{}!", mod.metadata().version().raw() );
	}
}
