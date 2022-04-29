package io.github.thecodecrafters.imcgui.mixin;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import io.github.thecodecrafters.imcgui.ImcGUI;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowMixin {
	@Shadow @Final private long handle;

	@Inject( method = "<init>", at = @At("TAIL") )
	public void onInit(WindowEventHandler windowEventHandler, MonitorTracker monitorTracker, WindowSettings windowSettings, String string, String string2, CallbackInfo ci) {
		// init imgui
		ImGui.createContext();
		ImGuiIO io = ImGui.getIO();
		io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
		// TODO: Find a way to load mc's font
//		ImGui.pushFont( io.getFonts().addFont( new byte[] {}, 16 ) );
		// init bindings
		ImcGUI.get().getGlfw().init( this.handle, true );
		ImcGUI.get().getGl3().init( "#version 130" );
	}

	@Inject( method = "close", at = @At( value = "INVOKE", target = "Lorg/lwjgl/glfw/Callbacks;glfwFreeCallbacks(J)V" ) )
	public void onClose(CallbackInfo ci) {
		ImcGUI.get().getGl3().dispose();
		ImcGUI.get().getGlfw().dispose();
		ImGui.destroyContext();
	}
}
