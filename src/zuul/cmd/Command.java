package zuul.cmd;

import zuul.Game;

/**
 * This class is the main class of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * This class holds information about a command that was issued by the user.
 * A command currently consists of two strings: a command word and a second
 * word (for example, if the command was "take map", then the two strings
 * obviously are "take" and "map").
 * 
 * The way this is used is: Commands are already checked for being valid
 * command words. If the user entered an invalid command (a word that is not
 * known) then the command word is <null>.
 *
 * If the command had only one word, then the second word is <null>.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */

public class Command {
    private String[] words; // Each word within the user-inputted command.
    private boolean isCommand = false; // True if valid command, initialized in constructor.

    /**
     * Create a command object. First and second word must be supplied, but
     * either one (or both) can be null. The command word should be null to
     * indicate that this was a command that is not recognised by this game.
     */
    public Command(String[] tokens) {
    	this.words = tokens;
    	if (words.length > 0) {
    		if (CommandWords.isCommand(this)) {
    			isCommand = true;
    		} else {
    			isCommand = false;
    		}
    	} else {
    		isCommand = false;
    	}
    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     */
    public String getCommandWord()
    {
        return words.length > 0 ? words[0] : null;
    }

    /**
     * Return the second word of this command. Returns null if there was no
     * second word.
     */
    public String getSecondWord()
    {
        return words.length > 1 ? words[1] : null;
    }

    public String getWordAt(int idx) {
    	if (idx < 0) throw new IllegalArgumentException("Cannot have negative command word index");
        return words.length > idx ? words[idx] : null;
    }
    
    /**
     * Return true if this command was not understood.
     */
    public boolean isUnknown()
    {
        return (!isCommand);
    }

    /**
     * Return true if the command has a second word.
     */
    public boolean hasSecondWord()
    {
        return words.length > 1;
    }
    
    // Return true if this command word matches the target string using any case 
    public boolean matches(String target) {
    	if (getCommandWord() != null)
    		return Game.blurryMatch(getCommandWord(), target);
		return false;
    }
}

