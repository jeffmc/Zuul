package zuul.world.path;

import zuul.world.PlayerState;
import zuul.world.Room;

public class OneWayPath extends Path {
	 // Room "child" never has access to Room "parent".
	public OneWayPath(Room parent, Room child, String aName, String bName) {
		super(parent, child, aName, bName);
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
	public Type getType() {
		return Type.ONE_WAY;
	}

}
