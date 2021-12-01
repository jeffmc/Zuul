package zuul;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlMappingBuilder;
import com.amihaiemil.eoyaml.YamlPrinter;

public class Level {
	// Serialized
	private String name;
	private List<Room> rooms; // List of all rooms in the level.
	private Room spawnRoom; // Room to spawn in, start the player in.
	
	// Not serialized
	private Signal repaintSignal;
    private boolean loadingComplete; // Do not trigger repaints while this is true
	
	public Level(String name, boolean loadingComplete) {
		this.name = name;
		rooms = new ArrayList<Room>();
		
		this.loadingComplete = loadingComplete;
		repaintSignal = new Signal("Level repaint: " + name);
	}

	public Level(String name) {
		this(name, true);
	}
	
	public void completedLoading() {
		loadingComplete = true;
		repaint();
	}
	
	public void repaint() {
		if (loadingComplete)
			repaintSignal.run();
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

	public String getName() {
		return name;
	}
	
	private void setName(String name) {
		this.name = name;
	}
    
    public List<Room> getRooms() {
		return rooms;
	}
	
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

	public static Level load(File f) {
    	Level l = new Level(null);
    	l.setName(null);
    	
    	// Add file loading and YAML parsing
		return l;
    }
    
}
