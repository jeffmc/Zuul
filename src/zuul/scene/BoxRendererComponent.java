package zuul.scene;

import mcmillan.ecs.Component;
import mcmillan.ecs.ECS;
import zuul.renderer.Material;

public class BoxRendererComponent extends Component {

	public Material material;
	
	public BoxRendererComponent(ECS parentECS, long parentEntity, Material m) {
		super(parentECS, parentEntity);
		this.material = m;
	}
	
	@Override
	public String toString() {
		return BoxRendererComponent.class.getName();
	}
}
