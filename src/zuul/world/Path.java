package zuul.world;

import java.awt.Color;

import zuul.math.IntTransform;
import zuul.renderer.Material;
import zuul.renderer.Renderable;
import zuul.renderer.Renderable.Shape;

public abstract class Path {
	// Serialized fields
	// getType() is also serialized.
	private Room a, b;
	private String aName, bName;
	
	// TODO: Add conditional paths (based on player inventory, explored paths/rooms, etc.)
	public Path(Room _a, Room _b, String _aName, String _bName) {
		this.a = _a;
		this.b = _b;
		this.aName = _aName;
		this.bName = _bName;
		a.addPath(this);
		b.addPath(this);
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

	public abstract boolean potentialToA();
	
	public abstract boolean potentialToB();
	
	public abstract boolean accessToA(PlayerState ps);
	
	public abstract boolean accessToB(PlayerState ps);

	public abstract PathType getType();
	
	public Renderable getRenderable() {
		Room a = getA(), b = getB();
		return new Renderable(
				Shape.LINE,
				Material.stroke(this instanceof TwoWayPath?Color.GREEN:Color.YELLOW),
				new IntTransform(a.getPosition(), b.getPosition()));
	}
	
	public enum PathType {
		TWO_WAY,
		ONE_WAY,
		CONDITIONAL;
	}
}
