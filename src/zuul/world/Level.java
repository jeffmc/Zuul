package zuul.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import zuul.world.path.Path;

public class Level {
	
	private String name;
	private List<Room> rooms; // List of all rooms in the level.
	private Room spawnRoom; // Room to spawn in, start the player in.
	private Set<Path> paths;
	
	public Level(String name, boolean loadingComplete) {
		this.name = name;
		rooms = new ArrayList<Room>();
		paths = new HashSet<Path>();
	}

	public void calcExits() {
		for (Room r : rooms)
			r.calcExits();
	}
	
	public Level(String name) {
		this(name, true);
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
	}
	
	public void add(Room... rs) {
		for (int i=0;i<rs.length;i++) add(rs[i]);
	}

	public String getName() {
		return name;
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
}
