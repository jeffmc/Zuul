package zuul;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;

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
	private String name; // name of room (used in level saving-loading)
    private String description; // description of the room
    private HashMap<String, Room> exits; // stores exits of this room.
    private Level level; // The parent level

    private byte x, y, width, height; // coordinates and size of the room, defined as a box by top-left corner, utilized only in graphic implementation. Defined as bytes to limit coords from 0-255
    
    /**
     * Create a room described "description". Initially, it has no exits.
     * "description" is something like "in a kitchen" or "in an open court 
     * yard".
     */
    public Room(String name, String description, byte x, byte y, byte width, byte height) 
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
	   this(name, description,(byte)103,(byte)103,(byte)50,(byte)50); // default coordinates and size (centered and 50x50)
   }
   
    /**
     * Define an exit from this room.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
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

    // Return parent level
	public Level getLevel() {
		return level;
	}

	// Set parent level, should only be called by level.add()
	public void setLevel(Level level) {
		this.level = level;
	}

	// Get X coord
	public byte getX() {
		return x;
	}

	// Set X coord
	public void setX(byte x) {
		level.repaint();
		this.x = x;
	}

	// Get Y coord
	public byte getY() {
		return y;
	}

	// Set Y coord
	public void setY(byte y) {
		level.repaint();
		this.y = y;
	}

	// Get width
	public byte getWidth() {
		return width;
	}

	// Set width
	public void setWidth(byte width) {
		level.repaint();
		this.width = width;
	}

	// Get height
	public byte getHeight() {
		return height;
	}

	// Set height
	public void setHeight(byte height) {
		level.repaint();
		this.height = height;
	}
	
	// Set coordinates (x,y)
	public void setPosition(byte x, byte y) {
		this.x = x;
		this.y = y;
		level.repaint();
	}
	
	// Set size (width, height)
	public void setSize(byte width, byte height) {
		this.width = width;
		this.height = height;
		level.repaint();
	}
	
	// Set size and position (x, y, width, height) More efficient for repainting GUI
	public void setTransform(byte x, byte y, byte width, byte height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		level.repaint();
	}
}

