package zuul;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import zuul.world.Level;
import zuul.world.Path;
import zuul.world.Room;
import zuul.world.TwoWayPath;

@SuppressWarnings("serial")
public class LevelCanvas extends Canvas {

	private Color bg;
	private Level level;
	
	public LevelCanvas(Level level, Dimension size, Color background) {
		super();
		this.level = level;
		bg = background;
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
	}
	
	public LevelCanvas(Dimension size, Color background) {
		this(null, size, background);
	}
	
	@Override
	public void paint(Graphics g) {
		// Fill background with background color specified in constructor.
		g.setColor(bg);
		Rectangle size = g.getClipBounds();
		g.fillRect(size.x, size.y, size.width, size.height);

		// Draw 4x4 white rect at center of board
		g.setColor(Color.white);
		g.fillRect(size.width/2-2, size.height/2-2, 4, 4);
		
		if (level != null) {
			// Center canvas at spawn room's center
			Room s = level.getSpawn();
			centerAt(g, s.getX()+s.getWidth()/2,s.getY()+s.getHeight()/2);

			// Draw paths
			for (Path p : level.getPaths()) {
				g.setColor(p instanceof TwoWayPath?Color.GREEN:Color.YELLOW);
				Room a = p.getA(), b = p.getB();
				g.drawLine(a.getX()+a.getWidth()/2, a.getY()+a.getHeight()/2,
						b.getX()+b.getWidth()/2, b.getY()+b.getHeight()/2);
			}
			
			// Draw rooms
			for (Room r : level.getRooms()) {
				int rx = r.getX(), ry = r.getY();
				// Draw room rectangle and label
				g.setColor(r.isSpawnpoint()?Color.MAGENTA:Color.BLUE);
				g.drawRect(rx, ry, r.getWidth(), r.getHeight());
				g.setColor(r.isSpawnpoint()?Color.PINK:Color.CYAN);
				g.drawString(r.getName(), rx, ry);
			}
		}
	}

	private void centerAt(Graphics g, int scx, int scy) {
		g.translate(-scx+this.getWidth()/2,-scy+this.getHeight()/2);
		
	}

	public void setActiveLevel(Level l) {
		level = l;
		repaint();
	}
	
	public void setBackground(Color c) {
		bg = c;
		repaint();
	}
	
}
