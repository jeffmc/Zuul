package zuul.world;

import java.util.HashSet;

import zuul.world.path.Path;

public class PlayerState {
	// TODO: inventory
	private HashSet<Path> exploredPaths = new HashSet<>(); // TODO: Add explore path functionality
	private HashSet<Room> exploredRooms = new HashSet<>(); // TODO: Add explore room functionality
	private Room location = null;
	
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
