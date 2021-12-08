package zuul.world;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import zuul.math.Int2;
import zuul.math.IntTransform;

/*
 * Class Room - a room in an adventure game.
 *
 * This class is the main class of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */

public class Room {
	// Serialized
	private String name; // name of room (used in level saving-loading)
	private String description; // description of the room
	private HashMap<String, Room> exits; // stores exits of this room.
	private IntTransform transform; // position and size (used by reference in renderer.)
	
	// Not-serialized
//	private Renderable.Shape shape = Renderable.Shape.BOX; TODO: Reimplement shape
 	private Level level; // The parent level
	private Set<Path> paths; // Contains connections to neighbors
	
	/**
	 * Create a room described "description". Initially, it has no exits.
	 * "description" is something like "in a kitchen" or "in an open court 
	 * yard".
	 */
	public Room(String name, String description, IntTransform transform) { 
		this.name = name;
	    this.description = description;
	    this.transform = transform;
	    
	    exits = new HashMap<String, Room>();
	    paths = new HashSet<Path>();
	}

    public Room(String name, String description, int x, int y, int width, int height) {
    	this(name, description, new IntTransform(x, y, width, height));
    }
   
	// Called after loading in from file.
	public void calcExits() {
		if (exits != null) { exits.clear(); } else {
			exits = new HashMap<String, Room>();
		}
		for (Path p : paths) {
			boolean isa;
			if (p.getA()==this) { 
				isa = true; 
			} else if (p.getB()==this) { 
				isa = false; 
			} else { throw new IllegalStateException("Path doesn't contain this room as A or B!"); }
			if ((isa&&p.potentialToB())||
				((!isa)&&p.potentialToA())) 
					this.exits.put(isa?p.getAName():p.getBName(), isa?p.getB():p.getA());
		}
	}
	
	// Has immediate path to target, not necessarily access to.
	private boolean hasPathTo(Room target) {
		for (Path p : paths) 
			if ((p.getA()==this&&p.getB()==target) ||
				(p.getA()==target&&p.getB()==this)) return true;
		return false;
	}

	// Return the name of the room ( defined in the constructor).
	public String getName() { return name; }
    
	// Return the description of the room ( defined in the constructor).c
	public String getShortDescription() { return description; }

	/**
	 * Return a long description of this room, in the form:
	 *     You are in the kitchen.
	 *     Exits: north west
	 */
	public String getLongDescription() {
	    return "You are " + description + ".\n" + getExitString();
    }

	/**
	 * Return a string describing the room's exits, for example
	 * "Exits: north west".
	 */
	private String getExitString()
	{
	    String returnString = "Exits:";
	    Set<Entry<String, Room>> entries = exits.entrySet();
	    for(Entry<String, Room> e : entries)
	        returnString += " " + e.getKey() + " (" + e.getValue().getName() + "),";
	    return returnString;
	}

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     */
    public Room getExit(String direction) { return (Room)exits.get(direction); }

    public Map<String, Room> getExits() { return exits; }
    
    // Return parent level
	public Level getLevel() { return level; }

	// Set parent level, should only be called by level.add()
	public void setLevel(Level level) { this.level = level; }

	public int getX() { return transform.position.x; }

	public void setX(int x) { transform.position.x = x; }

	public int getY() { return transform.position.y; }

	public void setY(int y) { transform.position.y = y; }

	public int getWidth() { return transform.scale.x; }

	public void setWidth(int width) { transform.scale.x = width; }

	public int getHeight() { return transform.scale.y; }
	
	public void setHeight(int height) { transform.scale.y = height; }

	public Int2 getPosition() { return transform.position; }
	
	public void setPosition(Int2 position) { transform.position.set(position); }

	public Int2 getSize() { return transform.scale; }
	
	public void setSize(Int2 size) { transform.scale.set(size); }

	public IntTransform getTransform() { return transform; }
	
	public void setTransform(IntTransform o) { transform.set(o); }
	
	public void setTransform(int x, int y, int width, int height) { transform.set(x, y, width, height); }
	
	protected void addPath(Path p) { paths.add(p); }
	
	public Set<Path> getPaths() { return paths; }

	public boolean isSpawnpoint() { return level.getSpawn() == this; }
	
	public boolean contains(Int2 p) { // TODO: Doesn't work for negative scale rect or have oval support
		Int2 o = new Int2(transform.position).sub(new Int2(transform.scale).div(2)); // o for origin, (UPPER-LEFT)
		Int2 s = transform.scale;
		return p.x>=o.x&&p.x<=o.x+s.x&&p.y>=o.y&&p.y<=o.y+s.y;
	}
}

