package mcmillan.engine.scene;

import java.awt.Color;

import mcmillan.engine.ecs.Component;
import mcmillan.engine.ecs.ECSRegistry;
import mcmillan.engine.math.Int2;

// Operates completely independent of TransformComponent, not influenced at all.
public class LineRendererComponent extends Component {

	public Color color;
	public Int2 a, b;
	
	public LineRendererComponent(ECSRegistry parentECS, long parentEntity, Color color, Int2 a, Int2 b) {
		super(parentECS, parentEntity);
		this.color = color;
		this.a = a;
		this.b = b;
	}
	
	public LineRendererComponent(ECSRegistry parentECS, long parentEntity) {
		this(parentECS, parentEntity, new Color(0,0,0), new Int2(), new Int2());
	}
	
	@Override
	public String toString() {
		return LineRendererComponent.class.getSimpleName() + ": " + a.toString() + " to " + b.toString();
	}

}
