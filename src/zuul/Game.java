package zuul;

import zuul.cmd.Command;
import zuul.cmd.CommandParser;
import zuul.cmd.CommandWords;
import zuul.world.Level;
import zuul.world.PlayerState;
import zuul.world.Room;
import zuul.world.item.Inventory;
import zuul.world.item.Item;
import zuul.world.path.Path;
import zuul.world.path.ConditionalTwoWayPath;
import zuul.world.path.ExitPair;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initializes all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */

public class Game  {
    // Return true if strings are equal when converted to lowercase and whitespace is trimmed. TODO: Move to a util/misc class.
    public static boolean blurryMatch(String a, String b) {
		return a.trim().toLowerCase().equals(b.trim().toLowerCase());
    }
    
    private Level level; // Current level being played by this game instance, contains all rooms, paths, and items.
    private PlayerState playerState; // Current player playing within the above level.
    
    /**
     * Create the game and specify level to play.
     */
    public Game(Level level) {
    	this.level = level;
    }
    
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() {            
    	playerState = new PlayerState(level.getSpawn());
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (!finished) {
            Command command = CommandParser.getCommand();
            finished = processCommand(command);
            if (this.level.checkWin(playerState)) {
            	System.out.println("You have collected all metals, congrats!");
            	finished = true;
            }
        }
        System.out.println("Thank you for playing. Good bye.");
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        if (command.matches(CommandWords.HELP))
            printHelp();
        else if (command.matches(CommandWords.GO))
            goRoom(command);
        else if (command.matches(CommandWords.QUIT))
            wantToQuit = quit(command);
        else if (command.matches(CommandWords.PICKUP))
        	pickupItem(command);
        else if (command.matches(CommandWords.DROP))
        	dropItem(command);
        else if (command.matches(CommandWords.INV))
        	printInventory();
        else if (command.matches(CommandWords.WHEREAMI))
        	printWhereAmI();
        return wantToQuit;
    }
    // ________________________________________________________
    // USER COMMANDS:

    /**
	 * Print out the opening message for the player.
	 */
	private void printWelcome()
	{
	    System.out.println();
	    System.out.println("Welcome to The Town!");
	    System.out.println("The Town is a brand-new, incredibly exiciting adventure game.");
	    System.out.println("Type 'help' if you need help.");
	    System.out.println();
	    printWhereAmI();
	}

	/**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println();
        System.out.println("Your command words are:");
        CommandWords.showHelp();
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        ExitPair pair = playerState.getLocation().getExitPair(direction);
        
        if (pair != null) { // Determine if given direction is valid.
            Room r = pair.room();
            Path p = pair.path();
        	if (pair.path().accessTo(playerState, playerState.getLocation(), r)) { // Check if path allows access to room through exit
        		playerState.goTo(r);
                printWhereAmI();	
        	} else if (p instanceof ConditionalTwoWayPath) // Print tip/clue if possible.
        		System.out.println(ConditionalTwoWayPath.class.cast(p).message());
        } else {
            System.out.println("'" + direction + "' isn't a valid exit!");
        }
    }
    
    // Prints the room entered message.
    private void printWhereAmI() {
    	playerState.getLocation().printEntered();
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game. Return true, if this command
     * quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else
            return true;  // signal that we want to quit
    }
    
    // Pickup item specified by command if the current room contains it, otherwise tell user it wasn't found.
    private void pickupItem(Command command) {
    	if (!command.hasSecondWord()) {
    		System.out.println("Pickup what?");
    		return;
    	}
    	Room currentRoom = playerState.getLocation();
    	Inventory roomInventory = currentRoom.getInventory();
    	Item foundItem = roomInventory.getItemFromString(command.getSecondWord());
    	if (foundItem != null) {
    		foundItem.moveTo(playerState.getInventory());
    		System.out.println("Picked up " + foundItem.getName() + "!");
    	} else {
    		System.out.println("Couldn't find '" + command.getSecondWord() + "' in " + currentRoom.getName());
    	}
    } 
    
    // Drop item specified by command if the player is holding it, otherwise tell user it wasn't found.
    private void dropItem(Command command) {
    	if (!command.hasSecondWord()) {
    		System.out.println("Drop what?");
    		return;
    	}
    	Room currentRoom = playerState.getLocation();
    	Inventory roomInventory = currentRoom.getInventory();
    	
    	Inventory playerInventory = playerState.getInventory();
    	Item foundItem = playerInventory.getItemFromString(command.getSecondWord());
    	if (foundItem != null) {
        	if (foundItem.isUndroppable()) {
        		System.out.println(foundItem.getName() + " cannot be dropped!");
        	} else {
	    		foundItem.moveTo(roomInventory);
	    		System.out.println("Dropped up " + foundItem.getName() + "!");
        	}
    	} else {
    		System.out.println("Couldn't find '" + command.getSecondWord() + "' in your inventory!");
    	} 
	} 
    
    // Print player's inventory contents.
    private void printInventory() {
        System.out.println("Your inventory:\n" + playerState.getInventory().getAsString("  "));
    } 
    
}
