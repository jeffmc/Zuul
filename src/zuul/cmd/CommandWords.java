package zuul.cmd;
/*
 * This class is the main class of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */

public class CommandWords {
    // a constant array that holds all valid command words
	public static final String GO = "go", QUIT = "quit", HELP = "help", TEST = "test", PICKUP = "pickup", DROP = "drop", INV = "inv";
	
    private static final String[] validCommands = {
        GO, QUIT, HELP, TEST, PICKUP, DROP, INV
    };
    private static final String[] commandDescriptions = {
    		"go [exit] - Moves the player to the room using given path.",
    		"quit - Quits the game.",
    		"help - Shows this help message.",
    		"test [args] - Runs current test method.",
    		"pickup [item] - Pickup item from room and place in player inventory.",
    		"drop [item] - Drop item from player inventory and place in room.",
    		"inv - Print player inventory"
    };

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        // nothing to do at the moment...
    }

    /**
     * Check whether a given String is a valid command word. 
     * Return true if it is, false if it isn't.
     */
    public static boolean isCommand(String aString) {
        for(int i = 0; i < validCommands.length; i++) {
            if(validCommands[i].equals(aString))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }

    /*
     * Print all valid commands to System.out.
     */
    public static void showHelp() {
    	if (validCommands.length != commandDescriptions.length) 
    		System.err.println("IS THERE HELP FOR EVERY COMMAND IN CommandWords?");
        for(int i = 0; i < commandDescriptions.length; i++) {
            System.out.print("  " + commandDescriptions[i] + "\n");
        }
    }
}
