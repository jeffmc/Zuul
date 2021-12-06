package mcmillan.ecs;

public class TagComponent extends Component {

	public String tag;
	
	public TagComponent(ECS ecs, long entity, String tag) {
		super(ecs, entity);
		this.tag = tag;
	}
	
}
