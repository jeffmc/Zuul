package zuul;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

class Room
{
	// Serialized
	private String name; // name of room (used in level saving-loading)
    private String description; // description of the room
    private HashMap<String, Room> exits; // stores exits of this room.
    private int x, y, width, height; // coordinates and size of the room, defined as a box by top-left corner.
    
    // Not-serialized
    private Level level; // The parent level
    
    /**
     * Create a room described "description". Initially, it has no exits.
     * "description" is something like "in a kitchen" or "in an open court 
     * yard".
     */
    public Room(String name, String description, int x, int y, int width, int height) 
    {
    	this.name = name;
        this.description = description;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        exits = new HashMap<String, Room>();
    }

   public Room(String name, String description) 
   {
	   this(name, description,240,103,50,50); // default coordinates and size (centered and 50x50)
   }
   
   private void repaint() {
	   level.repaint();
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
		repaint();
	}

	// Get X coord
	public int getX() {
		return x;
	}

	// Set X coord
	public void setX(int x) {
		repaint();
		this.x = x;
	}

	// Get Y coord
	public int getY() {
		return y;
	}

	// Set Y coord
	public void setY(int y) {
		repaint();
		this.y = y;
	}

	// Get width
	public int getWidth() {
		return width;
	}

	// Set width
	public void setWidth(int width) {
		repaint();
		this.width = width;
	}

	// Get height
	public int getHeight() {
		return height;
	}

	// Set height
	public void setHeight(int height) {
		repaint();
		this.height = height;
	}
	
	// Set coordinates (x,y)
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		repaint();
	}
	
	// Set size (width, height)
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		repaint();
	}
	
	// Set size and position (x, y, width, height) More efficient for repainting GUI
	public void setTransform(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		repaint();
	}
}

