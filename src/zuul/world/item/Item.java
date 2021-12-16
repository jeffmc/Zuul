package zuul.world.item;

import zuul.world.Room;

// Items can be transferred from inventory to inventory, but never destroyed.
// All items are stored within an inventory, whether player or room.

public class Item {
	private String name; // Name of item, used in console output often (NEEDS TO BE SINGLE WORD NOW) TODO: Multi-word item names?
	public String getName() { return name; } // Returns item name.
	
	private Inventory parentInventory = null; // Reference to parent inventory.
	public Inventory getInventory() { return parentInventory; } // Returns parent inventory containing this item.
	protected void setInventory(Inventory i) { parentInventory = i; } // Called by Inventory.moveItemTo(...), and Inventory(Room, Item[]) constructor.
	
	private boolean undroppable; // True if the player is prevented from dropping this item once picked up.
	public boolean isUndroppable() { return undroppable; } // Getter
	
	// Constructor for Item
	public Item(Inventory newInventory, String name, boolean undroppable) {
		this.name = name;
		this.undroppable = undroppable;
		newInventory.addItems(this); // Add this item to parentInventory.
	}
	
	// Overloaded constructors
	public Item(Room room, String name, boolean undroppable) {
		this(room.getInventory(), name, undroppable);
	}
	public Item(Room room, String name) {
		this(room.getInventory(), name, false);
	}
	
	// Move this item to a new inventory, calls parentInventory.moveItemTo
	public void moveTo(Inventory i) {
		parentInventory.moveItemTo(this, i);
	}
	
	// Override toString for easier debugging, outputted string now contains item name.
	@Override
	public String toString() {
		return super.toString() + "['" + name + "']";
	}
	
}
