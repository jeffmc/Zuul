package mcmillan.engine.core;

import mcmillan.editor.EditorLayer;
import mcmillan.engine.math.IntTransform;
import zuul.Game;
import zuul.world.Level;
import zuul.world.Room;
import zuul.world.TwoWayPath;

// For repository viewers: https://github.com/decorators-squad/eo-yaml/releases/tag/6.0.0
// This release of YAML library was put into the libSrc folder.

public class Launcher {
	
	private static EditorLayer editor;
	private static Game instance;
	private static Level level;
//	private static LevelState ls; // TODO: Add levelState, to check if items have been picked up.
	
	@Deprecated
	@SuppressWarnings("unused")
    private static Level createDefaultLevel()
    {
    	Level l = new Level("Genesis", false);
        Room outside, theatre, pub, lab, office;
        
        // Create the rooms TODO: Change room positions
        outside = new Room("Outside", "outside the main entrance of the university",
        		new IntTransform(0, 0, 50, 50));
        theatre = new Room("Theatre", "in a lecture theatre",
        		new IntTransform(0, 0, 50, 50));
        pub = new Room("Pub", "in the campus pub",
        		new IntTransform(0, 0, 50, 50));
        lab = new Room("Lab", "in a computing lab",
        		new IntTransform(0, 0, 50, 50));
        office = new Room("Office", "in the computing admin office",
        		new IntTransform(0, 0, 50, 50));

        l.add(outside,theatre,pub,lab,office);
        l.setSpawn(outside);
        
        // Initialize room exits TODO: Replace setExit with addPath and new Path().
        
        new TwoWayPath(outside, theatre, Game.EAST, Game.WEST);
        new TwoWayPath(outside, lab, Game.SOUTH, Game.NORTH);
        new TwoWayPath(outside, pub, Game.WEST, Game.EAST);
        new TwoWayPath(lab, office, Game.EAST, Game.WEST);
        
        l.completedLoading();
        return l;
    }
}
