package zuul.math;

public class IntTransform {
	public Int2 position, scale;
	
	
	public IntTransform(Int2 position, Int2 scale) {
		this.position = position;
		this.scale = scale;
	}

	public IntTransform(int x, int y, int w, int h) {
		this.position = new Int2(x,y);
		this.scale = new Int2(w,h);
	}
	
	public IntTransform(Int2 position) {
		this(position, new Int2());
	}
	
	public void set(int x, int y, int w, int h) {
		position.set(x, y);
		scale.set(w, h);
	}
	public void set(IntTransform o) {
		position.set(o.position);
		scale.set(o.scale);
	}
}
