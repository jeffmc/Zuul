package zuul.renderer;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import zuul.SceneEditor;
import zuul.math.Int2;

public abstract class Renderer {
	
	private static int viewportWidth = SceneEditor.EDITOR_SIZE, viewportHeight = SceneEditor.EDITOR_SIZE;
	
	private static List<RenderCommand> renderCommands = new ArrayList<>();
	
//	 TODO: ADD TEXT SUPPORT TO NEW RENDERER!
	
	// Frame methods and fields
	private static boolean frameClosed = true;
	
	public static void beginFrame() {
		frameClosed = false;
		if (renderCommands != null) {
			renderCommands.clear();
		} else {
			renderCommands = new ArrayList<>();
		}
	}
	
	public static void endFrame() {
		frameClosed = true;
	}
	
	public static void drawFrame(Graphics g) {
		if (!frameClosed) System.err.println("Renderer: drawFrame called before frameClosed == true!");
		for (RenderCommand cmd : renderCommands)
			cmd.fire(g);
	}
	
	// Submit a Graphics command
	public static void submit(RenderCommand cmd) { if (!frameClosed) renderCommands.add(cmd); }

	// Viewport methods
	public static void setViewport(int width, int height) { viewportWidth = width; viewportHeight = height; }
	public static int viewportWidth() { return viewportWidth; }
	public static int viewportHeight() { return viewportHeight; }
	
	// Weird debugger methods
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
	
	// Old methods
//	private void drawElement(Graphics g, Renderable r) {
//		Material m = r.material;
//		Int2 pos = r.transform.position, scale = r.transform.scale;
//		Int2 hscale, ulpos;
//		switch (r.shape) {
//		case BOX:
//			hscale = new Int2(scale).div(2);
//			ulpos = new Int2(pos).sub(hscale);
//			if (m.fill != null) {
//				g.setColor(m.fill);
//				g.fillRect(ulpos.x, ulpos.y, scale.x, scale.y);
//			}
//			if (m.stroke != null) {
//				g.setColor(m.stroke);
//				g.drawRect(ulpos.x, ulpos.y, scale.x, scale.y);
//			}
//			break;
//		case LINE: // Fill not supported for LINE
//			if (m.stroke != null) {
//				g.setColor(m.stroke);
//				g.drawLine(pos.x, pos.y, scale.x, scale.y);
//			}
//			break;
//		case OVAL:
//			hscale = new Int2(scale).div(2);
//			ulpos = new Int2(pos).sub(hscale);
//			if (m.fill != null) {
//				g.setColor(m.fill);
//				g.fillOval(ulpos.x, ulpos.y, scale.x, scale.y);
//			}
//			if (m.stroke != null) {
//				g.setColor(m.stroke);
//				g.drawOval(ulpos.x, ulpos.y, scale.x, scale.y);
//			}
//			break;
//		}
//	}
}
