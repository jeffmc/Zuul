package zuul.world;

public class OneWayPath extends Path {

	// Serialized
	private boolean blind;
	
	public OneWayPath(Room parent, Room child, String aName, String bName, boolean blind) { // child can't access parent, child is B
		super(parent, child, aName, bName);
		this.blind = blind; // If blind is true, path will not be visible to child.
	}
	
	public boolean isChildBlind() {
		return blind;
	}
	
	public Room getParent() {
		return getA();
	}

	public Room getChild() {
		return getB();
	}
	
	@Override
	public boolean accessToA(PlayerState ps) {
		return false;
	}

	@Override
	public boolean accessToB(PlayerState ps) {
		return true;
	}

	@Override
	public boolean potentialToA() {
		return false;
	}

	@Override
	public boolean potentialToB() {
		return true;
	}

	@Override
	public PathType getType() {
		return PathType.ONE_WAY;
	}

}
