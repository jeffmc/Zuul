package zuul;

import java.util.ArrayList;
import java.util.List;

public class Signal {
	private List<Runnable> listeners;
	private String debugLabel;
	
	public Signal(String debugLabel) {
		listeners = new ArrayList<>();
		this.debugLabel = debugLabel;
	}
	
	public Signal() {
		this(null);
	}
	
	public void addListener(Runnable r) {
		listeners.add(r);
	}
	
	public boolean removeListener(Runnable r) {
		return listeners.remove(r);
	}
	
	public void run() {
		if (debugLabel != null && debugLabel.length() > 0) System.err.println(debugLabel);
		for (Runnable r : listeners) {
			r.run();
		}
	}
}
