package zuul.world;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

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
	private HashMap<String, Room> exits = new HashMap<>(); // stores exits of this room.
 	private Level level; // The parent level
	private Set<Path> paths = new HashSet<Path>(); // Contains connections to neighbors
	
	/**
	 * Create a room described "description". Initially, it has no exits.
	 * "description" is something like "in a kitchen" or "in an open court 
	 * yard".
	 */
	public Room(Level level, String name, String description) { 
		this.level = level;
		this.level.add(this);
		this.name = name;
	    this.description = description;
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
	    return "You are " + description + ".\n" + getInventoryString() + "\n" + getExitString();
    }

	/**
	 * Return a string describing the room's exits, for example
	 * "Exits: north west".
	 */
	
	private String getInventoryString() {
		return "Items:"; // Add room inventory printing.
	}
	
	private String getExitString()
	{
	    String returnString = "Exits:";
	    Set<Entry<String, Room>> entries = exits.entrySet();
	    for(Entry<String, Room> e : entries)
	        returnString += "\n  " + e.getKey() + " (" + e.getValue().getName() + ")";
	    return returnString;
	}
	
	// Called when the player enters this room, meant to only print string.
	public void printEntered() {
        System.out.println(getLongDescription());
	}

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     */
    public Room getExit(String direction) { return (Room)exits.get(direction); }

    public Map<String, Room> getExits() { return exits; }
    
    // Return parent level
	public Level getLevel() { return level; }
	
	public void addPath(Path p) { paths.add(p); }
	
	public Set<Path> getPaths() { return paths; }

	public boolean isSpawnpoint() { return level.getSpawn() == this; }
}

