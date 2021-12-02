package zuul;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import zuul.math.Int2;
import zuul.renderer.Renderer;
import zuul.world.Level;
import zuul.world.Room;

@SuppressWarnings("serial")
public class LevelCanvas extends JPanel {

	public enum DragType {
		CAM_MOVE,
		ROOM_MOVE,
	}

	private Color background;
	private Level activeLevel;

	private Int2 camera, lastCamera;
	private Point startDrag;
	
	private DragType dragType;
	private Room movingRoom;
	private Point startRoom;
	private Renderer renderer;
	
	public LevelCanvas(Level level, Dimension size, Color background, Renderer renderer) {
		super();
		this.background = background;
		this.renderer = renderer;
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		
		camera = new Int2();
		lastCamera = new Int2();
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
					lastCamera.set(camera);
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
	
	public LevelCanvas(Dimension size, Color background, Renderer renderer) {
		this(null, size, background, renderer);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
//		renderer.oldDraw(g, background, activeLevel, camera, movingRoom);
		renderer.drawElements(g, background, camera);
	}

	private Int2 canvasCoordsToLevelCoords(Point canvas) {
		Int2 lvl = new Int2(camera.x, camera.y); // TODO: Add Int2(Int2 clone) constructor
		lvl.x -= getWidth()/2-canvas.x;
		lvl.y -= getHeight()/2-canvas.y;
		return lvl;
	}

	public void setActiveLevel(Level l) {
		activeLevel = l;
		if (l != null) {
			// Center camera at spawn room
			Room s = activeLevel.getSpawn();
			camera.set(s.getX()+s.getWidth()/2,
					s.getY()+s.getHeight()/2);
			renderer.setLevel(l);
		} else {
			camera.set(0, 0);
		}
		repaint();
	}
	
	public void setBackground(Color c) {
		background = c;
		repaint();
	}
	
}
