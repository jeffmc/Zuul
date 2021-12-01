package zuul;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlInput;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlMappingBuilder;
import com.amihaiemil.eoyaml.YamlPrinter;

public class LevelManager {

	
	// https://github.com/decorators-squad/eo-yaml/wiki/Block-Style-YAML
    public static void save(Level l, File f) {
    	// Create level mapping
    	YamlMappingBuilder level = Yaml.createYamlMappingBuilder()
    		    .add("Level", l.getName());
    	
    	// Create rooms mapping
    	YamlMappingBuilder rooms = Yaml.createYamlMappingBuilder();
    	for (Room r : l.getRooms()) {
    		// Make exits mapping per room
        	YamlMappingBuilder exits = Yaml.createYamlMappingBuilder();
        	for (Entry<String, Room> e : r.getExits().entrySet()) 
        		exits = exits.add(e.getKey(), e.getValue().getName());
        	
        	// Make singular room data mapping (contains exits mapping) and add to rooms mapping
    		rooms = rooms.add(r.getName(), Yaml.createYamlMappingBuilder()
		        		.add("Description", r.getShortDescription())
		        		.add("Exits", exits.build())
		        		.add("X", Integer.toString(r.getX()))
		        		.add("Y", Integer.toString(r.getY()))
		        		.add("Width", Integer.toString(r.getWidth()))
		        		.add("Height", Integer.toString(r.getHeight()))
		        		.build()
		        		);
    	}

    	// Add rooms mapping to level mapping
	    level = level.add("Rooms", rooms.build());
    	
	    // Build level mapping and output
    	YamlMapping yaml = level.build();
		try {
			YamlPrinter printer = Yaml.createYamlPrinter(
				    new FileWriter(f)
				);
	    	printer.print(yaml);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void save(Level l) {
    	save(l, new File(l.getName() + ".yaml"));
    }

	public static Level load(File f) throws IOException {
		YamlInput in = Yaml.createYamlInput(f);
		YamlMapping root = in.readYamlMapping();
		Level l = new Level(root.string("Level"), false);
    	
		
		l.completedLoading();
		return l;
    }
}
