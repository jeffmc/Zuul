package zuul.world.path;

import zuul.world.PlayerState;
import zuul.world.Room;

public class TwoWayPath extends Path {

	public TwoWayPath(Room _a, Room _b, String aName, String bName) {
		super(_a, _b, aName, bName);
	}

	@Override
	public boolean accessToA(PlayerState ps) {
		return true;
	}

	@Override
	public boolean accessToB(PlayerState ps) {
		return true;
	}

	@Override
	public boolean potentialToA() {
		return true;
	}

	@Override
	public boolean potentialToB() {
		return true;
	}

	@Override
	public Type getType() {
		return Type.TWO_WAY;
	}

}
