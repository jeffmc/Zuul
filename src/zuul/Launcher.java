package zuul;

import java.io.File;

import zuul.editor.Editor;
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
//	private static LevelState ls; // TODO: Add levelState, to check if items have been picked up.
	
	public static void main(String args[]) {
//		propertiesTest();
		
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
	
//	public static void propertiesTest() {
//		Class<?> cls = ECS.class;
//		for (Field f : cls.getDeclaredFields()) {
//			System.out.println(Modifier.toString(f.getModifiers()) + " " + f.getGenericType().getTypeName() + " " + f.getName() + ";");
//		}
//		System.out.println();
//		for (Constructor<?> d : cls.getDeclaredConstructors()) {
//			Parameter[] ps = d.getParameters();
//			String[] pss = new String[ps.length]; 
//			for (int i=0;i<ps.length;i++)
//				pss[i] = ps[i].getParameterizedType().getTypeName();
//			
//			System.out.println(Modifier.toString(d.getModifiers()) + " " + d.getName() + "(" + String.join(", ", pss) + ");");
//		}
//		System.out.println();
//		for (Method m : cls.getDeclaredMethods()) {
//			Parameter[] ps = m.getParameters();
//			String[] pss = new String[ps.length];
//			for (int i=0;i<ps.length;i++)
//				pss[i] = ps[i].getParameterizedType().getTypeName();
//			
//			System.out.println(Modifier.toString(m.getModifiers()) + " " + m.getGenericReturnType().getTypeName() + " " + m.getName() + "(" + String.join(", ", pss) + ");");
//		}
//	}
	
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
