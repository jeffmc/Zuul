package mcmillan.ecs;

public abstract class Component {
	
	private ECS ecs;
	public ECS getECS() { return ecs; }
	
	private long entity;
	public long getEntity() { return entity; }
	
	public Component(ECS parentECS, long parentEntity) {
		this.ecs = parentECS;
		this.entity = parentEntity;
	}
	
}
