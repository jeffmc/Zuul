package zuul;

import java.io.File;
import java.io.IOException;

// For repository viewers: https://github.com/decorators-squad/eo-yaml/releases/tag/6.0.0
// This release of YAML library was put into the libSrc folder.

public class Launcher {
	
	private static Editor editor;
	private static Game instance;
	
	public static void main(String args[]) {
		
		try {
			Level l = LevelManager.load(new File("ReadOnly.yaml"));
			LevelManager.save(l);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		editor = new Editor();
		editor.start();
//		instance = new Game(l);
//		instance.play();
	}
	

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
