package zuul.util;

public class LevelManager {
/*
	// TODO: Add CSV color scalar, similar to Int2 and IntTransform
	
	// https://github.com/decorators-squad/eo-yaml/wiki/Block-Style-YAML
    public static void save(Level l, File f) {
    	// Create root mapping
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
		        		.add("Transform", r.getTransform().toString())
		        		.build());
    	}
    	// Add rooms mapping to root mapping
	    level = level.add("Rooms", rooms.build());

    	// Create paths mapping
	    YamlSequenceBuilder paths = Yaml.createYamlSequenceBuilder();
	    for (Path p : l.getPaths()) {
			YamlMappingBuilder builder = Yaml.createYamlMappingBuilder();
	    	switch (p.getType()) {
			case ONE_WAY:
				OneWayPath owp = OneWayPath.class.cast(p);
				boolean isBlind = owp.isChildBlind();
				builder = builder
					.add("Type", p.getType().name())
					.add("Parent", p.getA().getName())
					.add("Child", p.getB().getName())
					.add("IsBlind", Boolean.toString(isBlind))
					.add("ParentName", p.getAName());
				if (!isBlind) builder = builder.add("ChildName", p.getBName());
				break;
			case TWO_WAY:
				TwoWayPath twp = TwoWayPath.class.cast(p);
				builder = builder
					.add("Type", p.getType().name())
					.add("A", p.getA().getName())
					.add("B", p.getB().getName())
					.add("AName", p.getAName())
					.add("BName", p.getBName());
				break;
			case CONDITIONAL:
			default:
				throw new IllegalStateException("Unsupported Path Type: " + p.getType());
	    	}
			paths = paths.add(builder.build());
	    }
    	// Add paths mapping to root mapping
	    level = level.add("Paths", paths.build());
    	
	    // Build level mapping and output
    	YamlMapping yaml = level.build("Zuul Level File, by Jeff McMillan - https://github.com/jeffmc/Zuul");
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

	public static Level load(File f) { // TODO: Remove exit loading
		try {
			// Setup Yaml input
			YamlInput in = Yaml.createYamlInput(f);
			YamlMapping root = in.readYamlMapping();
			
			// Make level object
			Level level = new Level(root.string("LevelName"), false);
			
			// Parse rooms into the level as Room objects and into exitMapping to be parsed later
	    	YamlMapping rooms = root.yamlMapping("Rooms");
	    	for (YamlNode k : rooms.keys()) {
	    		YamlMapping r = rooms.yamlMapping(k);
	    		Room room = new Room(
	    				k.asScalar().value(),
	    				r.string("Description"),
	    				IntTransform.parseIntTransform(r.string("Transform"))
					);
	    		level.add(room);
	    	}
	    	
	    	// Set spawn by finding in level via name.
	    	Room spawnRoom = level.getRoom(root.string("Spawn"));
	    	level.setSpawn(spawnRoom);
	    	
	    	YamlSequence paths = root.yamlSequence("Paths");
	    	for (YamlNode k : paths.values()) {
	    		YamlMapping r = YamlMapping.class.cast(k);
	    		String strType = r.string("Type");
	    		Path.PathType type = Path.PathType.valueOf(strType);
	    		Path path;
	    		switch (type) {
				case ONE_WAY:
					boolean isBlind = Boolean.valueOf(r.string("IsBlind"));
					path = new OneWayPath(
							level.getRoom(r.string("Parent")),
							level.getRoom(r.string("Child")), 
							r.string("ParentName"),
							isBlind?null:r.string("ChildName"),
							isBlind);
					break;
				case TWO_WAY:
					path = new TwoWayPath(
							level.getRoom(r.string("A")),
							level.getRoom(r.string("B")), 
							r.string("AName"),
							r.string("BName"));
					break;
				case CONDITIONAL:
				default:
					throw new IllegalStateException("Unsupported Path Type: " + type);
	    		}
	    		level.add(path);
	    	}
	    	
	    	for (Room r : level.getRooms())
				r.calcExits();
			level.completedLoading();
			return level;
		} catch (IOException e) {
			System.err.println("Error reading level file: " + f.getAbsolutePath());
		}
		return null;
    }*/
}
