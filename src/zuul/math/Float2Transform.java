package zuul.math;

public class Float2Transform {
	public Float2 position, scale;
	
	public Float2Transform(Float2 position, Float2 scale) {
		this.position = position;
		this.scale = scale;
	}

	public Float2Transform(float x, float y, float w, float h) {
		this.position = new Float2(x,y);
		this.scale = new Float2(w,h);
	}
	
	public Float2Transform(Float2 position) {
		this(position, new Float2());
	}
	public Float2Transform() {
		this(0,0,1,1);
	}
	public void set(float x, float y, float w, float h) {
		position.set(x, y);
		scale.set(w, h);
	}
}
