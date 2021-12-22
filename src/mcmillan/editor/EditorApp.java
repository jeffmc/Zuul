package mcmillan.editor;

import mcmillan.engine.core.Application;

public class EditorApp extends Application {

	public EditorLayer editorLayer;
	
	public EditorApp(String[] args) {
		super("ZEngine Editor", args);
		
		editorLayer = new EditorLayer();
		
		pushLayer(editorLayer);
	}

	public static void main(String args[]) {
		Application app = new EditorApp(args);
		app.run();
	}
	
}
