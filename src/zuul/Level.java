package zuul;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlPrinter;

public class Level {
	private String name;
	private List<Room> rooms; // List of all rooms in the level.
	private Room spawnRoom; // Room to spawn in, start the player in.
	
	public Level(String name) {
		this.name = name;
		rooms = new ArrayList<Room>();
	}

	public void setSpawn(Room r) { // Checks if this level contains room first, then assigns spawn to room.
		if (rooms.contains(r)) {
			spawnRoom = r;
		} else throw new IllegalArgumentException("Level " + name + " does not contain the spawn room provided: " + r.getShortDescription());
	}
	
	public Room getSpawn() { // Return spawn room
		return spawnRoom;
	}
	
	public void add(Room r) {
		rooms.add(r);
		r.setLevel(this);
	}
	
	public void add(Room... rs) {
		for (int i=0;i<rs.length;i++) add(rs[i]);
	}
	
	public void repaint() {
		// Call repaint on GUI elements.
	}

	public String getName() {
		return name;
	}
	
	private void setName(String name) {
		this.name = name;
	}
	
	// https://github.com/decorators-squad/eo-yaml/wiki/Block-Style-YAML
    public static void save(Level l, File f) {
    	// Add real saving
    	Room r = l.getSpawn();
    	YamlMapping yaml = Yaml.createYamlMappingBuilder()
    		    .add("Level", l.getName())
    		    .add(
    		        "Rooms",
    		        Yaml.createYamlMappingBuilder()
    		        	.add(r.getName(), Yaml.createYamlMappingBuilder()
    		        		.add("Description", r.getShortDescription())
    		        		.add("Exits", Yaml.createYamlMappingBuilder()
    		        				.add("west", "Lab")
    		        				.build())
    		        		.add("X", Byte.toString(r.getX()))
    		        		.add("Y", Byte.toString(r.getY()))
    		        		.add("Width", Byte.toString(r.getWidth()))
    		        		.add("Height", Byte.toString(r.getHeight()))
    		        		.build()
    		        		).build()
    		    ).build();
    	System.out.println(yaml);
    	
		try {
			YamlPrinter printer = Yaml.createYamlPrinter(
				    new FileWriter(f)
				);
	    	printer.print(yaml);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static Level load(File f) {
    	Level l = new Level(null);
    	l.setName(null);
    	
    	// Add file loading and YAML parsing
		return l;
    }
    
}
