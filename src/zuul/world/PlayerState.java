package zuul.world;

import java.util.HashSet;

import zuul.world.item.Inventory;
import zuul.world.item.Item;
import zuul.world.path.Path;

// PlayerState contains all player data; 
// the item inventory, player-explored paths and rooms, player location (Room)

public class PlayerState {
	private HashSet<Path> exploredPaths = new HashSet<>(); // All player-explored paths
	private HashSet<Room> exploredRooms = new HashSet<>(); // All player-explored rooms
	
	private Room location = null; // Room that the player is in.
	
	private Inventory inventory; // Player's inventory
	public Inventory getInventory() { return inventory; } // Getter
	
	// Constructor for player, specify spawn room.
	public PlayerState(Room spawn) {
		inventory = new Inventory(spawn.getLevel(), true, new Item[]{});
		goTo(spawn);
	}
	
	// Mark a path as explored by player
	public void traversedPath(Path p) {
		assert p != null;
		exploredPaths.add(p);
	}
	
	// Go to a room, marks room as explored by player.
	public void goTo(Room r) {
		assert r != null;
		exploredRooms.add(r);
		location = r;
	}
	
	// Get the room which the player currently resides.
	public Room getLocation() {
		return location;
	}
	
	// Quicker helper method
	public boolean hasItem(Item i) {
		return inventory.hasItem(i);
	}

	// Checking a condition of this inventory, used for win and conditional paths.
	public interface Conditional {
		boolean check(PlayerState ps);
	}
}
