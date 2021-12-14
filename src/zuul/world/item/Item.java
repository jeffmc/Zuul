package zuul.world.item;

public class Item {
	// Declaration and initialization of items.
	public static Item SWORD = new Item("Sword");
	
	// Registry of all items
	public static Item[] registry = { SWORD };
			
	private String name;
	public String getName() { return name; } // Returns item name.
	
	private Inventory parentInventory = null; // Reference to parent inventory.
	public Inventory getInventory() { return parentInventory; } // Returns parent inventory containing this item.
	protected void setInventory(Inventory i) { parentInventory = i; } // Called by Inventory.moveItemTo(...), and Inventory(Room, Item[]) constructor.
	
	public Item(String name) {
		this.name = name;
	}
	
	public void moveTo(Inventory i) {
		parentInventory.moveItemTo(this, i);
	}
	
	// Allows any-case string to be parsed into a reference to an existing item within Item.registry, or null if not found;
	public static Item getItemFromString(String name) {
		for (Item i : registry)
			if (i.getName().toLowerCase().equals(name.trim().toLowerCase()))
				return i;
		return null;
	}
	
	@Override
	public String toString() {
		return super.toString() + "['" + name + "']";
	}
	
}
