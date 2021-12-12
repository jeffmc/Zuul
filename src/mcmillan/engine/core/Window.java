package mcmillan.engine.core;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import mcmillan.engine.math.Int2;
import mcmillan.engine.renderer.Renderer;

public class Window {

	public static final int CANVAS_SIZE = 768;

	private JFrame frame = new JFrame();
	public JFrame getFrame() { return frame; }
	private Canvas canvas = new Canvas();

	
	public enum DragType {
		CAM_MOVE,
		ROOM_MOVE,
	}
	private DragType dragType;
	private Int2 startDrag;
	
	private BufferStrategy bufferStrategy;
	
	public Window() {

		frame.setResizable(true);
		frame.setIgnoreRepaint(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				Application.instance().close();
			}
		});
		
		setupCanvas(new Dimension(CANVAS_SIZE,CANVAS_SIZE));
		frame.add(canvas);
		frame.pack();
		
		canvas.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				Renderer.setViewport(canvas.getWidth(), canvas.getHeight());
			}
		});
		
 		frame.pack(); // necessary to validate components for buffer functions below
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		
		// Set visible in center of screen
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void setTitle(String title) {
		frame.setTitle(title);
	}
	
	public void preUpdate() {
		Renderer.beginFrame();
	}
	
	public void postUpdate() {
		Renderer.endFrame();
		
		bufferStrategy.show();
		Graphics gg = bufferStrategy.getDrawGraphics();
		
		Renderer.drawFrame(gg);
		
		gg.dispose();
		bufferStrategy.show();
	}
	
	private void setupCanvas(Dimension size) {
		canvas.setMinimumSize(size);
		canvas.setPreferredSize(size);
		canvas.setMaximumSize(size);
	}
	
//	private void setupDragging() {
//		dragType = null;
//		// TODO: Refactor all mouse input into own class
//		canvas.addMouseMotionListener(new MouseMotionAdapter() {
//			@Override
//			public void mouseDragged(MouseEvent e) {
//				if (dragType != null) {
//					Point los = e.getLocationOnScreen();
//					Int2 nowDrag = new Int2(los.x, los.y);
//					Int2 delta = new Int2(startDrag);
//					delta.x -= nowDrag.x;
//					delta.y -= nowDrag.y;
//					switch (dragType) {
//					case ROOM_MOVE:
//						movingRoom.setPosition(Int2.sub(startRoom, delta));
//						break;
//					case CAM_MOVE:
//						camera.set(Int2.add(startCamera, delta));
//						break;
//					}
//				}
//			}
//		});
//		canvas.addMouseListener(new MouseAdapter() {
//			@Override public void mouseReleased(MouseEvent e) {
//				if (startDrag != null) {
//					Point los = e.getLocationOnScreen();
//					Int2 endDrag = new Int2(los.x, los.y);
//					Int2 delta = new Int2(startDrag);
//					delta.x -= endDrag.x;
//					delta.y -= endDrag.y;
//					switch(e.getButton()) {
//					case MouseEvent.BUTTON1:
//	//					if (movingRoom != null) { TODO: Gizmos
//	//						movingRoom.setPosition(Int2.sub(startRoom, delta));
//	//					}
//	//					dragType = null;
//	//					repaint();
//						break;
//					case MouseEvent.BUTTON2:
//						camera.set(Int2.add(startCamera, delta));
//						dragType = null;
//						break;
//					}
//					startDrag = null;
//				}
//			}
//			@Override public void mousePressed(MouseEvent e) {
//				switch(e.getButton()) {
//				case MouseEvent.BUTTON1:
////					movingRoom = editor.selectRoom(canvasCoordsToLevelCoords(e.getPoint())); TODO: Make gizmos
////					if (movingRoom != null) {
////						startRoom = new Int2(movingRoom.getPosition());
////						Point los = e.getLocationOnScreen();
////						startDrag = new Int2(los.x,los.y);
////						dragType = DragType.ROOM_MOVE;
////					}
//					break;
//				case MouseEvent.BUTTON2:
//					startCamera.set(camera);
//					Point los = e.getLocationOnScreen();
//					startDrag = new Int2(los.x,los.y);
//					dragType = DragType.CAM_MOVE;
//					break;
//				}
//			}
//		});
//	}
}
