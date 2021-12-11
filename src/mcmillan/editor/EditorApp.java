package mcmillan.editor;

import mcmillan.engine.core.Application;

public class EditorApp extends Application {

	public EditorLayer editorLayer = new EditorLayer();
	
	public EditorApp(String[] args) {
		super("ZEngine Editor", args);
		pushLayer(editorLayer);
	}

	public static void main(String args[]) {
		Application app = new EditorApp(args);
		app.run();
	}
	
}
