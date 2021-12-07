package mcmillan.ecs;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.Random;
import java.util.Set;

public final class ECS {

	private static Random random = new Random();
	
	private String label;
	public String getLabel() { return label; }
	
	private Set<Long> entities;
	private Set<Component> components;
	
	private Map<Class<? extends Component>, Set<Component>> componentMap;
	private Map<Long, Set<Component>> entityComponentMap;
	
	public ECS(String label) {
		this.label = label;
		
		entities = new HashSet<>();
		components = new HashSet<>();
		
		componentMap = new HashMap<Class<? extends Component>, Set<Component>>();
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

	public boolean hasComponent(Class<? extends Component> comp, long entity) {
		Set<Component> eComps = entityComponentMap.get(entity); // Get all components in entity.
		for (Component c : eComps) // Iterate all components in entity
			if (comp.isInstance(c)) return true; // Return true if component instance of component type.
		return false; // Return false at end
	}

	// Returns null if component not found
	public <T extends Component> T getComponent(Class<T> comp, long entity) {
		T result = getComponentOrNull(comp, entity);
		if (result != null) return result;
		throw new IllegalStateException("Entity doesn't have component of type: " + comp.getName());
	}
	
	// Returns null if component not found
	private <T extends Component> T getComponentOrNull(Class<T> comp, long entity) {
		Set<Component> eComps = entityComponentMap.get(entity); // Get all components in entity.
		for (Component c : eComps) // Iterate over components
			if (comp.isInstance(c)) return comp.cast(c); // If component is instance of type, cast and return.
		return null;
	}
	
	public boolean entityExists(long entity) { return entities.contains(entity); } // Does entity exist
	
	// TODO: Add remove component, view<Component, Component> methods, etc.
	public <T extends Component> T addComponent(Class<T> comp, long entity, Object... otherArgs) {
		if (hasComponent(comp, entity)) throw new IllegalStateException("Entity already has component of type: " + comp.getTypeName());

		final int OFFSET = 2;
		Class<?>[] argTypes = new Class<?>[otherArgs.length+OFFSET];
		Object[] args = new Object[otherArgs.length+OFFSET];
		args[0] = this; // TODO: Think about removing component polymorphism.
		args[1] = entity;
		argTypes[0] = ECS.class;
		argTypes[1] = long.class;
		for (int i=0;i<otherArgs.length;i++) {
			args[i+OFFSET] = otherArgs[i];
			argTypes[i+OFFSET] = otherArgs[i].getClass();
		}
		
		try {
			Constructor<T> con = comp.getConstructor(argTypes); // Find constructor that matches passed args.
			T newComp = con.newInstance(args); // Make instance of component using found constructor.
			
			entityComponentMap.get(entity).add(newComp); // Add component to entityComponentMap
			
			if (componentMap.containsKey(comp)) { // Add to existing componentMap set
				componentMap.get(comp).add(newComp);
			} else { // Add to new componentMap set
				Set<Component> newSet = new HashSet<>();
				newSet.add(newComp);
				componentMap.put(comp, newSet);
			}
			return newComp; // Return newly constructed component.
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
	
	public boolean removeComponent(Class<? extends Component> compType, long entity) {
		Component comp = getComponentOrNull(compType, entity); // TODO: Fix this function
		boolean removedFromECMap = entityComponentMap.get(entity).remove(comp);
		boolean removedFromCMap = componentMap.get(compType).remove(comp);
		boolean removedFromCSet = components.remove(comp);
		return (removedFromECMap && removedFromCMap && removedFromCSet);
	}

	@SafeVarargs
	public final ECS.View view(Class<? extends Component>... componentTypes) {
		return new ECS.View(this, componentTypes);
	}
	public class View {
		
		private ECS ecs;
		public ECS getECS() { return ecs; }
		private Class<? extends Component>[] componentTypes;
		
		private Set<Long> entities;
		private Map<Long, Component[]> results;
		
		public View(ECS ecs, Class<? extends Component>[] componentTypes) { // TODO: Add caching
			if (componentTypes == null) throw new IllegalArgumentException("Parameter componentTypes is null!");
			if (componentTypes.length < 1) throw new IllegalArgumentException("Parameter componentTypes has a length < 1!");

			this.ecs = ecs;
			this.componentTypes = componentTypes;
			
			entities = new HashSet<>();
			results = new HashMap<>();
			
			for (Component c : componentMap.get(componentTypes[0])) {
				long e = c.getEntity();
				entities.add(e);
				Component[] cArr = new Component[componentTypes.length];
				cArr[0] = c;
				results.put(e, cArr);
			};
			for (int i=1;i<componentTypes.length;i++) {
				final int x = i;
				entities.removeIf(new Predicate<Long>() {
					@Override public boolean test(Long e) {
						Component c = ecs.getComponentOrNull(componentTypes[x], e);
						if (c == null) {
							return true;
						} else {
							results.get(e)[x] = c;
							return false;
						}
					}
				});
			}
		}
	}
	public void runTest() {
		System.out.println("ECS TEST:");
		for (Entry<Long, Set<Component>> e : entityComponentMap.entrySet()) 
			System.out.println(getComponentOrNull(TagComponent.class, e.getKey()).tag);
		System.out.println();
		
	}
}
