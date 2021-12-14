package zuul.world;

import java.util.HashSet;

import zuul.world.item.Inventory;
import zuul.world.path.Path;

public class PlayerState {
	private HashSet<Path> exploredPaths = new HashSet<>(); // TODO: Add public access
	private HashSet<Room> exploredRooms = new HashSet<>(); // TODO: Add public access
	
	private Room location = null;
	
	private Inventory inventory = new Inventory(true);
	public Inventory getInventory() { return inventory; }
	
	public PlayerState(Room spawn) {
		goTo(spawn);
	}
	
	public void traversedPath(Path p) {
		assert p != null;
		exploredPaths.add(p);
	}
	
	public void goTo(Room r) {
		assert r != null;
		exploredRooms.add(r);
		location = r;
	}
	
	public Room getLocation() {
		return location;
	}
}
