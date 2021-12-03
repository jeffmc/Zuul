package zuul.renderer;

import zuul.math.IntTransform;

public class Renderable {
	
	public Shape shape;
	public Material material;
	public IntTransform transform;
	
	public Renderable(Shape s, Material m, IntTransform transform) {
		this.shape = s;
		this.material = m;
		this.transform = transform;
	}
	
	public enum Shape {
		BOX, 
		LINE, // Line is special, position used as origin, and scale used as delta, fill is not supported
		OVAL;
	}
}
