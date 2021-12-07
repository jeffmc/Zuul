package zuul;

import java.io.File;

import mcmillan.ecs.ECS;
import mcmillan.ecs.TagComponent;
import zuul.math.IntTransform;
import zuul.scene.Scene;
import zuul.util.LevelManager;
import zuul.world.Level;
import zuul.world.Room;

// For repository viewers: https://github.com/decorators-squad/eo-yaml/releases/tag/6.0.0
// This release of YAML library was put into the libSrc folder.

public class Launcher {
	
	private static Editor editor;
	private static Game instance;
	private static Level level;
//	private static LevelState ls; TODO: Add levelState, to check if items have been picked up.
	
	public static void main(String args[]) {
		
		ECS ecs = new ECS("Zuul");
		long e = ecs.newEntity("Example Tag");
		TagComponent tag = ecs.getComponent(TagComponent.class, e);
//		TagComponent tag = ecs.addComponent(TagComponent.class, e,  "Invalid Tag");
		boolean test = ecs.hasComponent(TagComponent.class, e);
		System.out.println(test);
		System.out.println(tag.tag);
		
		level = LevelManager.load(new File("GenesisReadOnly.yaml"));
		LevelManager.save(level);
		
		editor = new Editor();
		Scene scene = Scene.levelToScene(level);
		editor.setActiveScene(scene);
		editor.start();
		
		if (level != null) {
			instance = new Game(level);
			instance.play();
		}
	}
	
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
//        outside.setExit(Game.EAST, theatre);
//        outside.setExit(Game.SOUTH, lab);
//        outside.setExit(Game.WEST, pub);
//
//        theatre.setExit(Game.WEST, outside);
//
//        pub.setExit(Game.EAST, outside);
//
//        lab.setExit(Game.NORTH, outside);
//        lab.setExit(Game.EAST, office);
//
//        office.setExit(Game.WEST, lab);
        
        l.completedLoading();
        return l;
    }
}
