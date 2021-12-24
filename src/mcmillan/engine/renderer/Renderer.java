package mcmillan.engine.renderer;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import mcmillan.engine.core.Window;
import mcmillan.engine.math.Int2;
import mcmillan.engine.math.IntTransform;

public abstract class Renderer {
	
	private static ArrayList<IntTransform> clipRectStack = new ArrayList<>();
	private static ArrayList<Int2> translationStack = new ArrayList<>();
	
	private static Font defaultFont = new Font("Roboto", Font.PLAIN, 18); 
	private static FontMetrics defaultFontMetrics = new Canvas().getFontMetrics(defaultFont);
	
	private static int viewportWidth = Window.CANVAS_SIZE, viewportHeight = Window.CANVAS_SIZE;

	public static Int2 viewport() { return new Int2(viewportWidth, viewportHeight); }
	
	private static List<Renderable> renderables = new ArrayList<>();
	
//	 TODO: ADD TEXT SUPPORT TO NEW RENDERER!
	
	// Frame methods and fields
	private static boolean frameClosed = true;
	
	public static void beginFrame(Int2 viewport) {
		viewportWidth = viewport.x;
		viewportHeight = viewport.y;
		frameClosed = false;
		
		// Lists
		if (clipRectStack != null) {
			clipRectStack.clear();
		} else {
			clipRectStack = new ArrayList<>();
		}
		if (translationStack != null) {
			translationStack.clear();
		} else {
			translationStack = new ArrayList<>();
		}
		if (renderables != null) {
			renderables.clear();
		} else {
			renderables = new ArrayList<>();
		}
		submit(RenderCommand.pushClipRect(new IntTransform(0, 0, viewportWidth, viewportHeight)));
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
		
		for (Renderable cmd : renderables)
			cmd.render(g);
		g.dispose();
	}
	
	// Submit a Graphics command
	public static void submit(Renderable cmd) { if (!frameClosed) renderables.add(cmd); }

	// Clipping
	public static void pushClipRect(Graphics g, IntTransform clip) {
		Rectangle bounds = g.getClipBounds();
		clipRectStack.add(bounds != null ? new IntTransform(bounds) : new IntTransform(0, 0, viewportWidth, viewportHeight));
		g.setClip(clip.position.x, clip.position.y, clip.scale.x, clip.scale.y);
	}
	
	public static void popClipRect(Graphics g) {
		int stackSize = clipRectStack.size();
		if (stackSize > 0) {
			IntTransform lastClip = clipRectStack.remove(clipRectStack.size()-1);
			g.setClip(lastClip.position.x, lastClip.position.y, lastClip.scale.x, lastClip.scale.y);
		} else {
			System.err.println("No clips to pop left in Renderer.clipRectStack!");
			g.setClip(0, 0, viewportWidth, viewportHeight);
		}
	}
	
	// Translation
	public static void pushTranslation(Graphics g, Int2 translation) {
		translationStack.add(new Int2(translation)); // Add a copy of the translation
		g.translate(translation.x, translation.y);
	}
	
	public static void popTranslation(Graphics g) {
		int stackSize = translationStack.size();
		if (stackSize > 0) {
			Int2 translation = translationStack.remove(stackSize-1);
			translation.negative();
			g.translate(translation.x, translation.y);
		} else {
			System.err.println("No translations to pop left in Renderer.translationStack!");
		}
	}
	
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
}
