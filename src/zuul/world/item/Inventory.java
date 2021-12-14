package zuul.world.item;

import java.util.HashSet;

import zuul.world.Room;

public class Inventory {
	// Room which this inventory belongs to, null if player inventory.
	private Room owner = null;
	public Room getOwner() { return owner; }
	
	private boolean isPlayerInventory = false;
	public boolean isPlayerInventory() { return isPlayerInventory; }
	
	private HashSet<Item> items = new HashSet<>();
	
	public Inventory(Room owner, Item... roomItems) {
		if (owner != null && owner.getInventory() == null) {
			this.owner = owner;
			this.isPlayerInventory = false;
			addItems(roomItems);
		}
	}
	
	public Inventory(boolean forPlayer, Item... playerItems) {
		if (forPlayer) {
			this.owner = null;
			this.isPlayerInventory = true;
			addItems(playerItems);
		} else {
			throw new IllegalArgumentException("Null-owner, non-player inventories not supported!");
		}
	}
	
	private void addItems(Item... newItems) {
		if (newItems != null) {
			for (Item i : newItems)
				this.items.add(i);
		}
	}
	
	public boolean hasItem(Item i) {
		return items.contains(i);
	}
	
	public void moveItemTo(Item i, Inventory target) {
		if (!this.hasItem(i)) throw new IllegalArgumentException("This inventory[" + 
				(isPlayerInventory ? "player" : owner.getName()) + 
				"] doesn't contain Item: " + i.getName()); 
		if (!items.remove(i)) throw new IllegalStateException("Inventory item array didn't contain item!");
		if (target.items.add(i));
		i.setInventory(target);
	}
	
	public String getAsString(String prefix) {
		String result = "";
		for (Item i : items)
			result += prefix + i.getName();
		return result;
	}
	
	// Print inventory contents (item names)
	public void print(String prefix) { // Prefix used for tab at start of list
		System.out.println(getAsString(prefix));
	}
	
	// Print without empty prefix
	public void print() {
		print("");
	}
	
}
