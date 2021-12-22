package mcmillan.engine.renderer;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import mcmillan.engine.math.Int2;
import mcmillan.engine.math.IntTransform;
import mcmillan.engine.scene.BoxRendererComponent;
import mcmillan.engine.scene.LineRendererComponent;
import mcmillan.engine.scene.TransformComponent;

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
	
	// Rect
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
	public static RenderCommand fillRect(Color c, IntTransform t) {
		return new RenderCommand(RenderCommand.setColor(c), RenderCommand.fillRect(t));
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
	public static RenderCommand drawRect(Color c, IntTransform t) {
		return new RenderCommand(RenderCommand.setColor(c), RenderCommand.drawRect(t));
	}

	// Translation
	public static RenderCommand translate(int x, int y) {
		try {
			Method m = gfxcls.getMethod("translate", int.class, int.class);
			return new RenderCommand(m, x, y);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
	public static RenderCommand translate(Int2 translation) {
		return RenderCommand.translate(translation.x, translation.y);
	}
	public static RenderCommand centerAt(Int2 center, Int2 viewport) {
		return RenderCommand.translate(
					-center.x+viewport.x/2,
					-center.y+viewport.y/2);
	}
	
	// Box
	public static RenderCommand box(TransformComponent tc, BoxRendererComponent brc) {
		Material m = brc.material;
		IntTransform t = defineByUpperLeft(tc.transform);
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
	public static RenderCommand drawBox(IntTransform ot) {
		IntTransform t = defineByUpperLeft(ot);
		return RenderCommand.drawRect(new IntTransform(t.position, t.scale));
	}
	public static RenderCommand drawBox(Color c, IntTransform t) {
		return new RenderCommand(RenderCommand.setColor(c), RenderCommand.drawBox(t)); 
	}
	
	public static RenderCommand fillBox(IntTransform ot) {
		IntTransform t = defineByUpperLeft(ot);
		return RenderCommand.fillRect(new IntTransform(t.position, t.scale));
	}
	public static RenderCommand fillBox(Color c, IntTransform t) {
		return new RenderCommand(RenderCommand.setColor(c), RenderCommand.fillBox(t)); 
	}
	public static IntTransform defineByUpperLeft(IntTransform transform) {
		IntTransform t = new IntTransform(transform); // New transform defined by upper-left coordinate for drawing
		t.position.sub(Int2.div(t.scale, 2));
		return t;
	}
	
	// Line
	public static RenderCommand drawLine(Int2 a, Int2 b) {
		try {
			Method m = gfxcls.getMethod("drawLine", int.class, int.class, int.class, int.class);
			return new RenderCommand(m,
					a.x,a.y,b.x,b.y);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
	public static RenderCommand drawLine(Color c, Int2 a, Int2 b) {
		return new RenderCommand(RenderCommand.setColor(c), RenderCommand.drawLine(a, b));
	}
	public static RenderCommand line(LineRendererComponent lrc) {
		return new RenderCommand(
			RenderCommand.setColor(lrc.color), 
			RenderCommand.drawLine(lrc.a,lrc.b)
		);
	}

	// Text
	public static RenderCommand drawString(String s, Int2 p) {
		try {
			Method m = gfxcls.getMethod("drawString", String.class, int.class, int.class);
			// Store current value of passed string.
			return new RenderCommand(m, s, p.x, p.y); 
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
	public static RenderCommand drawString(Color c, String s, Int2 p) {
		return new RenderCommand(RenderCommand.setColor(c), RenderCommand.drawString(s, p));
	}
	
	public static RenderCommand text(String txt, Int2 pos, TextAnchor horizAnchor, TextAnchor vertAnchor) {
		Int2 offset = textBaselineOffset(txt, horizAnchor, vertAnchor);
		return RenderCommand.drawString(txt, Int2.add(pos, offset));
	}
	public static RenderCommand text(Color c, String txt, Int2 pos, TextAnchor horizAnchor, TextAnchor vertAnchor) {
		return new RenderCommand(
				RenderCommand.setColor(c),
				RenderCommand.text(txt, pos, horizAnchor, vertAnchor));
	}
	public static RenderCommand textBounding(String txt, Int2 pos, TextAnchor horizAnchor, TextAnchor vertAnchor) {
		FontMetrics fm = Renderer.getFontMetrics();
		Rectangle bounds = fm.getStringBounds(txt, null).getBounds();
		Int2 offset = textBaselineOffset(txt, horizAnchor, vertAnchor);

		IntTransform rect = new IntTransform(bounds.x, bounds.y, bounds.width, bounds.height);
		rect.position.add(pos).add(offset);
		return drawRect(rect);
	}
	public static RenderCommand textBounding(Color c, String txt, Int2 pos, TextAnchor horizAnchor, TextAnchor vertAnchor) {
		return new RenderCommand(
				RenderCommand.setColor(c),
				RenderCommand.textBounding(txt, pos, horizAnchor, vertAnchor));
	}
	public static Int2 textBaselineOffset(String txt, TextAnchor horizAnchor, TextAnchor vertAnchor) {
		FontMetrics fm = Renderer.getFontMetrics();
		Int2 offset = new Int2();
		Rectangle bounds = fm.getStringBounds(txt, null).getBounds();
		LineMetrics lm = fm.getLineMetrics(txt, null);
		switch (horizAnchor) {
		case LEFT:
			offset.x = 0;
			break;
		case RIGHT:
			offset.x = -bounds.width;
			break;
		case CENTER: 
			offset.x = -bounds.width/2;
			break;
		case BASELINE:
		case TOP:
		case BOTTOM:
		default:
			throw new IllegalArgumentException(horizAnchor.name() + " not a valid horizontal text anchor!");
		}
		switch (vertAnchor) {
		case BASELINE:
			offset.y = 0;
			break;
		case TOP:
			offset.y = (int)lm.getAscent();
			break;
		case BOTTOM: 
			offset.y = (int)-lm.getDescent();
			break;
		case CENTER: 
			offset.y = (int)(lm.getHeight()/2-lm.getDescent());
			break;
		case LEFT:
		case RIGHT:
		default:
			throw new IllegalArgumentException(vertAnchor.name() + " not a valid vertical text anchor!");
		}
		return offset;
	}
	public static RenderCommand image(Int2 pos, Image image) {
		try {
			Method m = gfxcls.getMethod("drawImage", Image.class, int.class, int.class, ImageObserver.class);
			return new RenderCommand(m,
					image, pos.x, pos.y, null);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
