package zuul.world;

import java.util.HashSet;

public class PlayerState {
	// TODO: inventory
	private HashSet<Path> exploredPaths; // TODO: Add explore path functionality
	private HashSet<Room> exploredRooms; // TODO: Add explore room functionality
	private Room location;
	
	public PlayerState() {
		
	}
	
	public void setLocation(Room r, boolean utilizedPath) {
		if (utilizedPath) {
			// TODO: Add explored functionality here
		}
		location = r;
	}
	
	public Room getLocation() {
		return location;
	}
}
