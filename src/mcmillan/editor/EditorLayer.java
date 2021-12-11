package mcmillan.editor;

import java.awt.Canvas;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.File;

import javax.swing.JColorChooser;
import javax.swing.JFrame;

import mcmillan.engine.core.Application;
import mcmillan.engine.core.Layer;
import mcmillan.engine.core.Timestep;
import mcmillan.engine.math.Int2;
import mcmillan.engine.math.IntTransform;
import mcmillan.engine.renderer.RenderCommand;
import mcmillan.engine.renderer.Renderer;
import zuul.Game;
import zuul.scene.BoxRendererComponent;
import zuul.scene.Entity;
import zuul.scene.LineRendererComponent;
import zuul.scene.Scene;
import zuul.scene.TransformComponent;
import zuul.util.LevelManager;
import zuul.world.Level;
import zuul.world.Room;

public class EditorLayer extends Layer {

	public static final int CANVAS_SIZE = 768;
	
	public enum DragType {
		CAM_MOVE,
		ROOM_MOVE,
	}

	private JFrame frame = new JFrame();
	private Canvas canvas = new Canvas();
	
	private BufferStrategy bufferStrategy;
	
	private Scene activeScene;
	private Entity selectedEntity = null;

	private Int2 camera, startCamera;
	
	private DragType dragType;
	private Int2 startDrag;
	
	private Room movingRoom;
	private Int2 startRoom;
	
	private MenuBar menuBar;
	
	private Color backgroundColor = new Color(20,20,20);
	private Color highlightColor = new Color(235,235,20);
	
	private SceneExplorerWindow sceneExplorer = null;
	private boolean sceneExplorerEnabled = true;
	private CheckboxMenuItem sceneExplorerBtn = new CheckboxMenuItem("Scene Explorer", sceneExplorerEnabled);
	public void disposedSceneExplorer() {
		sceneExplorer = null;
		sceneExplorerEnabled = false;
		sceneExplorerBtn.setState(sceneExplorerEnabled);
	}
	public void makeSceneExplorer() {
		sceneExplorer = new SceneExplorerWindow(EditorLayer.this, activeScene);
		sceneExplorerEnabled = true;
		sceneExplorerBtn.setState(sceneExplorerEnabled);
	}
	
	private EntityInspectorWindow entityInspector = null;
	private boolean entityInspectorEnabled = true;
	private CheckboxMenuItem entityInspectorBtn = new CheckboxMenuItem("Entity Inspector", sceneExplorerEnabled);
	public void disposedEntityInspector() { 
		entityInspector = null;
		entityInspectorEnabled = false;
		entityInspectorBtn.setState(entityInspectorEnabled);
	}
	public void makeEntityInspector() {
		entityInspector = new EntityInspectorWindow(EditorLayer.this, selectedEntity);
		entityInspectorEnabled = true;
		entityInspectorBtn.setState(entityInspectorEnabled);
	}
	
	public EditorLayer() {
		super("EditorLayer");
		Scene loadedScene = null;
		{
			Level level = LevelManager.load(new File("GenesisReadOnly.yaml"));
			LevelManager.save(level);
			
			loadedScene = Scene.levelToScene(level);
			
			if (level != null) {
				// Start cmd line game in own thread
				Game instance = new Game(level);
				new Thread(() -> instance.play()).start();
			}
		}
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // TODO: Implement for awt Frame
		frame.setResizable(true);
		frame.setIgnoreRepaint(true);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				Application.instance().close();
			}
		});
		
		setupMenubar();
		setupCanvas(new Dimension(CANVAS_SIZE,CANVAS_SIZE));
		frame.add(canvas);
		frame.pack();
		
		canvas.addComponentListener(new ComponentAdapter() {
			@Override public void componentResized(ComponentEvent e) {
				Renderer.setViewport(canvas.getWidth(), canvas.getHeight());
			}
		});
		
		camera = new Int2();
		startCamera = new Int2();
		setActiveScene(loadedScene);
		
		setupDragging();
		setupEditorWindows();
	}
	
	private void setupEditorWindows() {
		if (sceneExplorerEnabled) makeSceneExplorer();
		if (entityInspectorEnabled) makeEntityInspector();
	}
	
	private void setupMenubar() {
		menuBar = new MenuBar();
		frame.setMenuBar(menuBar);
		Menu fileMenu = new Menu("File");
		menuBar.add(fileMenu);
		{
			MenuItem openBtn = new MenuItem("Open Scene...", new MenuShortcut(KeyEvent.VK_O));
			fileMenu.add(openBtn);
			MenuItem saveBtn = new MenuItem("Save Scene", new MenuShortcut(KeyEvent.VK_S));
			fileMenu.add(saveBtn);
			MenuItem saveAsBtn = new MenuItem("Save Scene as...", new MenuShortcut(KeyEvent.VK_S, true));
			fileMenu.add(saveAsBtn);
			fileMenu.addSeparator();
			MenuItem exitBtn = new MenuItem("Exit", new MenuShortcut(KeyEvent.VK_E));
			fileMenu.add(exitBtn);
			exitBtn.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}});
		}
		Menu editMenu = new Menu("Edit");
		menuBar.add(editMenu);
		{
			MenuItem selectNoneBtn = new MenuItem("Select None", new MenuShortcut(KeyEvent.VK_A, true));
			selectNoneBtn.addActionListener((e)->selectEntity(null));
			editMenu.add(selectNoneBtn);
			editMenu.addSeparator();
			MenuItem changeHighlightBtn = new MenuItem("Change Hightlight Color...", new MenuShortcut(KeyEvent.VK_H));
			changeHighlightBtn.addActionListener((e)-> {
				Color newCol = JColorChooser.showDialog(frame, "Change Hightlight Color", highlightColor);
				if (newCol != null) highlightColor = newCol;
			});
			editMenu.add(changeHighlightBtn);
			MenuItem changeBackgroundBtn = new MenuItem("Change Background Color...", new MenuShortcut(KeyEvent.VK_B));
			changeBackgroundBtn.addActionListener((e)-> {
				Color newCol = JColorChooser.showDialog(frame, "Change Background Color", backgroundColor);
				if (newCol != null) backgroundColor = newCol;
			});
			editMenu.add(changeBackgroundBtn);
		}
		Menu viewMenu = new Menu("View");
		menuBar.add(viewMenu);
		{
			sceneExplorerBtn.addItemListener(new ItemListener() { @Override public void itemStateChanged(ItemEvent e) {
				sceneExplorerEnabled = sceneExplorerBtn.getState();
				if (sceneExplorerEnabled) {
					makeSceneExplorer();
				} else {
					sceneExplorer.dispose(); 
					sceneExplorer = null;
				}	
			}});
			viewMenu.add(sceneExplorerBtn);
			entityInspectorBtn.addItemListener(new ItemListener() { @Override public void itemStateChanged(ItemEvent e) {
				entityInspectorEnabled = entityInspectorBtn.getState();
				if (entityInspectorEnabled) {
					makeEntityInspector();
				} else {
					entityInspector.dispose(); 
					entityInspector = null;
				}	
			}});
			viewMenu.add(entityInspectorBtn);
		}
	}
	
	private void setupCanvas(Dimension size) {
		canvas.setMinimumSize(size);
		canvas.setPreferredSize(size);
		canvas.setMaximumSize(size);
	}
	
	private void setupDragging() {
		dragType = null;
		// TODO: Refactor all mouse input into own class
		canvas.addMouseMotionListener(new MouseMotionAdapter() {
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
		canvas.addMouseListener(new MouseAdapter() {
			@Override public void mouseReleased(MouseEvent e) {
				if (startDrag != null) {
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
					startDrag = null;
				}
			}
			@Override public void mousePressed(MouseEvent e) {
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
		});
	}

	@Override
	public void onAttach() {
		
		// Get bufferStrat
 		frame.pack(); // necessary to validate components for buffer functions below
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		
		// Set visible in center of screen
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	@Override
	public void onDetach() {
		System.out.println("Detached EditorLayer!");
		// TODO: Close cleanly
	}

	@Override
	public void onUpdate(Timestep ts) {
		bufferStrategy.show();
		Graphics gg = bufferStrategy.getDrawGraphics();
		if (gg != null) {
			Int2 viewport = Renderer.beginFrame();
			Renderer.submit(RenderCommand.fillRect(backgroundColor, new IntTransform(new Int2(), viewport)));
			activeScene.render(viewport, camera);
			
			if (selectedEntity != null) {
				TransformComponent tc = selectedEntity.getComponent(TransformComponent.class);
				LineRendererComponent lrc = selectedEntity.getComponentOrNull(LineRendererComponent.class);
				if (lrc != null) {
					Renderer.submit(RenderCommand.drawBox(highlightColor, boxSurrounding(lrc.a, lrc.b, 5)));
				}
				BoxRendererComponent brc = selectedEntity.getComponentOrNull(BoxRendererComponent.class);
				if (brc != null) {
					Renderer.submit(RenderCommand.drawBox(highlightColor, boxOffset(tc.transform, 5)));
				}
			}
			Renderer.endFrame();
			Renderer.drawFrame(gg);
			gg.dispose();
			bufferStrategy.show();
		}
	}

	@Override
	public void OnZUIRender() {
		ZUI.beginFrame();
		
	}
	
	private Int2 canvasCoordsToWorldCoords(Int2 canvasCoords) {
		Int2 lvl = new Int2(camera);
		lvl.x -= canvas.getWidth()/2-canvasCoords.x;
		lvl.y -= canvas.getHeight()/2-canvasCoords.y;
		return lvl;
	}

	public void selectEntity(Entity newSelection) { // TODO: Replace w/ selected entity
		selectedEntity = newSelection;
		if (sceneExplorer != null) sceneExplorer.setSelectedEntity(selectedEntity);
		if (entityInspector != null) entityInspector.setSelectedEntity(selectedEntity);
	}
	public Entity entityAtMouse(Int2 mouse) { 
		return activeScene.selectEntityAt(canvasCoordsToWorldCoords(mouse));
	}
	
	public void setActiveScene(Scene scene) {
		if (scene != null) {
			activeScene = scene;
		} else {
			activeScene = new Scene("Untitled Scene");
		}
		frame.setTitle("Zuul Editor - " + activeScene.getLabel());
		camera.set(0,0);
		if (sceneExplorer != null && activeScene != null)
			sceneExplorer.setActiveScene(activeScene);
	}
	
	private static IntTransform boxSurrounding(Int2 a, Int2 b, int offset) {
		return new IntTransform(
				(a.x+b.x)/2,
				(a.y+b.y)/2,
				Math.abs(a.x-b.x)+offset,
				Math.abs(a.y-b.y)+offset);
	}
	private static IntTransform boxSurrounding(Int2 a, Int2 b) {
		return boxSurrounding(a,b,0);
	}
	
	private static IntTransform boxOffset(IntTransform in, int offset) {
		IntTransform val = new IntTransform(in);
		val.scale.add(offset, offset);
		return val;
	}
}
