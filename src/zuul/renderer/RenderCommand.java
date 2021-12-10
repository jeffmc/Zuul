package zuul.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import zuul.math.Int2;
import zuul.math.IntTransform;
import zuul.scene.BoxRendererComponent;
import zuul.scene.LineRendererComponent;
import zuul.scene.TransformComponent;

public class RenderCommand {
	
	public static Class<Graphics> gfxcls = Graphics.class;
	
	private int length;
	private Method[] methods;
	private Object[][] args;

	private RenderCommand(Method[] ms, Object[][] args) {
		this.methods = ms;
		this.args = args;
		if (args.length != methods.length) throw new IllegalArgumentException("Argument array length not equal to method array length!");
		length = methods.length;
	}
	private RenderCommand(Method m, Object... args) {
		this(new Method[] {m}, new Object[][] {args});
	}
	private RenderCommand() {
		this(new Method[0], new Object[0][]);
	}
	private RenderCommand(RenderCommand... cmds) {
		int totalLength = 0;
		for (RenderCommand c : cmds) totalLength += c.length;
		this.methods = new Method[totalLength];
		this.args = new Object[totalLength][];
		int l = 0;
		for (RenderCommand cmd : cmds) {
			for (int i=0;i<cmd.length;i++) {
				if (l >= totalLength) throw new ArrayIndexOutOfBoundsException("Iterated past full length!");
				this.methods[l] = cmd.methods[i];
				this.args[l] = cmd.args[i];
				l++;
			}
		}
		if (l != totalLength) throw new IllegalStateException("Did not iterate to full length!");
		this.length = totalLength;
	}
	
	public void fire(Graphics g) {
		try {
			for (int i=0;i<length;i++)
				methods[i].invoke(g, args[i]);
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

	public static RenderCommand drawLine(Int2 a, Int2 b) {
		try {
			Method m = gfxcls.getMethod("drawLine", int.class, int.class, int.class, int.class);
			return new RenderCommand(m,
					a.x,a.y,b.x,b.y);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	// TODO: Line and text render commands (TextAnchor modes?)
	
	public static RenderCommand box(TransformComponent tc, BoxRendererComponent brc) {
		Material m = brc.material;
		IntTransform t = new IntTransform(tc.transform); // New transform defined by upper-left coordinate for drawing
		t.position.sub(Int2.div(t.scale, 2));
		if (m.stroke!=null&&m.fill!=null) {
			return new RenderCommand(
				RenderCommand.setColor(m.fill),
				RenderCommand.fillRect(new IntTransform(t.position, t.scale)),
				RenderCommand.setColor(m.stroke), 
				RenderCommand.drawRect(new IntTransform(t.position, t.scale))
			);
		} else if (m.stroke!=null) {
			return new RenderCommand(
				RenderCommand.setColor(m.stroke), 
				RenderCommand.drawRect(new IntTransform(t.position, t.scale))
			);
		} else if (m.fill!=null) {
			return new RenderCommand(
					RenderCommand.setColor(m.fill),
					RenderCommand.fillRect(new IntTransform(t.position, t.scale))
				);
		} else {
			return new RenderCommand();
		}
	}
	public static RenderCommand line(LineRendererComponent lrc) {
		return new RenderCommand(
			RenderCommand.setColor(lrc.color), 
			RenderCommand.drawLine(lrc.a,lrc.b)
		);
	}
	
}
