package zuul.world;

import java.util.HashSet;
import zuul.world.item.Item;
import zuul.world.path.Path;

// Level class
// Each instance of this class will contain all rooms/paths/items of the entire level.
// These rooms are now constructed using a ton of element constructors (new Room(), new Path(), new Item()) but 
// were originally constructed using deserialization from a file. TODO: Add serialization again.

public class Level {
	
	private String name;
	
	private HashSet<Room> rooms = new HashSet<Room>(); // List of all rooms in the level.
	private Room spawnRoom = null; // Room to spawn in, start the player in.
	
	private HashSet<Path> paths = new HashSet<Path>(); // All paths between rooms in this level.
	private HashSet<Item> items = new HashSet<Item>(); // All items contained in this level.
	
	private PlayerState.Conditional winCondition = null; // Level win condition.
	
	// Calculate exits for rooms in this level ( call after loading entire level, all rooms and paths. )
	public void calcExits() {
		for (Room r : rooms)
			r.calcExits();
	}
	
	// Construct level using string name.
	public Level(String name) {
		this.name = name;
	}
	
	 // Checks if this level contains room first, then assigns spawn to room.
	public void setSpawn(Room r) {
		if (rooms.contains(r)) {
			spawnRoom = r;
		} else throw new IllegalArgumentException("Level " + name + " does not contain the spawn room provided: " + r.getShortDescription());
	}
	
	// Get spawn room of this level
	public Room getSpawn() {
		return spawnRoom;
	}
	
	// Add a room(s) to this level.
	public void add(Room... rs) {
		for (int i=0;i<rs.length;i++) this.rooms.add(rs[i]);
	}

	// Get the name of this level.
	public String getName() {
		return name;
	}
    
	// Get all rooms in this level.
    public HashSet<Room> getRooms() {
		return rooms;
	}
    
    // Get a room by name string in this level
    public Room getRoom(String name) {
    	for (Room r : rooms) 
    		if (r.getName().equals(name)) return r;
    	return null;
    }
    
    // Add a path to level-wide set for this level.
    public void add(Path p) {
    	paths.add(p);
    }
    
    // Get all paths between rooms in this level
    public HashSet<Path> getPaths() {
    	return paths;
    }
    
    // Get level-wide set of items
    public HashSet<Item> getItems() {
    	return items;
    }
    
    // Add item to level-wide set
    public void add(Item... items) {
    	for (Item i : items)
    		this.items.add(i);
    }
    
    // Check if level contains item in any instance.
    public boolean hasItem(Item i) {
    	return items.contains(i);
    }

	// Allows any-case string to be parsed into a reference to an existing item within this inventory.items Set.
	public Item getItemFromString(String name) {
		for (Item i : items)
			if (i.getName().toLowerCase().equals(name.trim().toLowerCase()))
				return i;
		return null;
	}
	
	// Set level win condition.
	public void setWinCondition(PlayerState.Conditional wc) { this.winCondition = wc; }
	
	// Check if level has been beat using current player state.
	public boolean checkWin(PlayerState ps) {
		if (winCondition != null) {
			return winCondition.check(ps);
		}
		return false;
	}
	
}
