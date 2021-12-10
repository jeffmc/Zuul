package zuul;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import zuul.math.Int2;
import zuul.renderer.Renderer;
import zuul.scene.Scene;
import zuul.world.Room;

public class Editor {

	public static final int CANVAS_SIZE = 768;
	
	public enum DragType {
		CAM_MOVE,
		ROOM_MOVE,
	}

	private Frame frame;
	private Canvas canvas;
	
	private BufferStrategy bufferStrategy;
	
	private Scene activeScene;

	private Int2 camera, startCamera;
	
	private DragType dragType;
	private Int2 startDrag;
	
	private Room movingRoom;
	private Int2 startRoom;
	
	public Editor(Scene scene) {
		frame = new Frame("Zuul Editor");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setIgnoreRepaint(true);
		
		setupCanvas(new Dimension(CANVAS_SIZE,CANVAS_SIZE));
		frame.add(canvas);
		frame.pack();
		
		camera = new Int2();
		startCamera = new Int2();
		setActiveScene(scene);
		
		setupDragging();
	}
	
	private void setupCanvas(Dimension size) {
		canvas = new Canvas();
		canvas.setMinimumSize(size);
		canvas.setPreferredSize(size);
		canvas.setMaximumSize(size);
	}
	
	
	private void setupDragging() {
		dragType = null;
		// TODO: Refactor all mouse input into own class
		canvas.addMouseMotionListener(new MouseMotionListener() {
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
						break;
					case CAM_MOVE:
						camera.set(Int2.add(startCamera, delta));
						break;
					}
				}
			}
		});
		canvas.addMouseListener(new MouseListener() {
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
	
	public Editor() {
		this(null);
	}
	
	public void start() {
		// Get bufferStrat
 		frame.pack(); // necessary to validate components for buffer functions below
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		
		// Set visible in center of screen
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		// Begin game loop
		long lastTime = System.nanoTime();
		while (true) { // TODO: Make a better framerate-locked loop (LayerStack in Application?)
			long time = System.nanoTime();
			long delta = time - lastTime;
			final long nspf = 1000000000 / 60;
			if (delta > nspf) {
				lastTime = time;
				System.out.println("EditorFrame: frameTime: " + delta/1000000.0f + "ms");
	
				bufferStrategy.show();
				Graphics gg = bufferStrategy.getDrawGraphics();
				if (gg != null) {
					int vw = Renderer.viewportWidth(), vh = Renderer.viewportHeight();
					gg.setColor(Color.ORANGE);
					gg.fillRect(0, 0, vw, vh);
					activeScene.render(camera);
					activeScene.draw(gg);
					gg.dispose();
					bufferStrategy.show();
				}
			}
		}
	}
	
	private Int2 canvasCoordsToLevelCoords(Point canvasCoords) {
		Int2 lvl = new Int2(camera);
		lvl.x -= canvas.getWidth()/2-canvasCoords.x;
		lvl.y -= canvas.getHeight()/2-canvasCoords.y;
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


//	public Room selectRoom(Room newSelection) { TODO: Replace w/ selected entity
//		if (selectedRoom != null)
//			selectedRoom.getRenderable().material.fill = null;
//		selectedRoom = newSelection;
//		if (selectedRoom != null)
//			selectedRoom.getRenderable().material.fill = Color.RED;
//		return selectedRoom;
//	}
//	public Room selectRoom(Int2 worldCoords) { 
//		return selectRoom(activeLevel.getRoom(worldCoords));
//	}
	public void setActiveScene(Scene scene) {
		activeScene = scene;
		camera.set(0,0);
	}
	
}
