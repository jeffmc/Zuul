package zuul.world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import zuul.math.Int2;
import zuul.util.Signal;

public class Level {
	// Serialized
	private String name;
	private List<Room> rooms; // List of all rooms in the level.
	private Room spawnRoom; // Room to spawn in, start the player in.
	
	// Not serialized
	private Signal repaintSignal;
    private boolean loadingComplete; // Do not trigger repaints while this is true
    private Set<Path> paths;
	
	public Level(String name, boolean loadingComplete) {
		this.name = name;
		rooms = new ArrayList<Room>();
		paths = new HashSet<Path>();
		
		this.loadingComplete = loadingComplete;
		repaintSignal = new Signal("Level repaint: " + name, false);
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
    
    public Room getRoom(String name) {
    	for (Room r : rooms) 
    		if (r.getName().equals(name)) return r;
    	return null;
    }
    
    public void add(Path p) {
    	paths.add(p);
    }
    
    public Set<Path> getPaths() {
    	return paths;
    }
    
    public Room getRoom(Int2 p) {
    	for (Room r : rooms) 
    		if (r.contains(p)) return r;
    	return null;
    }
    
}
