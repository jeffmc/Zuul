package zuul.scene;

import mcmillan.engine.ecs.Component;
import mcmillan.engine.ecs.ECSRegistry;

public class TagComponent extends Component {

	public String tag;
	
	public TagComponent(ECSRegistry ecs, long entity, String tag) {
		super(ecs, entity);
		this.tag = tag;
	}
	
	@Override
	public String toString() {
		return tag;
	}
	
}
