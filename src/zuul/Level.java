package zuul;

import java.util.List;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;

public class Level {
	private List<Room> rooms; // List of all rooms in the level.
	private Room spawnRoom; // Room to spawn in, start the player in.
	
	public Level(String name) {
		
	}
    
    public static void save(Level l) {
    	YamlMapping yaml = Yaml.createYamlMappingBuilder()
    		    .add("architect", "amihaiemil")
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
}
