package zuul;

import zuul.scene.Scene;

// TODO: Squash Editor and EditorFrame together (and maybe move Frame functionality to a ZuulWindow class. )
public class SceneEditor {
	private EditorFrame editorFrame;
	
//	private Room selectedRoom; TODO: replace with selectedEntity
	
	private Scene activeScene;
	
	public static final int EDITOR_SIZE = 768;
	
	public SceneEditor(Scene scene) {
		
		editorFrame = new EditorFrame();
		
		setActiveScene(scene);
//		selectedRoom = null;
	}
	
	public SceneEditor() {
		this(null);
	}
	
	public void start() {
		editorFrame.start();
	}
	
//	@Deprecated
//	public void setActiveLevel(Level l) {
//		lc.setActiveLevel(l);
//		activeLevel = l;
//	}
//	public Level getActiveLevel() { return activeLevel; }
	
	public void setActiveScene(Scene scene) {
		editorFrame.setActiveScene(scene);
		activeScene = scene;
	}
	public Scene getActiveScene() { return activeScene; }

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
}
