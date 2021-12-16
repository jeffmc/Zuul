package zuul.cmd;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/*
 * This class is the main class of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0 (February 2002)
 */

public class CommandParser {

	// Get command from console input and return command object.
    public static Command getCommand() 
    {
        String inputLine = "";   // will hold the full input line

        System.out.println();
        System.out.print("-> ");     // print prompt

        BufferedReader reader = 
            new BufferedReader(new InputStreamReader(System.in));
        try {
            inputLine = reader.readLine();
        }
        catch(java.io.IOException exc) {
            System.out.println ("There was an error during reading: "
                                + exc.getMessage());
        }

        // Tokenize string into word array
        StringTokenizer tokenizer = new StringTokenizer(inputLine);
        int length = tokenizer.countTokens();
        String[] words = new String[length];
        
        for (int i=0;i<length;i++) {
        	words[i] = tokenizer.nextToken();
        }

        // Construct command using tokenized input.
        return new Command(words);        	
    }
}
