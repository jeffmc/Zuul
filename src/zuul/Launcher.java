package zuul;

import java.io.File;

import zuul.util.LevelManager;
import zuul.world.Level;
import zuul.world.Room;

// For repository viewers: https://github.com/decorators-squad/eo-yaml/releases/tag/6.0.0
// This release of YAML library was put into the libSrc folder.

public class Launcher {
	
	private static Editor editor;
	private static Game instance;
	
	public static void main(String args[]) {
		Level l = LevelManager.load(new File("GenesisReadOnly.yaml"));
		LevelManager.save(l);
		
		editor = new Editor();
		editor.start();
		editor.setActiveLevel(l);
		
		if (l != null) {
			instance = new Game(l);
			instance.play();
		}
	}
	
	@Deprecated
	@SuppressWarnings("unused")
    private static Level createDefaultLevel()
    {
    	Level l = new Level("Genesis", false);
        Room outside, theatre, pub, lab, office;
        
        // Create the rooms
        outside = new Room("Outside", "outside the main entrance of the university");
        theatre = new Room("Theatre", "in a lecture theatre");
        pub = new Room("Pub", "in the campus pub");
        lab = new Room("Lab", "in a computing lab");
        office = new Room("Office", "in the computing admin office");

        l.add(outside,theatre,pub,lab,office);
        l.setSpawn(outside);
        
        // Initialize room exits
        outside.setExit(Game.EAST, theatre);
        outside.setExit(Game.SOUTH, lab);
        outside.setExit(Game.WEST, pub);

        theatre.setExit(Game.WEST, outside);

        pub.setExit(Game.EAST, outside);

        lab.setExit(Game.NORTH, outside);
        lab.setExit(Game.EAST, office);

        office.setExit(Game.WEST, lab);
        
        l.completedLoading();
        return l;
    }
}
