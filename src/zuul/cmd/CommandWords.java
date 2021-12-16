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
	// All commands defined as their own fields.
	public static final String 
			GO = "go", 
			QUIT = "quit", 
			HELP = "help", 
			PICKUP = "pickup", 
			DROP = "drop", 
			INV = "inv",
			WHEREAMI = "whereami";

    // a final array that holds all valid command words
    private static final String[] validCommands = {
        GO, QUIT, HELP, PICKUP, DROP, INV, WHEREAMI
    };
    
    // Printed when help command is fired.
    private static final String[] commandDescriptions = {
    		"go [exit] - Moves the player to the room using given path.",
    		"quit - Quits the game.",
    		"help - Shows this help message.",
    		"pickup [item] - Pickup item from room and place in player inventory.",
    		"drop [item] - Drop item from player inventory and place in room.",
    		"inv - Print player inventory",
    		"whereami - Prints the message welcoming you to the current room once more."
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
    public static boolean isCommand(Command c) {
        for(int i = 0; i < validCommands.length; i++) {
            if(c.matches(validCommands[i]))
                return true;
        }
        // if we get here, the string was not found in the commands
        return false;
    }

    /*
     * Print all valid commands and their description to System.out.
     */
    public static void showHelp() {
    	if (validCommands.length != commandDescriptions.length) 
    		System.err.println("IS THERE HELP FOR EVERY COMMAND IN CommandWords?");
        for(int i = 0; i < commandDescriptions.length; i++) {
            System.out.print("  " + commandDescriptions[i] + "\n");
        }
    }
}
