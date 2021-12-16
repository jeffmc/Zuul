package zuul.util;

import zuul.world.Level;
import zuul.world.PlayerState;
import zuul.world.Room;
import zuul.world.item.Inventory;
import zuul.world.item.Item;
import zuul.world.path.ConditionalTwoWayPath;
import zuul.world.path.TwoWayPath;

// LevelMangager is used to create the level
// Initializes Room objects, Path objects, Item Objects, and combines them all into one Level object for Game instance to use.

// TODO: Add the file serializer again.

public class LevelManager {
	
	// Directional strings for path construction
    private static final String NORTH = "north", EAST = "east", SOUTH = "south", WEST = "west";
    
    // Make the default level.
	public static Level createDefaultLevel()
    {
		// Create level
    	Level l = new Level("Town");

        // Create the rooms
        Room moda, construction, warehouse, travel, townSq, mainSt, backAlley, 
        	techStore, grocery, nature, aptBldg, supplies, secondSt, bank, manuPlant;
        moda = new Room(l, "Moda Center", "in a large arena with tons of seats.");
        construction = new Room(l, "Construction Site", "in a dangerous place. You should find a hart hat.");
        warehouse = new Room(l, "Mysterious Warehouse", "unsettled. Nothing is in this massive place.");
        travel = new Room(l, "Travel Agency", "in a place from the past.");
        townSq = new Room(l, "Town Square", "in the hustle and bustle of this city, but it's now midnight.");
        mainSt = new Room(l, "Main Street", "on the busiest street in the city. Lined with shops");
        backAlley = new Room(l, "Back Alley", "now hidden away from the rest of this huge city.");
        techStore = new Room(l, "Tech Store", "fascinated by bright screens and blinking lights everywhere!");
        grocery = new Room(l, "Grocery Store", "surrounded by delicious foods...");
        nature = new Room(l, "Nature", "amidst trees and shrubbery, you can hear animals lurking.");
        aptBldg = new Room(l, "Apartment Building", "in the lobby.");
        supplies = new Room(l, "General Supplies", "in a room surrounded by a seemingly randomized set of goods, all useful.");
        secondSt = new Room(l, "Second St.", "on the second busiest street in this city.");
        bank = new Room(l, "Bank", "leering at gold.");
        manuPlant = new Room(l, "Manufacturing Plant", "surround by unfinished PCBs and there are complicated assembling robots everywhere.");
        // Set level spawn
        l.setSpawn(townSq);

        // Construct and place items
        Item key, gold, silver, copper, steel, aluminum;
        key = new Item(warehouse, "Key", true); // TODO: Add use
        gold = new Item(bank, "Gold"); // Gold ingot
        silver = new Item(supplies, "Silver"); // Silver coin
        copper = new Item(manuPlant, "Copper"); // Copper wiring
        steel = new Item(construction, "Steel"); // Steel I-beam
        aluminum = new Item(techStore, "Aluminum"); // Aluminum casing
        
        // Key Conditional
        PlayerState.Conditional kc = new PlayerState.Conditional() {
			@Override
			public boolean check(PlayerState ps) {
				return ps.hasItem(key);
			}
		};
		String kcm = "Need " + key.getName() + " to access this!";
        
        // Make paths
        { // First col
	        new TwoWayPath(warehouse, backAlley, EAST, WEST);
	        new TwoWayPath(construction, mainSt, EAST, WEST);
	        new TwoWayPath(moda, townSq, EAST, WEST);
        }
        { // Second col
        	new ConditionalTwoWayPath(techStore, backAlley, SOUTH, NORTH, kc, kcm);
        	new TwoWayPath(backAlley, mainSt, SOUTH, NORTH);
        	new TwoWayPath(mainSt, townSq, SOUTH, NORTH);
        	new TwoWayPath(townSq, travel, SOUTH, NORTH);
	        new TwoWayPath(backAlley, aptBldg, EAST, WEST);
	        new TwoWayPath(mainSt, nature, EAST, WEST);
	        new TwoWayPath(townSq, grocery, EAST, WEST);
        }
        { // Third col

        	new TwoWayPath(aptBldg, nature, SOUTH, NORTH);
	        new TwoWayPath(nature, secondSt, EAST, WEST);
        }
        { // Fourth col

        	new ConditionalTwoWayPath(bank, secondSt, SOUTH, NORTH, kc, kcm);
        	new ConditionalTwoWayPath(secondSt, supplies, SOUTH, NORTH, kc, kcm);
	        new ConditionalTwoWayPath(secondSt, manuPlant, EAST, WEST, kc, kcm);
        }
        
        // All post-init
        l.calcExits();
        // Make win condition
        l.setWinCondition(new PlayerState.Conditional() {
			@Override
			public boolean check(PlayerState ps) {
				Inventory pi = ps.getInventory(); // Check if player is holding all metals
				return (pi.hasItem(gold)&&
						pi.hasItem(silver)&&
						pi.hasItem(copper)&&
						pi.hasItem(steel)&&
						pi.hasItem(aluminum));
			}
		});
        return l;
    }
	
}
