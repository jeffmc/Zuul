package zuul.world;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import zuul.Game;
import zuul.world.item.Inventory;
import zuul.world.item.Item;
import zuul.world.path.ExitPair;
import zuul.world.path.Path;

import java.util.Set;

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
	private String name; // name of room (used in level saving-loading)
	private String description; // description of the room
	private HashMap<String, ExitPair> exits = new HashMap<>(); // stores exits of this room.
 	private Level level; // The parent level
	private Set<Path> paths = new HashSet<Path>(); // Contains connections to neighbors
	
	private Inventory inventory; // Room item inventory
	public Inventory getInventory() { return inventory; } // Getter
	
	/**
	 * Create a room described "description". Initially, it has no exits.
	 * "description" is something like "in a kitchen" or "in an open court 
	 * yard". 
	 * Initializes room inventory.
	 * Adds items to room if possible.
	 * Description shown in this format to the user "You are [description]."
	 */
	public Room(Level level, String name, String description, Item... items) { 
		this.level = level;
		this.level.add(this);
		this.name = name;
	    this.description = description;
	    this.inventory = new Inventory(this, items);
	}
	
	// Initialize room without any items.
	public Room(Level level, String name, String description) {
		this(level,name,description, new Item[]{});
	};
   
	// Called after loading in from file.
	public void calcExits() {
		if (exits != null) { exits.clear(); } else {
			exits = new HashMap<String, ExitPair>();
		}
		for (Path p : paths) {
			boolean isa;
			if (p.getA()==this)
				isa = true; 
			else if (p.getB()==this)
				isa = false; 
			else throw new IllegalStateException("Path doesn't contain this room as A or B!");
			
			if ((isa&&p.potentialToB())||
				((!isa)&&p.potentialToA())) 
					this.exits.put(isa?p.getAName():p.getBName(), new ExitPair(isa?p.getB():p.getA(), p));
		}
	}

	// Return the name of the room ( defined in the constructor).
	public String getName() { return name; }
    
	// Return the description of the room ( defined in the constructor).c
	public String getShortDescription() { return description; }
	
	/**
	 * Return a string describing the room's exits, for example
	 * "Exits: north west".
	 */
	private String getExitString()
	{
	    String returnString = "Exits:";
	    Set<Entry<String, ExitPair>> entries = exits.entrySet();
	    for(Entry<String, ExitPair> e : entries)
	        returnString += "\n  " + e.getKey() + " (" + e.getValue().room().getName() + ")";
	    return returnString;
	}
	
	// Called when the player enters this room, describes the state of the room (inventory) and displays exits.
	public void printEntered() {
        System.out.println("You are " + description + ".");
        System.out.println("Items:\n" + inventory.getAsString("  "));
		System.out.println(getExitString());
	}

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     */
    public Room getExit(String direction) { 
    	ExitPair ep = getExitPair(direction);
    	if (ep != null) return ep.room();
    	return null;
    }
    
    // Return ExitPair at direction or null if not found.
    public ExitPair getExitPair(String direction) { 
    	for (Entry<String, ExitPair> e : exits.entrySet())
    		if (Game.blurryMatch(e.getKey(), direction))
				return e.getValue();
    	return null;
    }

    // Get all exits this room has in an unmodifiable map.
    public Map<String, ExitPair> getExits() { return Collections.unmodifiableMap(exits); }
    
    // Return parent level
	public Level getLevel() { return level; }
	
	// Add a path to this room
	public void addPath(Path p) { paths.add(p); }
	
	// Get paths this room contains.
	public Set<Path> getPaths() { return Collections.unmodifiableSet(paths); }

	// Return if this room is the spawn for the parent level.
	public boolean isSpawn() { return level.getSpawn() == this; }
}

