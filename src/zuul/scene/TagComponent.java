package zuul.scene;

import mcmillan.ecs.Component;
import mcmillan.ecs.ECS;

public class TagComponent extends Component {

	public String tag;
	
	public TagComponent(ECS ecs, long entity, String tag) {
		super(ecs, entity);
		this.tag = tag;
	}
	
	@Override
	public String toString() {
		return tag;
	}
	
}
