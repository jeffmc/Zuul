package zuul.util;

import java.util.ArrayList;
import java.util.List;

public class Signal {
	private List<Runnable> listeners;
	private String debugLabel;
	private boolean debugLog;
	
	public Signal(String debugLabel, boolean debugLog) {
		listeners = new ArrayList<>();
		this.debugLabel = debugLabel;
		this.debugLog = debugLog;
	}
	
	public Signal() {
		this(null, false);
	}
	
	public void addListener(Runnable r) {
		listeners.add(r);
	}
	
	public boolean removeListener(Runnable r) {
		return listeners.remove(r);
	}
	
	public void run() {
		if (debugLog && debugLabel != null && debugLabel.length() > 0) System.err.println(debugLabel);
		for (Runnable r : listeners) {
			r.run();
		}
	}
}
