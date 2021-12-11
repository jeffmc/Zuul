package mcmillan.engine.renderer;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import mcmillan.editor.EditorLayer;
import mcmillan.engine.math.Int2;

public abstract class Renderer {
	
	private static int viewportWidth = EditorLayer.CANVAS_SIZE, viewportHeight = EditorLayer.CANVAS_SIZE;
	
	private static List<RenderCommand> renderCommands = new ArrayList<>();
	
//	 TODO: ADD TEXT SUPPORT TO NEW RENDERER!
	
	// Frame methods and fields
	private static boolean frameClosed = true;
	
	public static Int2 beginFrame() {
		frameClosed = false;
		if (renderCommands != null) {
			renderCommands.clear();
		} else {
			renderCommands = new ArrayList<>();
		}
		return new Int2(viewportWidth, viewportHeight);
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
}
