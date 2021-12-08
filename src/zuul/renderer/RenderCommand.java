package zuul.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import zuul.math.Int2;
import zuul.math.IntTransform;

public class RenderCommand {
	
	public static Class<Graphics> gfxcls = Graphics.class;
	
	private Method method;
	private Object[] args;
	
	private RenderCommand(Method m, Object... args) {
		this.method = m;
		this.args = args;
		
	}
	
	public void fire(Graphics g) {
		try {
			method.invoke(g, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static RenderCommand setColor(Color c) {
		try {
			Method m = gfxcls.getMethod("setColor", Color.class);
			// Store current value of passed color. ( Even though I think Color is read-only once constructed. )
			return new RenderCommand(m, new Color(c.getRGB(), true)); 
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static RenderCommand drawString(String s, Int2 p) {
		try {
			Method m = gfxcls.getMethod("drawString", String.class, int.class, int.class);
			// Store current value of passed string.
			return new RenderCommand(m, s, p.x, p.y); 
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static RenderCommand fillRect(IntTransform t) {
		try {
			Method m = gfxcls.getMethod("fillRect", int.class, int.class, int.class, int.class);
			return new RenderCommand(m,
					t.position.x,t.position.y,
					t.scale.x,t.scale.y);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static RenderCommand drawRect(IntTransform t) {
		try {
			Method m = gfxcls.getMethod("drawRect", int.class, int.class, int.class, int.class);
			return new RenderCommand(m,
					t.position.x,t.position.y,
					t.scale.x,t.scale.y);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
	

	public static RenderCommand centerAt(Int2 center, Int2 viewport) {
		try {
			Method m = gfxcls.getMethod("translate", int.class, int.class);
			return new RenderCommand(m,
					-center.x+viewport.x/2,
					-center.y+viewport.y/2);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
