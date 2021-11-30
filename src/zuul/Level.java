package zuul;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;

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
    	YamlMapping yaml = Yaml.createYamlMappingBuilder()
    		    .add("Level", l.getName())
    		    .add(
    		        "devops",
    		        Yaml.createYamlSequenceBuilder()
    		            .add("rultor")
    		            .add("0pdd")
    		            .build()
    		    ).add(
    		        "developers",
    		        Yaml.createYamlSequenceBuilder()
    		            .add("amihaiemil")
    		            .add("salikjan")
    		            .add("SherifWally")
    		            .build()
    		    ).build();
    	System.out.println(yaml);
    }
    
    public static Level load(File f) {
    	Level l = new Level(null);
    	l.setName(null);
    	
    	// Add file loading and YAML parsing
		return l;
    }

}
