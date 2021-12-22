package mcmillan.engine.scene;

import java.util.Set;

import mcmillan.engine.ecs.Component;

public class Entity {

	private long ecsID;
	public long ecsID() { return ecsID; }
	private Scene scene;
	public Scene getScene() { return scene; }
	
	public Entity(Scene scene, long ecsID) {
		this.scene = scene;
		this.ecsID = ecsID;
	}

	public <T extends Component> boolean hasComponent(Class<T> compCls) {
		return scene.ecs.hasComponent(compCls, ecsID);
	}

	public <T extends Component> T getComponentOrNull(Class<T> compCls) {
		return scene.ecs.getComponentOrNull(compCls, ecsID);
	}
	
	public <T extends Component> T getComponent(Class<T> compCls) {
		assert hasComponent(compCls);
		return scene.ecs.getComponent(compCls, ecsID);
	}
	
	public Set<Component> getComponents() {
		return scene.ecs.getComponents(ecsID);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Entity) {
			Entity e = (Entity) o;
			return (scene == e.scene && this.ecsID == e.ecsID);
		} else return false;
	}
	
	@Override
	public int hashCode() {
		return (int) (scene.hashCode() + ecsID);
	}
}
