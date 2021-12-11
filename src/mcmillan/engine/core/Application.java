package mcmillan.engine.core;

import java.util.Iterator;

public abstract class Application {

	private static Application instance = null;
	public static Application instance() { return instance; }
	
	private LayerStack layerStack = new LayerStack();
	
	private boolean running = false;
	
	private String[] cmdLineArgs;
	private int cmdLineArgCount;
	public int getCmdLineArgCount() { return cmdLineArgCount; }
	public String getCmdLineArg(int idx) { return cmdLineArgs[idx]; }
	
	private String name;
	public String getName() { return name; }

	private long lastTime = System.nanoTime();
	
	public Application(String name, String[] args) {
		if (instance != null) throw new IllegalStateException("There is already an Application instance!");
		instance = this;
		this.name = name.isBlank() ? "ZEngine App" : name;
		cmdLineArgs = args;
		cmdLineArgCount = cmdLineArgs.length;
	}
	
//	public void onEvent(Event e) { }
	
	public void pushLayer(Layer layer) {
		layerStack.pushLayer(layer);
	}
	
	public void pushOverlay(Layer overlay) {
		layerStack.pushOverlay(overlay);
	}
	
	public void close() {
		System.err.println("CLOSE!"); // TODO: Close cleanly
		System.exit(0);
	}
	
	public void run() {
		running = true;
		while (running) {
			while (true) { // TODO: Make a better framerate-locked loop (LayerStack in Application?)
				long time = System.nanoTime();
				long delta = time - lastTime;
				final long nspf = 1000000000 / 60;
				if (delta > nspf) {
					lastTime = System.nanoTime();
					Timestep ts = new Timestep(delta);
					Iterator<Layer> layers = layerStack.ascendingIterator();
					while (layers.hasNext()) {
						Layer layer = layers.next();
						layer.onUpdate(ts);
					}
				}
			}
		}
	}
	
	
}
