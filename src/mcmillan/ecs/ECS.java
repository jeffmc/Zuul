package mcmillan.ecs;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ECS {
	
	private static Random random = new Random();
	
	private String label;
	public String getLabel() { return label; }
	
	private Set<Long> entities;
	private Set<Component> components;
	
	private Map<Class<? extends Component>, Component> componentMap;
	private Map<Long, Set<Component>> entityComponentMap;
	
	public ECS(String label) {
		this.label = label;
		
		entities = new HashSet<>();
		components = new HashSet<>();
		
		componentMap = new HashMap<Class<? extends Component>, Component>();
		entityComponentMap = new HashMap<Long, Set<Component>>();
	}
	
	public long newEntity(String tag) {
		Long e = random.nextLong(); // Generate entity
		entityComponentMap.put(e, new HashSet<>()); // Generate component map for entity
		if (!entities.add(e)) throw new IllegalStateException("ECS RANDOMLY GENERATED SAME LONG TWICE!"); // Add entity to entity set
		addComponent(TagComponent.class, e, tag == null ? "Entity" : tag.isBlank() ? "Entity" : tag); // Add tag component to entity
		return e; // Return new entity.
	}
	
	public long newEntity() { return newEntity(null); }

	public <T extends Component> boolean hasComponent(Class<T> comp, long entity) {
		Set<Component> eComps = entityComponentMap.get(entity); // Get all components in entity.
		for (Component c : eComps)
			if (comp.isInstance(c)) return true;
		return false;
	}
	
	public <T extends Component> T getComponent(Class<T> comp, long entity) {
		Set<Component> eComps = entityComponentMap.get(entity); // Get all components in entity.
		for (Component c : eComps) // Iterate over components
			if (comp.isInstance(c)) return comp.cast(c); // If component is instance of type, cast and return.
		throw new IllegalStateException("Entity doesn't have component of type: " + comp.getTypeName());
	}
	
	public boolean entityExists(long entity) { return entities.contains(entity); }
	
	// TODO: Add remove component, view<Component, Component> methods, etc.
	public <T extends Component> T addComponent(Class<T> comp, long entity, Object... otherArgs) {
		if (hasComponent(comp, entity)) throw new IllegalStateException("Entity already has component of type: " + comp.getTypeName());
		Class<?>[] argTypes = new Class<?>[otherArgs.length+2];
		Object[] args = new Object[otherArgs.length+2];
		args[0] = this;
		args[1] = entity;
		argTypes[0] = ECS.class;
		argTypes[1] = long.class;
		for (int i=0;i<otherArgs.length;i++) {
			args[i+2] = otherArgs[i];
			argTypes[i+2] = otherArgs[i].getClass();
		}
		try {
			Constructor<T> con = comp.getConstructor(argTypes);
			T newComp = con.newInstance(args);
			entityComponentMap.get(entity).add(newComp);
			return newComp;
		} catch (Exception e) {
			throw new IllegalArgumentException(comp.getName() + " doesn't accept " + getArgTypesString(argTypes) + " as constructor parameters: ", e);
		}
	}
	
	public static String getArgTypesString(Class<?>[] argTypes) {
		String[] typeNames = new String[argTypes.length];
		for (int i=0;i<argTypes.length;i++)
			typeNames[i] = argTypes[i].getName();
		return "[" + String.join(", ", typeNames) + "]";
	}
}
