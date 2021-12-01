package zuul;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

@SuppressWarnings("serial")
public class LevelCanvas extends Canvas {

	private Color bg;
	
	public LevelCanvas(Dimension size, Color background) {
		super();
		bg = background;
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(bg);
		Rectangle r = g.getClipBounds();
		g.fillRect(r.x, r.y, r.width, r.height);
	}
	
}
