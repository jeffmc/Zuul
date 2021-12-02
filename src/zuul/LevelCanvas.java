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

	public enum DragType {
		CAM_MOVE,
		ROOM_MOVE,
	}

	private Color bg;
	private Level activeLevel;

	private Point camera, lastCamera;
	private Point startDrag;
	
	private DragType dragType;
	private Room movingRoom;
	private Point startRoom;
	
	public LevelCanvas(Level level, Dimension size, Color background) {
		super();
		bg = background;
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		
		camera = new Point();
		lastCamera = new Point();
		setActiveLevel(level);
		
		dragType = null;
		// TODO: Refactor all mouse input
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if (dragType != null) {
					Point nowDrag, delta;
					switch (dragType) {
					case ROOM_MOVE:
						nowDrag = e.getLocationOnScreen();
						delta = new Point(startDrag);
						delta.x -= nowDrag.x;
						delta.y -= nowDrag.y;
						movingRoom.setPosition(
								startRoom.x - delta.x, 
								startRoom.y - delta.y);
						repaint();
						break;
					case CAM_MOVE:
						nowDrag = e.getLocationOnScreen();
						delta = new Point(startDrag);
						delta.x -= nowDrag.x;
						delta.y -= nowDrag.y;
						camera.x = lastCamera.x + delta.x;
						camera.y = lastCamera.y + delta.y;
						repaint();
						break;
					}
				}
			}
		});
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Point endDrag, delta;
				switch(e.getButton()) {
				case MouseEvent.BUTTON1:
					endDrag = e.getLocationOnScreen();
					delta = new Point(startDrag);
					delta.x -= endDrag.x;
					delta.y -= endDrag.y;
					if (movingRoom != null) {
						movingRoom.setPosition(
								startRoom.x - delta.x, 
								startRoom.y - delta.y);
					}
					dragType = null;
					repaint();
					break;
				case MouseEvent.BUTTON2:
					endDrag = e.getLocationOnScreen();
					delta = new Point(startDrag);
					delta.x -= endDrag.x;
					delta.y -= endDrag.y;
					camera.x = lastCamera.x + delta.x;
					camera.y = lastCamera.y + delta.y;
					dragType = null;
					repaint();
					break;
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				switch(e.getButton()) {
				case MouseEvent.BUTTON1:
					movingRoom = activeLevel.getRoom(
							canvasCoordsToLevelCoords(e.getPoint()));
					if (movingRoom != null) {
						startRoom = new Point(movingRoom.getX(),movingRoom.getY());
						startDrag = e.getLocationOnScreen();
						dragType = DragType.ROOM_MOVE;
					}
					break;
				case MouseEvent.BUTTON2:
					lastCamera.setLocation(camera);
					startDrag = e.getLocationOnScreen();
					dragType = DragType.CAM_MOVE;
					break;
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

	private Point canvasCoordsToLevelCoords(Point canvas) {
		Point lvl = new Point(camera);
		lvl.x -= getWidth()/2-canvas.x;
		lvl.y -= getHeight()/2-canvas.y;
		return lvl;
	}
	
	private void centerAt(Graphics g, Point center) {
		g.translate(-center.x+this.getWidth()/2,-center.y+this.getHeight()/2);
		
	}

	public void setActiveLevel(Level l) {
		activeLevel = l;
		if (l != null) {
			// Center camera at spawn room
			Room s = activeLevel.getSpawn();
			camera.setLocation(s.getX()+s.getWidth()/2,
					s.getY()+s.getHeight()/2);
		} else {
			camera.setLocation(0, 0);
		}
		repaint();
	}
	
	public void setBackground(Color c) {
		bg = c;
		repaint();
	}
	
}
