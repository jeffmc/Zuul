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
import zuul.math.IntTransform;
import zuul.renderer.Renderer;
import zuul.scene.Scene;
import zuul.world.Level;
import zuul.world.Room;

@SuppressWarnings("serial")
public class LevelCanvas extends JPanel { // TODO: Eliminate all repaint calls in favor of drawing loop.

	public enum DragType {
		CAM_MOVE,
		ROOM_MOVE,
	}

	private Editor editor;
	private Color background;
//	private Level activeLevel; TODO: Remove altogether
	private Scene activeScene;

	private Int2 camera, startCamera;
	
	private DragType dragType;
	private Int2 startDrag;
	
	private Room movingRoom;
	private Int2 startRoom;
	
	private Renderer renderer;
	
	public LevelCanvas(Editor editor, Scene scene, Dimension size, Color background, Renderer renderer) {
		super();
		this.editor = editor;
		this.background = background;
		this.renderer = renderer;
		this.setMinimumSize(size);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		
		camera = new Int2();
		startCamera = new Int2();
		setActiveScene(scene);
		
		dragType = null;
		// TODO: Refactor all mouse input into own class
		addMouseMotionListener(new MouseMotionListener() {
			@Override public void mouseMoved(MouseEvent e) { }
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if (dragType != null) {
					Point los = e.getLocationOnScreen();
					Int2 nowDrag = new Int2(los.x, los.y);
					Int2 delta = new Int2(startDrag);
					delta.x -= nowDrag.x;
					delta.y -= nowDrag.y;
					switch (dragType) {
					case ROOM_MOVE:
						movingRoom.setPosition(Int2.sub(startRoom, delta));
						repaint();
						break;
					case CAM_MOVE:
						camera.set(Int2.add(startCamera, delta));
						repaint();
						break;
					}
				}
			}
		});
		addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Point los = e.getLocationOnScreen();
				Int2 endDrag = new Int2(los.x, los.y);
				Int2 delta = new Int2(startDrag);
				delta.x -= endDrag.x;
				delta.y -= endDrag.y;
				switch(e.getButton()) {
				case MouseEvent.BUTTON1:
//					if (movingRoom != null) { TODO: Gizmos
//						movingRoom.setPosition(Int2.sub(startRoom, delta));
//					}
//					dragType = null;
//					repaint();
					break;
				case MouseEvent.BUTTON2:
					camera.set(Int2.add(startCamera, delta));
					dragType = null;
					repaint();
					break;
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				switch(e.getButton()) {
				case MouseEvent.BUTTON1:
//					movingRoom = editor.selectRoom(canvasCoordsToLevelCoords(e.getPoint())); TODO: Make gizmos
//					if (movingRoom != null) {
//						startRoom = new Int2(movingRoom.getPosition());
//						Point los = e.getLocationOnScreen();
//						startDrag = new Int2(los.x,los.y);
//						dragType = DragType.ROOM_MOVE;
//					}
					break;
				case MouseEvent.BUTTON2:
					startCamera.set(camera);
					Point los = e.getLocationOnScreen();
					startDrag = new Int2(los.x,los.y);
					dragType = DragType.CAM_MOVE;
					break;
				}
			}
			
			@Override public void mouseExited(MouseEvent e) {  }
			@Override public void mouseEntered(MouseEvent e) { }
			@Override public void mouseClicked(MouseEvent e) { }
		});
	}
	
	public LevelCanvas(Editor editor, Dimension size, Color background, Renderer renderer) {
		this(editor, null, size, background, renderer);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		renderer.drawElements(g, background, camera);
	}

	private Int2 canvasCoordsToLevelCoords(Point canvas) {
		Int2 lvl = new Int2(camera);
		lvl.x -= getWidth()/2-canvas.x;
		lvl.y -= getHeight()/2-canvas.y;
		return lvl;
	}

//	@Deprecated
//	public void setActiveLevel(Level l) {
//		activeLevel = l;
//		if (l != null) {
//			// Center camera at spawn room
//			Int2 spawn = activeLevel.getSpawn().getPosition();
//			camera.set(spawn.x, spawn.y);
//			renderer.setRenderables(l.getRenderables());
//		} else {
//			camera.set(0, 0);
//		}
//		repaint();
//	}
	
	public void setActiveScene(Scene scene) {
		activeScene = scene;
		if (scene != null) {
			renderer.setActiveScene(scene);
		}
		camera.set(0,0);
		repaint();
	}
	
	public void setBackground(Color c) {
		background = c;
		repaint();
	}
	
}
