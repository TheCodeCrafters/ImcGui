package io.github.thecodecrafters.imcgui;

import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import io.github.thecodecrafters.imcgui.api.ImguiRenderEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class ImcGUI implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("ImcGUI");
	private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
	private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
	private static ImcGUI INSTANCE;

	public static ImcGUI get() {
		return INSTANCE;
	}

	public ImcGUI() {
		INSTANCE = this;
	}

	public ImGuiImplGl3 getGl3() {
		return imGuiGl3;
	}

	public ImGuiImplGlfw getGlfw() {
		return imGuiGlfw;
	}

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info(
			"Initialized ImcGUI v{} with ImGUI v{}",
			mod.metadata().version().raw(),
			ImGui.getVersion()
		);
		if ( QuiltLoader.isDevelopmentEnvironment() )
			ImguiRenderEvent.EVENT.register( () -> {
				ImGui.begin("Test");
					ImGui.labelText("Hello world!", "Label:");
				ImGui.end();
			} );
	}
}
