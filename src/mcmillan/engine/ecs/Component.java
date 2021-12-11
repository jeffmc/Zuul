package mcmillan.engine.ecs;

public abstract class Component {
	
	private ECSRegistry ecs;
	public ECSRegistry getECS() { return ecs; }
	
	private long entity;
	public long getEntity() { return entity; }
	
	public Component(ECSRegistry parentECS, long parentEntity) {
		this.ecs = parentECS;
		this.entity = parentEntity;
	}
	
}
