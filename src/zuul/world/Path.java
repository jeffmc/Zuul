package zuul.world;

public abstract class Path {
	protected Room a, b;
	private String aName, bName;
	
	// TODO: Add conditional paths (based on player inventory, explored paths/rooms, etc.)
	public Path(Room _a, Room _b, String aName, String bName) {
		a=_a;
		b=_b;
		if (a == b) throw new IllegalArgumentException("Invalid path, rooms are the same!");
		if (a.getLevel() != b.getLevel()) throw new IllegalArgumentException("Rooms don't have same parent level!");
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

	public abstract boolean accessToA(PlayerState ps);
//		return b.getExits().values().contains(a);
	
	public abstract boolean accessToB(PlayerState ps);
//		return a.getExits().values().contains(b);
	
}
