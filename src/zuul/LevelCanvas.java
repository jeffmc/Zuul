package zuul;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

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
		g.setColor(bg);
		Rectangle rect = g.getClipBounds();
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		if (level != null) {
			for (Room room : level.getRooms()) {
				int rx = room.getX(), ry = room.getY(), rw = room.getWidth(), rh = room.getHeight();
				
				// Draw rect
				g.setColor(Color.RED);
				g.drawRect(rx, ry, rw, rh);
				
				// Draw label
				g.setColor(Color.YELLOW);
				g.drawString(room.getName(), rx, ry);
				
				// Connect exits
				g.setColor(Color.GREEN);
				int rcx = rx + rw/2, rcy = ry + rh/2;
				for (Room exit : room.getExits().values()) { // TODO: Don't draw all lines twice
					int ex = exit.getX(), ey = exit.getY(), ew = exit.getWidth(), eh = exit.getHeight();
					int ecx = ex + ew/2, ecy = ey + eh/2;
					g.drawLine(rcx, rcy, ecx, ecy);
				}
			}
		}
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
