package io.github.thecodecrafters.imcgui.api;

import org.quiltmc.qsl.base.api.event.Event;

@FunctionalInterface
public interface ImguiRenderEvent {
	Event<ImguiRenderEvent> EVENT = Event.create(
		ImguiRenderEvent.class,
		listeners -> () -> {
			for ( var listener : listeners )
				listener.onRender();
		}
	);
	void onRender();
}
