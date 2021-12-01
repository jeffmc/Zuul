package zuul.world;

public class OneWayPath extends Path {

	private boolean blind;
	
	public OneWayPath(Room parent, Room child, String aName, String bName, boolean blind) {
		super(parent, child, aName, bName);
		this.blind = blind; // If blind is true, path will not be visible to child.
	}
	
	public boolean isChildBlind() {
		return blind;
	}
	
	public Room getParent() {
		return a;
	}

	public Room getChild() {
		return b;
	}
	
	@Override
	public boolean accessToA(PlayerState ps) {
		return true;
	}

	@Override
	public boolean accessToB(PlayerState ps) {
		return true;
	}

}
