package zuul.world.item;

import java.util.HashSet;

import zuul.Game;
import zuul.world.Level;
import zuul.world.Room;

// Inventory used by Room objects and PlayerState objects, contains items.
// Items can be transferred from inventory to inventory, but never destroyed.

public class Inventory {
	// Room which this inventory belongs to, null if player inventory.
	private Room owner = null;
	public Room getOwner() { return owner; }
	
	// True if this inventory belongs to the player, if so, the Room owner field should be null.
	private boolean isPlayerInventory = false;
	public boolean isPlayerInventory() { return isPlayerInventory; }
	
	// Level that contains this inventory.
	private Level level;
	
	// A set of the items within this inventory.
	private HashSet<Item> items = new HashSet<>();
	
	// Room inventory constructor.
	public Inventory(Room owner, Item... roomItems) {
		if (owner != null && owner.getInventory() == null) {
			this.owner = owner;
			this.level = owner.getLevel();
			this.isPlayerInventory = false;
			addItems(roomItems);
		}
	}
	
	// Player inventory constructor.
	public Inventory(Level l, boolean forPlayer, Item... playerItems) {
		if (forPlayer) {
			this.level = l;
			this.owner = null;
			this.isPlayerInventory = true;
			addItems(playerItems);
		} else {
			throw new IllegalArgumentException("Null-owner, non-player inventories not supported!");
		}
	}
	
	// Only called outside in Item constructor.
	protected void addItems(Item... newItems) {
		if (newItems != null) {
			for (Item i : newItems) {
				i.setInventory(this);
				this.items.add(i);
				level.add(i);
			}
		}
	}
	
	// Returns true if this inventory contains item.
	public boolean hasItem(Item i) {
		return items.contains(i);
	}
	
	// Moves item to the target inventory, as long as conditions are met
	public void moveItemTo(Item i, Inventory target) {
		if (target.items.contains(i)) throw new IllegalStateException("Target inventory already contains item!"); // Target doesn't already contain.
		if (!this.hasItem(i)) throw new IllegalArgumentException("This inventory[" + // If origin inventory doesn't contain.
				(isPlayerInventory ? "player" : owner.getName()) + 
				"] doesn't contain Item: " + i.getName()); 
		items.remove(i); // Remove item from this inventory.
		target.items.add(i); // Add item to target inventory.
		i.setInventory(target); // Set parentInventory to target.
	}
	
	// Get contents of inventory, separated by "\n" and preceded by prefix argument.
	public String getAsString(String prefix) {
		if (isEmpty()) {
			return prefix + "No items!";
		} else {
			String result = "";
			for (Item i : items)
				result += prefix + i.getName() + "\n";
			return result;
		}
	}
	
	// Print inventory contents (item names) to console.
	public void print(String prefix) { // Prefix used for tab at start of list
		System.out.println(getAsString(prefix));
	}
	
	// Print this inventoryt to console without empty prefix
	public void print() {
		print("");
	}
	
	// Allows any-case string to be parsed into a reference to an existing item within this inventory.items Set.
	public Item getItemFromString(String name) {
		for (Item i : items)
			if (Game.blurryMatch(i.getName(), name))
				return i;
		return null;
	}
	
	// Return true if the inventory doesn't contain any items.
	public boolean isEmpty() {
		return items.size() < 1;
	}
	
}
