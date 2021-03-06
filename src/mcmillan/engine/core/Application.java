package mcmillan.engine.core;

import java.util.Iterator;

import mcmillan.engine.debug.Profiler;
import mcmillan.engine.debug.Profiler.Scope;
import mcmillan.engine.zui.ZUILayer;

public abstract class Application {

	private static Application instance = null;
	public static Application instance() { return instance; }
	
	private LayerStack layerStack = new LayerStack();
	
	private Window window = new Window();
	public Window window() { return window; }
	
	private boolean running = false;
	
	private String[] cmdLineArgs;
	private int cmdLineArgCount;
	public int getCmdLineArgCount() { return cmdLineArgCount; }
	public String getCmdLineArg(int idx) { return cmdLineArgs[idx]; }
	
	private String name;
	public String getName() { return name; }

	public ZUILayer zuiLayer;
	
	private long lastTime = System.nanoTime();
	
	public Application(String name, String[] args) {
		if (instance != null) throw new IllegalStateException("There is already an Application instance!");
		instance = this;
		this.name = name.isBlank() ? "ZEngine App" : name;
		cmdLineArgs = args;
		cmdLineArgCount = cmdLineArgs.length;
		
		zuiLayer = new ZUILayer();

		pushOverlay(zuiLayer);
	}
	
//	public void onEvent(Event e) { }
	
	public void pushLayer(Layer layer) {
		layerStack.pushLayer(layer);
	}
	
	public void pushOverlay(Layer overlay) {
		layerStack.pushOverlay(overlay);
	}
	
	public void close() {
		running = false;
	}
	
	public void run() {
		running = true;
		while (running) { // TODO: Make a better framerate-locked loop (LayerStack in Application?)
			long time = System.nanoTime();
			long delta = time - lastTime;
			final long nspf = 1000000000 / 60;
			if (delta > nspf) {
				Profiler.Scope scope = Profiler.startScope();
				lastTime = System.nanoTime();
				Timestep ts = new Timestep(delta);
				
				Iterator<Layer> layers = layerStack.ascendingIterator();
				while (layers.hasNext()) {
					Layer layer = layers.next();
					layer.onUpdate(ts);
				}

				zuiLayer.begin();
				layers = layerStack.ascendingIterator();
				while (layers.hasNext()) {
					Layer layer = layers.next();
					layer.onZUIRender(ts);
				}
				zuiLayer.end();
				
				window.postUpdate();
				
				scope.stop();
			
			}
		}
		window.close();
	}
	
	
}
