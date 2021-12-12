package mcmillan.engine.zui;

import java.awt.Color;

import mcmillan.engine.math.IntTransform;
import mcmillan.engine.renderer.RenderCommand;
import mcmillan.engine.renderer.Renderer;

public abstract class ZUI {
	
	private static String currentTitle;
	
	public static void beginFrame(String title) {
		currentTitle = title;
	}

	public static void endFrame() {
		IntTransform frameBounds = new IntTransform(100,100,80,140);
		Renderer.submit(RenderCommand.fillRect(new Color(0,0,0), frameBounds));
		Renderer.submit(RenderCommand.drawRect(new Color(20,235,235), frameBounds));
		
	}
}
