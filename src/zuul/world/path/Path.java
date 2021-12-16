package zuul.world.path;

import zuul.world.PlayerState;
import zuul.world.Room;

// The abstract class Path is meant to be extended to create new types of paths, with varying levels of accessibility and complexity.
// All paths contain two rooms, and a name for the path from the perspective of each room.
// Paths have potential and access methods to determine whether or not the player may traverse.

public abstract class Path {
	private Room a, b; // Path ends.
	private String aName, bName; // Perspective names from each room
	
	public Path(Room _a, Room _b, String _aName, String _bName) {
		this.a = _a;
		this.b = _b;
		this.aName = _aName;
		this.bName = _bName;
		
		if (a == b) throw new IllegalArgumentException("Invalid path, rooms are the same!");
		if (a.getLevel() != b.getLevel()) throw new IllegalArgumentException("Rooms don't have same parent level!");
		
		a.addPath(this);
		b.addPath(this);
		a.getLevel().add(this);
	}
	
	final public Room getA() {
		return a;
	}
	
	final public Room getB() {
		return b;
	}
	
	final public String getAName() {
		return aName;
	}
	
	final public String getBName() {
		return bName;
	}

	// Returns true if X has potential to access Room Y
	public final boolean potentialTo(Room x, Room y) {
		return ((a==x&&b==y&&potentialToB()) || (a==y&&b==x&&potentialToA()));
	}
	
	// Returns true if X has immediate to access Room Y
	public final boolean accessTo(PlayerState ps, Room x, Room y) {
		return ((a==x&&b==y&&accessToB(ps)) || (a==y&&b==x&&accessToA(ps)));
	}
	
	// Returns true if Room B may access A, right now or in the future.
	public abstract boolean potentialToA();

	// Returns true if Room A may access B, right now or in the future.
	public abstract boolean potentialToB();

	// Returns true if Room B can access A given current player state.
	public abstract boolean accessToA(PlayerState ps);

	// Returns true if Room A can access B given current player state.
	public abstract boolean accessToB(PlayerState ps);

	// Return type of this path
	public abstract Type getType();
	
	// Path Types
	public enum Type {
		TWO_WAY,
		ONE_WAY,
		CONDITIONAL, 
		CONDITIONAL_TWO_WAY;
	}
}
