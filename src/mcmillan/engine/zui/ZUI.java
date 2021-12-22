package mcmillan.engine.zui;

import java.awt.Color;

import mcmillan.engine.math.Int2;
import mcmillan.engine.math.IntTransform;
import mcmillan.engine.renderer.RenderCommand;
import mcmillan.engine.renderer.Renderer;
import mcmillan.engine.renderer.TextAnchor;

public abstract class ZUI {
	
	private static String currentTitle;
	private static Int2 cursor;
	
	private static final int MARGIN = 5;
	private static final int THIN_MARGIN = 2;
	private static final String testStr = "AaBbCcDdEeFfGg";
	
	private static final Color FRAME_BACKGROUND = new Color(10,10,10);
	private static final Color FRAME_BORDER = new Color(120,120,120);
	
	private static final Color FOREGROUND_PRIMARY = new Color(50,205,205);
	private static final Color ALERT = new Color(205,50,50);
	private static final Color DEBUG = new Color(50,50,205);
	
	public static void begin(String title) {
		currentTitle = title;
		cursor = new Int2(Renderer.getFontMetrics().getHeight() + MARGIN*2, MARGIN);
	}

	public static void end() {
		IntTransform frameBounds = new IntTransform(10, 10, 300, 650);
		Renderer.submit(RenderCommand.fillRect(FRAME_BACKGROUND, frameBounds));
		Renderer.submit(RenderCommand.drawRect(FRAME_BORDER, frameBounds));
		Renderer.submit(RenderCommand.translate(frameBounds.position));
		IntTransform title = new IntTransform(MARGIN, MARGIN, 
				frameBounds.scale.x-MARGIN*2, 24);
//		Renderer.submit(RenderCommand.drawRect(DEBUG, title));
		Renderer.submit(RenderCommand.text(FOREGROUND_PRIMARY, currentTitle, title.position, TextAnchor.LEFT, TextAnchor.TOP));
		
		IntTransform resizer = new IntTransform(0, 0, 10, 10);
		resizer.position.set(frameBounds.scale.x-THIN_MARGIN-resizer.scale.x, 
				frameBounds.scale.y-THIN_MARGIN-resizer.scale.y);
		Renderer.submit(RenderCommand.fillRect(DEBUG, resizer));
		
		String sample = "AaPpQq";
		Int2 boxSz = new Int2(4,4);
		Int2 txtPos = Int2.add(title.position, title.scale).div(2);
		for (TextAnchor h : new TextAnchor[]{TextAnchor.LEFT, TextAnchor.RIGHT, TextAnchor.CENTER}) {
			for (TextAnchor v : new TextAnchor[]{TextAnchor.BOTTOM, TextAnchor.BASELINE, TextAnchor.CENTER, TextAnchor.TOP}) {
				txtPos.y += 48;
				Renderer.submit(RenderCommand.text(FOREGROUND_PRIMARY, sample, txtPos, h, v));
				Renderer.submit(RenderCommand.textBounding(ALERT, sample, txtPos, h, v));
				Renderer.submit(RenderCommand.drawBox(DEBUG, new IntTransform(txtPos, boxSz)));
			}
		}
	}
}
