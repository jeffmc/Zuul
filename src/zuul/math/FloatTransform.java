package zuul.math;

public class FloatTransform {
	public Float2 position, scale;
	
	public FloatTransform(Float2 position, Float2 scale) {
		this.position = position;
		this.scale = scale;
	}

	public FloatTransform(float x, float y, float w, float h) {
		this.position = new Float2(x,y);
		this.scale = new Float2(w,h);
	}
	
	public FloatTransform(Float2 position) {
		this(position, new Float2());
	}
	
	public void set(float x, float y, float w, float h) {
		position.set(x, y);
		scale.set(w, h);
	}
}
