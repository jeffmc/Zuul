package zuul;

import zuul.util.LevelManager;
import zuul.world.Level;

// The Zuul project.
// Launcher class, only used for creating and running game/level instances.

public class Launcher {
	
	private static Game game = null;
	private static Level level = null;
	
    // Create a game instance using a default level instance, and run.
    public static void main(String args[]) {
		level = LevelManager.createDefaultLevel();
		game = new Game(level);
		game.play();
    }
}
