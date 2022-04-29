package io.github.thecodecrafters.imcgui.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import io.github.thecodecrafters.imcgui.ImcGUI;
import io.github.thecodecrafters.imcgui.api.ImguiRenderEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.profiler.ProfilerSystem;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public abstract class RenderSystemMixin {
	@Inject( method = "flipFrame", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;clear()V" ) )
	private static void onFlipFrame( long l, CallbackInfo ci ) {
		if ( MinecraftClient.getInstance().isRunning() ) {
			ImcGUI.get().getGlfw().newFrame();
			ImGui.newFrame();

			MinecraftClient.getInstance().getProfiler().push("ImcGUI-RenderEvent");
				ImguiRenderEvent.EVENT.invoker().onRender();
			MinecraftClient.getInstance().getProfiler().swap("ImcGUI-Render");
				ImGui.render();
				ImcGUI.get().getGl3().renderDrawData( ImGui.getDrawData() );
			MinecraftClient.getInstance().getProfiler().pop();

			if ( ImGui.getIO().hasConfigFlags( ImGuiConfigFlags.ViewportsEnable ) ) {
				final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
				ImGui.updatePlatformWindows();
				ImGui.renderPlatformWindowsDefault();
				GLFW.glfwMakeContextCurrent(backupWindowPtr);
			}
		}
	}
}
