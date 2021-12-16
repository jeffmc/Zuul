package zuul.world.path;

import zuul.world.Room;

// Used in Room class for organizing exits and paths, this should eventually be dissolved into a smarter system.
public class ExitPair {
	
	private Room room;
	public Room room() { return room; } 
	private Path path;
	public Path path() { return path; }
	
	public ExitPair(Room r, Path p) {
		room = r;
		path = p;
	}
	
}
