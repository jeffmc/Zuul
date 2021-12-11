package zuul.scene;

import mcmillan.engine.ecs.Component;
import mcmillan.engine.ecs.ECSRegistry;
import mcmillan.engine.math.IntTransform;

public class TransformComponent extends Component {
	public IntTransform transform;
	
	public TransformComponent(ECSRegistry parentECS, long parentEntity, IntTransform t) {
		super(parentECS, parentEntity);
		this.transform = t;
	}
	
	public TransformComponent(ECSRegistry parentECS, long parentEntity) {
		this(parentECS, parentEntity, new IntTransform());
	}
	
	@Override
	public String toString() {
		return transform.toString();
	}
}
