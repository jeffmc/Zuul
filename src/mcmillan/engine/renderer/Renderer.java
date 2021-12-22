package mcmillan.engine.renderer;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import mcmillan.engine.core.Window;
import mcmillan.engine.math.Int2;

public abstract class Renderer {
	
	private static Font defaultFont = new Font("Roboto", Font.PLAIN, 18); 
	private static FontMetrics defaultFontMetrics = new Canvas().getFontMetrics(defaultFont);
	
	private static int viewportWidth = Window.CANVAS_SIZE, viewportHeight = Window.CANVAS_SIZE;

	public static Int2 viewport() { return new Int2(viewportWidth, viewportHeight); }
	
	private static List<RenderCommand> renderCommands = new ArrayList<>();
	
//	 TODO: ADD TEXT SUPPORT TO NEW RENDERER!
	
	// Frame methods and fields
	private static boolean frameClosed = true;
	
	public static void beginFrame(Int2 viewport) {
		viewportWidth = viewport.x;
		viewportHeight = viewport.y;
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
		if (!frameClosed) throw new IllegalStateException("Renderer: drawFrame called before frameClosed == true!");
		g.setFont(defaultFont);
		if (g instanceof Graphics2D) {
			Graphics2D.class.cast(g).setRenderingHint(
						RenderingHints.KEY_TEXT_ANTIALIASING, 
						RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		} else {
			throw new IllegalArgumentException("Graphics object not an instance of Graphics2D");
		}
		
		for (RenderCommand cmd : renderCommands)
			cmd.fire(g);
		g.dispose();
	}
	
	// Submit a Graphics command
	public static void submit(RenderCommand cmd) { if (!frameClosed) renderCommands.add(cmd); }

	
	// Fonts
	public static void setFontFromContext(Graphics g) {
		defaultFontMetrics = g.getFontMetrics();
		defaultFont = g.getFont();
	}
	
	public static Font getFont() { return defaultFont; }
	public static FontMetrics getFontMetrics() { return defaultFontMetrics; }
	
	public static void printAllFonts() {
		for (Font f : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
			System.out.println(f.getFontName());
		}
	}
	
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
