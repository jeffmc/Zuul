package mcmillan.engine.core;

import java.util.Iterator;
import java.util.LinkedList;

public class LayerStack {
	private LinkedList<Layer> layers = new LinkedList<Layer>();
	public int layerCount() { return layers.size(); }
	
	private int layerInsertIndex = 0;
	
	public void pushLayer(Layer layer) {
		layers.add(layerInsertIndex, layer);
		layerInsertIndex++;
		layer.onAttach();
	}
	public void popLayer(Layer layer) {
		int idx = layers.indexOf(layer);
		if (idx < 0) throw new IllegalArgumentException(layer.toString() + " not found in stack!");
		if (idx >= layerInsertIndex) throw new IllegalArgumentException(layer.toString() + " found after layer/overlay seperator! [" + idx + "]");
		layer.onDetach();
		layers.remove(idx);
		layerInsertIndex--;
	}
	
	public void pushOverlay(Layer overlay) {
		layers.add(overlay);
		overlay.onAttach();
	}
	public void popOverlay(Layer overlay) {
		int idx = layers.indexOf(overlay);
		if (idx < 0) throw new IllegalArgumentException(overlay.toString() + " not found in stack!");
		if (idx < layerInsertIndex) throw new IllegalArgumentException(overlay.toString() + " found before layer/overlay seperator! [" + idx + "]");
		overlay.onDetach();
		layers.remove(idx);
	}
	
	public Iterator<Layer> ascendingIterator() {
		return layers.iterator();
	}
	
	public Iterator<Layer> descendingIterator() {
		return layers.descendingIterator();
	}
	
	public void print() {
		System.out.println("LayerStack:");
		System.out.println("  Layers:");
		for (int i=0;i<layerInsertIndex;i++) {
			System.out.println("    " + layers.get(i).toString());
		}
		System.out.println("  Overlays:");
		for (int i=0;i<layerInsertIndex;i++) {
			System.out.println("    " + layers.get(i).toString());
		}
	}
}
