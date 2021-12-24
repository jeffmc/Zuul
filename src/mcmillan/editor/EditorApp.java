package mcmillan.editor;

import java.io.File;

import mcmillan.engine.core.Application;
import mcmillan.engine.debug.Profiler;

public class EditorApp extends Application {

	public EditorLayer editorLayer;
	
	public EditorApp(String[] args) {
		super("ZEngine Editor", args);

		editorLayer = new EditorLayer();
		
		pushLayer(editorLayer);
	}

	public static void main(String args[]) {
		Profiler.beginSession("App Construction", new File("results.json"));
		Application app = new EditorApp(args);
		app.run();
		Profiler.endSession();
	}
	
}
