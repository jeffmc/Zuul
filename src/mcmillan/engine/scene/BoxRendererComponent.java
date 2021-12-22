package mcmillan.engine.scene;

import mcmillan.engine.ecs.Component;
import mcmillan.engine.ecs.ECSRegistry;
import mcmillan.engine.renderer.Material;

public class BoxRendererComponent extends Component {

	public Material material;
	
	public BoxRendererComponent(ECSRegistry parentECS, long parentEntity, Material m) {
		super(parentECS, parentEntity);
		this.material = m;
	}
	
	@Override
	public String toString() {
		return "Box, " + material.toString();
	}
}
