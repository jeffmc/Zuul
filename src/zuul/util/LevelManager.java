package zuul.util;

import zuul.world.Level;
import zuul.world.Room;
import zuul.world.path.TwoWayPath;

public class LevelManager {
	
    private static final String NORTH = "north", EAST = "east", SOUTH = "south", WEST = "west";
	public static Level createDefaultLevel()
    {
		// Create level
    	Level l = new Level("University", false);

        // Create the rooms
        Room outside, theatre, pub, lab, office;
        outside = new Room(l, "Outside", "outside the main entrance of the university");
        theatre = new Room(l, "Theatre", "in a lecture theatre");
        pub     = new Room(l, "Pub", "in the campus pub");
        lab     = new Room(l, "Lab", "in a computing lab");
        office  = new Room(l, "Office", "in the computing admin office");

        // Set level spawn
        l.setSpawn(outside);

        // Make paths
        new TwoWayPath(outside, theatre, EAST, WEST);
        new TwoWayPath(outside, lab, SOUTH, NORTH);
        new TwoWayPath(outside, pub, WEST, EAST);
        new TwoWayPath(lab, office, EAST, WEST);

        // All post-init
        l.calcExits();
        return l;
    }
	
}
