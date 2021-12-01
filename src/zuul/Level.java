package zuul;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlInput;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlMappingBuilder;
import com.amihaiemil.eoyaml.YamlPrinter;

public class Level {
	// Serialized
	private String name;
	private List<Room> rooms; // List of all rooms in the level.
	private Room spawnRoom; // Room to spawn in, start the player in.
	
	// Not serialized
	private Signal repaintSignal;
    private boolean loadingComplete; // Do not trigger repaints while this is true
	
	public Level(String name, boolean loadingComplete) {
		this.name = name;
		rooms = new ArrayList<Room>();
		
		this.loadingComplete = loadingComplete;
		repaintSignal = new Signal("Level repaint: " + name);
	}

	public Level(String name) {
		this(name, true);
	}
	
	public void completedLoading() {
		loadingComplete = true;
		repaint();
	}
	
	public void repaint() {
		if (loadingComplete)
			repaintSignal.run();
	}

	public void setSpawn(Room r) { // Checks if this level contains room first, then assigns spawn to room.
		if (rooms.contains(r)) {
			spawnRoom = r;
		} else throw new IllegalArgumentException("Level " + name + " does not contain the spawn room provided: " + r.getShortDescription());
	}
	
	public Room getSpawn() { // Return spawn room
		return spawnRoom;
	}
	
	public void add(Room r) {
		rooms.add(r);
		r.setLevel(this);
	}
	
	public void add(Room... rs) {
		for (int i=0;i<rs.length;i++) add(rs[i]);
	}

	public String getName() {
		return name;
	}
	
	private void setName(String name) {
		this.name = name;
	}
    
    public List<Room> getRooms() {
		return rooms;
	}
    
}
