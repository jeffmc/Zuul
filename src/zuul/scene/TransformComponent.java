package zuul.scene;

import mcmillan.ecs.Component;
import mcmillan.ecs.ECS;
import zuul.math.IntTransform;

public class TransformComponent extends Component {
	public IntTransform transform;
	
	public TransformComponent(ECS parentECS, long parentEntity, IntTransform t) {
		super(parentECS, parentEntity);
		this.transform = t;
	}
	
	public TransformComponent(ECS parentECS, long parentEntity) {
		this(parentECS, parentEntity, new IntTransform());
	}
	
	@Override
	public String toString() {
		return transform.toString();
	}
}
