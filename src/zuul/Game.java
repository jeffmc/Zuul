package zuul;

import java.io.File;

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

class Game 
{
    private CommandParser cmdParser;
    private Level currentLevel;
    private Room currentRoom;
        
    private final String NORTH = "north", EAST = "east", SOUTH = "south", WEST = "west";
    
    /**
     * Create the game and initialize its internal map.
     */
    public Game() 
    {
        createDefaultLevel();
		Level.save(currentLevel, new File(currentLevel.getName() + ".yaml"));
		
        cmdParser = new CommandParser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createDefaultLevel()
    {
    	currentLevel = new Level("Genesis");
        Room outside, theatre, pub, lab, office;
        
        // create the rooms
        outside = new Room("Outside", "outside the main entrance of the university");
        theatre = new Room("Theatre", "in a lecture theatre");
        pub = new Room("Pub", "in the campus pub");
        lab = new Room("Lab", "in a computing lab");
        office = new Room("Office", "in the computing admin office");

        currentLevel.add(outside,theatre,pub,lab,office);
        currentLevel.setSpawn(outside);
        
        // Initialize room exits
        outside.setExit(EAST, theatre);
        outside.setExit(SOUTH, lab);
        outside.setExit(WEST, pub);

        theatre.setExit(WEST, outside);

        pub.setExit(EAST, outside);

        lab.setExit(NORTH, outside);
        lab.setExit(EAST, office);

        office.setExit(WEST, lab);
        
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            

        currentRoom = currentLevel.getSpawn();  // start game outside
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = cmdParser.getCommand();
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
        System.out.println(currentRoom.getLongDescription());
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
        if (commandWord.equals("help"))
            printHelp();
        else if (commandWord.equals("go"))
            goRoom(command);
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
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
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        cmdParser.showCommands();
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
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null)
            System.out.println("There is no door!");
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
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
