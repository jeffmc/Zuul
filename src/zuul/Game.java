package zuul;

import zuul.cmd.Command;
import zuul.cmd.CommandParser;
import zuul.cmd.CommandWords;
import zuul.world.Level;
import zuul.world.PlayerState;
import zuul.world.Room;
import zuul.world.item.Item;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */

public class Game  {
    private Level level;
    private PlayerState playerState;
    
    /**
     * Create the game and initialize its internal map.
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
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Adventure!");
        System.out.println("Adventure is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printWhereAmI();
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

        String commandWord = command.getCommandWord();
        if (commandWord.equals(CommandWords.HELP))
            printHelp();
        else if (commandWord.equals(CommandWords.GO))
            goRoom(command);
        else if (commandWord.equals(CommandWords.QUIT))
            wantToQuit = quit(command);
        else if (commandWord.equals(CommandWords.TEST))
        	System.out.println(Item.getItemFromString(command.getSecondWord()));
        else if (commandWord.equals(CommandWords.PICKUP))
        	System.out.println("HANDLE PICKUP"); // TODO: Pickup command logic
        else if (commandWord.equals(CommandWords.DROP))
        	System.out.println("HANDLE DROP"); // TODO: Drop command logic
        else if (commandWord.equals(CommandWords.INV))
        	System.out.println("HANDLE INV"); // TODO: Inv command logic
        return wantToQuit;
    }

    // implementations of user commands:

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
        Room nextRoom = playerState.getLocation().getExit(direction);

        if (nextRoom != null)
            playerState.goTo(nextRoom);
        else {
            System.out.println("'" + direction + "' isn't a valid exit!");
        }
        printWhereAmI();
    }
    
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
}
