package zuul.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlInput;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlMappingBuilder;
import com.amihaiemil.eoyaml.YamlNode;
import com.amihaiemil.eoyaml.YamlPrinter;

import zuul.world.Level;
import zuul.world.Room;

public class LevelManager {

	// TODO: Serialize paths instead of exits
	// TODO: Add CSV color scalar
	
	// https://github.com/decorators-squad/eo-yaml/wiki/Block-Style-YAML
    public static void save(Level l, File f) {
    	// Create level mapping
    	YamlMappingBuilder level = Yaml.createYamlMappingBuilder()
    		    .add("LevelName", l.getName())
    		    .add("Spawn", l.getSpawn().getName());
    	
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

	public static Level load(File f) {
		try {
			// Setup Yaml input
			YamlInput in = Yaml.createYamlInput(f);
			YamlMapping root = in.readYamlMapping();
			
			// Make level object
			Level level = new Level(root.string("LevelName"), false);
			
			// Parse rooms into the level as Room objects and into exitMapping to be parsed later
	    	YamlMapping rooms = root.yamlMapping("Rooms");
	    	Map<Room, YamlMapping> exitMapping = new HashMap<>();
	    	for (YamlNode k : rooms.keys()) {
	    		YamlMapping r = rooms.yamlMapping(k);
	    		Room room = new Room(
	    				k.asScalar().value(),
	    				r.string("Description"),
	    				r.integer("X"),
	    				r.integer("Y"),
	    				r.integer("Width"),
	    				r.integer("Height")
					);
	    		level.add(room);
	    		exitMapping.put(room, r.yamlMapping("Exits"));
	    	}
	    	
	    	// Set spawn by finding in level via name.
	    	Room spawnRoom = level.getRoom(root.string("Spawn"));
	    	level.setSpawn(spawnRoom);
	    	spawnRoom.updateSpawnStatus();
	    	
	    	// Parse through exit mappings and apply to Room objects in level
	    	for (Entry<Room, YamlMapping> e : exitMapping.entrySet()) {
	    		Room room = e.getKey();
	    		YamlMapping exits = e.getValue();
	        	for (YamlNode d : exits.keys()) {
	        		String roomName = exits.string(d);
	        		Room exitRoom = level.getRoom(roomName);
	        		if (exitRoom == null) throw new IOException("Invalid exit for '" + room.getName() + "': '" + roomName + "'!");
	        		room.setExit(d.asScalar().value(), exitRoom);
	        	}
	    	}
	    	for (Room r : level.getRooms())
				r.calcPaths();
	    	System.out.println(level.getPaths().size() + " total paths!");
			level.completedLoading();
			return level;
		} catch (IOException e) {
			System.err.println("Error reading level file: " + f.getAbsolutePath());
		}
		return null;
    }
}
