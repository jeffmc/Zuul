package zuul.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import zuul.math.Int2;
import zuul.math.IntTransform;
import zuul.renderer.Renderable.Shape;
import zuul.world.Level;
import zuul.world.Path;
import zuul.world.Room;
import zuul.world.TwoWayPath;

public class Renderer {
	
	public Set<Renderable> elements;
//	private Map<Renderable.Shape, Set<Renderable>> elementsByShape;
	private Rectangle bounds;
	
	public Renderer() {
		elements = new HashSet<>();
//		elementsByShape = new HashMap<>();
//		for (Renderable.Shape s : Renderable.Shape.values())
//			elementsByShape.put(s, new HashSet<>());
	}
	
	public void setLevel(Level l) { // TODO: Run this function better, implement the transform into Room object so that it correctly updates.
		elements.clear();
		for (Path p : l.getPaths()) {
			Room a = p.getA(), b = p.getB();
			int ax = a.getX()+a.getWidth()/2, ay = a.getY()+a.getHeight()/2;
			int bx = b.getX()+b.getWidth()/2, by = b.getY()+b.getHeight()/2;
			elements.add(new Renderable(
					Shape.LINE,
					Material.stroke(p instanceof TwoWayPath?Color.GREEN:Color.YELLOW),
					new IntTransform(new Int2(ax,ay), new Int2(bx-ax,by-ay))));
		}
		
		// Draw rooms
		for (Room r : l.getRooms()) {
			int rx = r.getX(), ry = r.getY();
			elements.add(new Renderable(
					Shape.BOX, 
					Material.stroke(r.isSpawnpoint()?Color.MAGENTA:Color.BLUE),
					new IntTransform(new Int2(rx, ry), new Int2(r.getWidth(), r.getHeight()))));
			
//			g.setColor(r.isSpawnpoint()?Color.PINK:Color.CYAN);
//			g.drawString(r.getName(), rx, ry); TODO: ADD TEXT SUPPORT TO NEW RENDERER!
		}
	}
	
	public void oldDraw(Graphics g, Color bg, Level activeLevel, Int2 camera, Room movingRoom) {
		// Get canvas bounds
		bounds = g.getClipBounds();
		
		// TODO: Add scaling
		// Fill background with background color specified in constructor.
		g.setColor(bg);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		// Draw 4x4 white rect at center of board
		g.setColor(Color.white);
		g.fillRect(bounds.width/2-2, bounds.height/2-2, 4, 4);
		
		if (activeLevel != null) {
			// Center canvas at camera coords
			centerAt(g, camera);
			
			// Draw paths
			for (Path p : activeLevel.getPaths()) {
				g.setColor(p instanceof TwoWayPath?Color.GREEN:Color.YELLOW);
				Room a = p.getA(), b = p.getB();
				g.drawLine(a.getX()+a.getWidth()/2, a.getY()+a.getHeight()/2,
						b.getX()+b.getWidth()/2, b.getY()+b.getHeight()/2);
			}
			
			// Draw rooms
			for (Room r : activeLevel.getRooms()) {
				int rx = r.getX(), ry = r.getY();
				// Draw room rectangle and label
				if (r == movingRoom) {
					g.setColor(Color.RED);
					g.fillRect(rx, ry, r.getWidth(), r.getHeight());
				}
				g.setColor(r.isSpawnpoint()?Color.MAGENTA:Color.BLUE);
				g.drawRect(rx, ry, r.getWidth(), r.getHeight());
				g.setColor(r.isSpawnpoint()?Color.PINK:Color.CYAN);
				g.drawString(r.getName(), rx, ry);
			}
		}
	}
	
	public void drawElements(Graphics g, Color bg, Int2 camera) {
		// Get canvas bounds
		bounds = g.getClipBounds();
		
		// TODO: Add scaling
		// Fill background with background color specified in constructor.
		g.setColor(bg);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		// Draw 4x4 white rect at center of board
		g.setColor(Color.white);
		g.fillRect(bounds.width/2-2, bounds.height/2-2, 4, 4);
		
		// Center canvas at camera coords
		centerAt(g, camera);
		
		for (Renderable e : elements) 
			drawElement(g, e);
		
	}
	
	private void drawElement(Graphics g, Renderable r) {
		Material m = r.material;
		Int2 pos = r.transform.position, scale = r.transform.scale;
		switch (r.shape) {
		case BOX:
			if (m.fill != null) {
				g.setColor(m.fill);
				g.fillRect(pos.x, pos.y, scale.x, scale.y);
			}
			if (m.stroke != null) {
				g.setColor(m.stroke);
				g.drawRect(pos.x, pos.y, scale.x, scale.y);
			}
			break;
		case LINE: // Fill not supported for LINE
			if (m.stroke != null) {
				g.setColor(m.stroke);
				g.drawLine(pos.x, pos.y, pos.x+scale.x, pos.y+scale.y);
			}
			break;
		case ELLIPSE:
			if (m.fill != null) {
				g.setColor(m.fill);
				g.fillOval(pos.x, pos.y, scale.x, scale.y);
			}
			if (m.stroke != null) {
				g.setColor(m.stroke);
				g.drawOval(pos.x, pos.y, scale.x, scale.y);
			}
			break;
		}
	}
	
	public void submit(Renderable r) {
		elements.add(r);
//		Set<Renderable> byShape = elementsByShape.get(r.shape);
//		if (byShape == null) throw new IllegalArgumentException("Shape renderable set not found in elementsByShape map!");
//		byShape.add(r);
	}
	
	private void centerAt(Graphics g, Int2 center) {
		g.translate(-center.x+bounds.width/2,-center.y+bounds.height/2);
	}
}
