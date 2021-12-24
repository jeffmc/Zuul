package mcmillan.engine.renderer;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.font.LineMetrics;

import mcmillan.engine.math.Int2;
import mcmillan.engine.math.IntTransform;
import mcmillan.engine.scene.BoxRendererComponent;
import mcmillan.engine.scene.LineRendererComponent;
import mcmillan.engine.scene.TransformComponent;

public class RenderCommand implements Renderable {
	
	public static Class<Graphics> gfxcls = Graphics.class;
	
	private Renderable[] cmds;
	public int getLength() { return cmds != null ? cmds.length : 0; }
	
	public RenderCommand(Renderable...cmds) {
		this.cmds = cmds;
	}
	
	@Override
	public void render(Graphics g) {
		if (cmds != null)
			for (Renderable r : cmds)
				r.render(g);
	}

	public static Renderable setColor(Color c) {
		return new Renderable() {
			@Override
			public void render(Graphics g) {
				g.setColor(c);
			}
		};
	}
	
	// Rect
	public static Renderable fillRect(IntTransform t) {
		return new Renderable() {
			@Override
			public void render(Graphics g) {
				g.fillRect(t.position.x,t.position.y,
									t.scale.x,t.scale.y);
			}
		};
	}
	public static RenderCommand fillRect(Color c, IntTransform t) {
		return new RenderCommand(RenderCommand.setColor(c), RenderCommand.fillRect(t));
	}
	public static RenderCommand background(Color c) {
		return fillRect(c, new IntTransform(new Int2(), Renderer.viewport()));
	}
	
	public static Renderable drawRect(IntTransform t) {
		return new Renderable() {
			@Override
			public void render(Graphics g) {
				g.drawRect(t.position.x,t.position.y,
						t.scale.x,t.scale.y);
			}
		};
	}
	public static RenderCommand drawRect(Color c, IntTransform t) {
		return new RenderCommand(RenderCommand.setColor(c), RenderCommand.drawRect(t));
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
	public static Renderable drawBox(IntTransform ot) {
		IntTransform t = defineByUpperLeft(ot);
		return RenderCommand.drawRect(new IntTransform(t.position, t.scale));
	}
	public static RenderCommand drawBox(Color c, IntTransform t) {
		return new RenderCommand(RenderCommand.setColor(c), RenderCommand.drawBox(t)); 
	}
	
	public static Renderable fillBox(IntTransform ot) {
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
	public static Renderable drawLine(Int2 a, Int2 b) {
		return new Renderable() {
			@Override
			public void render(Graphics g) {
				g.drawLine(a.x, a.y, b.x, b.y);
			}
		};
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
	public static Renderable drawString(String s, Int2 p) {
		return new Renderable() {
			@Override
			public void render(Graphics g) {
				g.drawString(s, p.x, p.y);
			}
		};
	}
	public static RenderCommand drawString(Color c, String s, Int2 p) {
		return new RenderCommand(RenderCommand.setColor(c), RenderCommand.drawString(s, p));
	}
	
	public static Renderable text(String txt, Int2 pos, TextAnchor horizAnchor, TextAnchor vertAnchor) {
		Int2 offset = textBaselineOffset(txt, horizAnchor, vertAnchor);
		return RenderCommand.drawString(txt, Int2.add(pos, offset));
	}
	public static RenderCommand text(Color c, String txt, Int2 pos, TextAnchor horizAnchor, TextAnchor vertAnchor) {
		return new RenderCommand(
				RenderCommand.setColor(c),
				RenderCommand.text(txt, pos, horizAnchor, vertAnchor));
	}
	public static Renderable textBounding(String txt, Int2 pos, TextAnchor horizAnchor, TextAnchor vertAnchor) {
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
	
	// Image
	public static Renderable image(Int2 pos, Image image) {
		return new Renderable() {
			@Override
			public void render(Graphics g) {
				g.drawImage(image, pos.x, pos.y, null);
			}
		};
	}

	// Translation
	@Deprecated
	public static Renderable translate(int x, int y) {
		return new Renderable() {
			@Override
			public void render(Graphics g) {
				g.translate(x, y);
			}
		};
	}
	@Deprecated
	public static Renderable translate(Int2 translation) {
		return RenderCommand.translate(translation.x, translation.y);
	}
	public static Renderable pushTranslation(Int2 translation) {
		return new Renderable() {
			@Override
			public void render(Graphics g) {
				Renderer.pushTranslation(g, translation);
			}
		};
	}
	public static Renderable pushTranslation(int x, int y) {
		return pushTranslation(new Int2(x,y));
	}
	public static Renderable popTranslation() {
		return new Renderable() {
			@Override
			public void render(Graphics g) {
				Renderer.popTranslation(g);
			}
		};
	}
	
	// Clipping
	public static Renderable pushClipRect(IntTransform clip) {
		return new Renderable() {
			@Override
			public void render(Graphics g) {
				Renderer.pushClipRect(g, clip);
			}
		};
	}
	
	public static Renderable popClipRect() {
		return new Renderable() {
			@Override
			public void render(Graphics g) {
				Renderer.popClipRect(g);
			}
		};
	}
}
