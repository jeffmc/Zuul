package zuul.world;
import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import zuul.math.Int2;
import zuul.math.IntTransform;
import zuul.renderer.Material;
import zuul.renderer.Renderable;

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
    private Renderable.Shape shape = Renderable.Shape.BOX;
    private Level level; // The parent level
    private Set<Path> paths; // Contains connections to neighbors
	private Renderable renderable; // Render object used by reference in Renderer.
    
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
   
    public Room(String name, String description) 
   {
	   this(name, description, 240, 103, 50, 50); // default coordinates and size (centered and 50x50)
   }
   
	/**
	 * Define an exit from this room.
	 */
	public void setExit(String direction, Room neighbor) 
	{
		if (level == neighbor.getLevel()) {
			exits.put(direction, neighbor);
		} else {
			throw new IllegalArgumentException("Desired neighbor rooms not in same level!");
		}
	}

	public void calcPaths() {
		for (Entry<String, Room> e : exits.entrySet()) {
			Room n = e.getValue(); // neighbor
			if (!(n.hasPathTo(this)||hasPathTo(n))) {
				// Get neighbor path name
				String neighborPathName = null;
				for (Entry<String, Room> d : n.getExits().entrySet())
					if (d.getValue()==this) neighborPathName = d.getKey();
				if (neighborPathName == null) {
					// One-way path
//					System.out.println("MADE ONE WAY PATH: " + getName() + "->" + n.getName());
					Path p = new OneWayPath(this, n, e.getKey(), null, true);
					addPath(p);
					n.addPath(p);
					level.add(p);
				} else {
					// Two-way path
					Path p = new TwoWayPath(this, n, e.getKey(), neighborPathName);
//					System.out.println("Two-way path made: " + e.getKey() + ", " + neighborPathName);
					addPath(p);
					n.addPath(p);
					level.add(p);
				}
			} else {
//				System.out.println("Path creation averted");
			}
		}
	}
	
	public void calcExits() {
//		HashMap<String, Room> beforeExits = exits;
		exits = new HashMap<String, Room>();
		for (Path p : paths) {
			boolean isa;
			if (p.getA()==this) { 
				isa = true; 
			} else if (p.getB()==this) { 
				isa = false; 
			} else { throw new IllegalStateException("Path doesn't contain this room as A or B!"); }
			if ((isa&&p.potentionalToB())||
				((!isa)&&p.potentionalToA())) 
					this.exits.put(isa?p.getAName():p.getBName(), isa?p.getB():p.getA());
		}
//		System.out.println(this.getName() + ":");
//		System.out.println("  " + beforeExits);
//		System.out.println("  " + exits);
//		System.out.println();
	}
	
	
	public Renderable getRenderable() {
		if (renderable != null) return renderable;
		makeRenderable();
		return renderable;
		
	}
	
	private void makeRenderable() {
		renderable = new Renderable(
				shape, 
				Material.stroke(isSpawnpoint()?Color.MAGENTA:Color.BLUE),
				transform);
	}
	
	// Has path to, not necessarily access to.
	private boolean hasPathTo(Room target) {
		for (Path p : paths) 
			if ((p.getA()==this&&p.getB()==target) ||
					(p.getA()==target&&p.getB()==this)) return true;
		return false;
	}

	/**
	 * Return the name of the room (the one that was defined in the
	 * constructor).
	 */
	public String getName() {
		return name;
	}
    
	/**
	 * Return the description of the room (the one that was defined in the
	 * constructor).
	 */
	public String getShortDescription()
	{
	    return description;
	}

	/**
	 * Return a long description of this room, in the form:
	 *     You are in the kitchen.
	 *     Exits: north west
	 */
	public String getLongDescription()
	{
	    return "You are " + description + ".\n" + getExitString();
    }

	/**
	 * Return a string describing the room's exits, for example
	 * "Exits: north west".
	 */
	private String getExitString()
	{
	    String returnString = "Exits:";
	    Set<String> keys = exits.keySet();
	    for(Iterator<String> iter = keys.iterator(); iter.hasNext(); )
	        returnString += " " + iter.next();
	    return returnString;
	}

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     */
    public Room getExit(String direction) 
    {
        return (Room)exits.get(direction);
    }

    public Map<String, Room> getExits() {
    	return exits;
    }
    
    // Return parent level
	public Level getLevel() {
		return level;
	}

	// Set parent level, should only be called by level.add()
	public void setLevel(Level level) {
		this.level = level;
	}

	// Get X coord
	public int getX() {
		return transform.position.x;
	}

	// Set X coord
	public void setX(int x) {
		transform.position.x = x;
	}

	// Get Y coord
	public int getY() {
		return transform.position.y;
	}

	// Set Y coord
	public void setY(int y) {
		transform.position.y = y;
	}

	// Get width
	public int getWidth() {
		return transform.scale.x;
	}

	// Set width
	public void setWidth(int width) {
		transform.scale.x = width;
	}

	// Get height
	public int getHeight() {
		return transform.scale.y;
	}

	// Set height
	public void setHeight(int height) {
		transform.scale.y = height;
	}
	
	public void setPosition(Int2 position) {
		transform.position.set(position);
	}
	
	public void setPosition(int x, int y) {
		transform.position.set(x,y);
	}
	
	public Int2 getPosition() {
		return transform.position;
	}
	
	// Set size (width, height)
	public void setSize(int width, int height) {
		transform.scale.set(width, height);
	}

	public Int2 getSize() {
		return transform.scale;
	}
	
	// Set size and position (x, y, width, height) More efficient for repainting GUI
	public void setTransform(int x, int y, int width, int height) {
		transform.set(x, y, width, height);
	}
	// Set size and position (x, y, width, height) More efficient for repainting GUI
	public void setTransform(IntTransform o) {
		transform.set(o);
	}
	
	public IntTransform getTransform() {
		return transform;
	}
	
	protected void addPath(Path p) {
		paths.add(p);
	}
	
	public Set<Path> getPaths() {
		return paths;
	}

	public boolean isSpawnpoint() {
		return level.getSpawn()==this;
	}
	
	public boolean contains(Int2 p) { // TODO: Doesn't work for negative scale rect or have oval support
		Int2 o = transform.position; // o for origin
		Int2 s = transform.scale;
		return p.x>=o.x&&p.x<=o.x+s.x&&p.y>=o.y&&p.y<=o.y+s.y;
	}
}

