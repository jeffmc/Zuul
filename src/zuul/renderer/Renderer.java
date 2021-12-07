package zuul.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import zuul.math.Int2;
import zuul.math.IntTransform;
import zuul.scene.Scene;

public class Renderer { // TODO: Make static
	
	private Rectangle bounds;
	
	private Scene activeScene;
	
	private List<RenderCommand> renderCommands;
	
//	 TODO: ADD TEXT SUPPORT TO NEW RENDERER!
	public Renderer() {
		activeScene = null;
		renderCommands = new ArrayList<>();
	}
	
	public void beginFrame() {
		renderCommands.clear();
	}
	
	public void endFrame() {
		
	}
	
	public void drawFrame(Graphics g) {
		for (RenderCommand cmd : renderCommands)
			cmd.fire(g);
	}
	
	public void addCmd(RenderCommand cmd) { renderCommands.add(cmd); }
	
	public void setActiveScene(Scene scene) {
		activeScene = scene;
	}
	
	public Scene getActiveScene() { return activeScene; }
	
	public void drawElements(Graphics g, Color bg, Int2 camera) {
		// Get canvas bounds
		bounds = g.getClipBounds();
		
		// Fill background with background color specified in constructor.
		g.setColor(bg);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		// Draw 4x4 white rect at center of board, mostly for debugging
		g.setColor(Color.white);
		g.fillRect(bounds.width/2-2, bounds.height/2-2, 4, 4);
		
		
		beginFrame();
		addCmd(RenderCommand.setColor(Color.WHITE));
		addCmd(RenderCommand.drawString(camera.toString(), new Int2(5, bounds.height-5))); // Draw camera position in bottom-left corner.
		
		addCmd(RenderCommand.setColor(new Color(255,0,0)));
		addCmd(RenderCommand.fillRect(new IntTransform(10, 20, 50, 30)));
		addCmd(RenderCommand.setColor(new Color(0,255,0)));
		addCmd(RenderCommand.drawRect(new IntTransform(10, 20, 50, 30)));
		endFrame();
		drawFrame(g);
		
		// Center canvas at camera coords TODO: Add camera zoom
		centerAt(g, camera);
		
		activeScene.draw(g);
		
	}

	public static String absoluteTrace(int stackTraceOffset) { 
		return methodName(1+stackTraceOffset) + "(" + fileName(1+stackTraceOffset) + ":" + lineNumber(1+stackTraceOffset) + ")"; }
	public static String absoluteTrace() { return absoluteTrace(1); }
	
	public static String fileName(int stackTraceOffset) {
		return Thread.currentThread().getStackTrace()[2+stackTraceOffset].getFileName(); }
	public static String fileName() { return fileName(1); }
	
	public static String methodName(int stackTraceOffset) { 
		return Thread.currentThread().getStackTrace()[2+stackTraceOffset].getMethodName(); }
	public static String methodName() { return methodName(1); }
			
	public static int lineNumber(int stackTraceOffset) { 
		return Thread.currentThread().getStackTrace()[2+stackTraceOffset].getLineNumber(); }
	public static int lineNumber() { return lineNumber(1); }
	
	@Deprecated
	@SuppressWarnings("unused")
	private void drawElement(Graphics g, Renderable r) {
		Material m = r.material;
		Int2 pos = r.transform.position, scale = r.transform.scale;
		Int2 hscale, ulpos;
		switch (r.shape) {
		case BOX:
			hscale = new Int2(scale).div(2);
			ulpos = new Int2(pos).sub(hscale);
			if (m.fill != null) {
				g.setColor(m.fill);
				g.fillRect(ulpos.x, ulpos.y, scale.x, scale.y);
			}
			if (m.stroke != null) {
				g.setColor(m.stroke);
				g.drawRect(ulpos.x, ulpos.y, scale.x, scale.y);
			}
			break;
		case LINE: // Fill not supported for LINE
			if (m.stroke != null) {
				g.setColor(m.stroke);
				g.drawLine(pos.x, pos.y, scale.x, scale.y);
			}
			break;
		case OVAL:
			hscale = new Int2(scale).div(2);
			ulpos = new Int2(pos).sub(hscale);
			if (m.fill != null) {
				g.setColor(m.fill);
				g.fillOval(ulpos.x, ulpos.y, scale.x, scale.y);
			}
			if (m.stroke != null) {
				g.setColor(m.stroke);
				g.drawOval(ulpos.x, ulpos.y, scale.x, scale.y);
			}
			break;
		}
	}
	
//	public void submit(Renderable r) {
//	}
	
	private void centerAt(Graphics g, Int2 center) {
		g.translate(-center.x+bounds.width/2,-center.y+bounds.height/2);
	}
}
