package io.github.thecodecrafters.imcgui.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import io.github.thecodecrafters.imcgui.ImguiLayer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.LongSupplier;

@Mixin(RenderSystem.class)
public abstract class RenderSystemMixin {
	private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
	private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
	private static final ImguiLayer layer = new ImguiLayer();

	@Inject( method = "initBackendSystem", at = @At("TAIL"), remap = false )
	private static void onInitBackendSystem(CallbackInfoReturnable<LongSupplier> cir) {
		// init imgui
		ImGui.createContext();
		ImGuiIO io = ImGui.getIO();
		io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
		// inti bindings
		// TODO: we must postpone this cuz the window hasn't been created yet at this stage
		imGuiGlfw.init( MinecraftClient.getInstance().getWindow().getHandle(), true);
		imGuiGl3.init( "#version 130" );
	}

	@Inject( method = "flipFrame", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;clear()V" ) )
	private static void onFlipFrame( long l, CallbackInfo ci ) {
		imGuiGlfw.newFrame();
		ImGui.newFrame();

		layer.imgui();

		ImGui.render();
		imGuiGl3.renderDrawData(ImGui.getDrawData());

		if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
			final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
			ImGui.updatePlatformWindows();
			ImGui.renderPlatformWindowsDefault();
			GLFW.glfwMakeContextCurrent(backupWindowPtr);
		}
	}
}
