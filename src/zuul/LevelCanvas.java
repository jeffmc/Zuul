package zuul;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import zuul.world.Level;
import zuul.world.Path;
import zuul.world.Room;
import zuul.world.TwoWayPath;

@SuppressWarnings("serial")
public class LevelCanvas extends JPanel {

	private Color bg;
	private Level level;
	private int cameraX, cameraY;
	private int lastCameraX, lastCameraY;
	
	private Point endDrag;
	private Point startDrag;
	
	public LevelCanvas(Level level, Dimension size, Color background) {
		super();
		bg = background;
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		setActiveLevel(level);
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				System.out.println("HERE");
				Point nowDrag = e.getLocationOnScreen();
				Point delta = new Point(startDrag);
				delta.x -= nowDrag.x;
				delta.y -= nowDrag.y;
				cameraX = lastCameraX + delta.x;
				cameraY = lastCameraY + delta.y;
				repaint();
			}
		});
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton()==MouseEvent.BUTTON2) {
					endDrag = e.getLocationOnScreen();
					Point delta = new Point(startDrag);
					delta.x -= endDrag.x;
					delta.y -= endDrag.y;
					cameraX = lastCameraX + delta.x;
					cameraY = lastCameraY + delta.y;
					repaint();
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton()==MouseEvent.BUTTON2) {
					lastCameraX = cameraX;
					lastCameraY = cameraY;
					startDrag = e.getLocationOnScreen();
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) { 
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
	}
	
	public LevelCanvas(Dimension size, Color background) {
		this(null, size, background);
	}
	
	public void changeCameraPosition(int dx, int dy) {
		cameraX += dx;
		cameraY += dy;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// TODO: Add scaling
		// Fill background with background color specified in constructor.
		g.setColor(bg);
		Rectangle size = g.getClipBounds();
		g.fillRect(size.x, size.y, size.width, size.height);

		// Draw 4x4 white rect at center of board
		g.setColor(Color.white);
		g.fillRect(size.width/2-2, size.height/2-2, 4, 4);
		
		if (level != null) {
			// Center canvas at camera coords
			centerAt(g, cameraX, cameraY);

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

	private void centerAt(Graphics g, int centerX, int centerY) {
		g.translate(-centerX+this.getWidth()/2,-centerY+this.getHeight()/2);
		
	}

	public void setActiveLevel(Level l) {
		level = l;
		if (l != null) {
			Room s = level.getSpawn();
			cameraX = s.getX()+s.getWidth()/2;
			cameraY = s.getY()+s.getHeight()/2;
		} else {
			cameraX = 0;
			cameraY = 0;
		}
		repaint();
	}
	
	public void setBackground(Color c) {
		bg = c;
		repaint();
	}
	
}
