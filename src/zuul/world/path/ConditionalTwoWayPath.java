package zuul.world.path;

import zuul.world.PlayerState;
import zuul.world.Room;

// A two-way path that is enabled in both directions if the conditional is met, 
// also contains a message to be presented to player when conditional isn't met, a sort of tip/clue.

public class ConditionalTwoWayPath extends TwoWayPath {

	private PlayerState.Conditional condition; // Conditional to be satisfied for bi-directional travel to be allowed.
	private String message; // Fail message (tip/clue)
	public String message() { return message; } // Getter
	
	// Constructor that fills all fields.
	public ConditionalTwoWayPath(Room _a, Room _b, String aName, String bName, PlayerState.Conditional condition, String message) {
		super(_a, _b, aName, bName);
		this.condition = condition;
		this.message = message;
	}

	@Override
	public boolean accessToA(PlayerState ps) {
		return condition.check(ps);
	}

	@Override
	public boolean accessToB(PlayerState ps) {
		return condition.check(ps);
	}
	
	@Override
	public Type getType() {
		return Type.CONDITIONAL_TWO_WAY;
	}
	
	

}
