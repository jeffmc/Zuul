package zuul.math;

public class Float2 {
	public float x,y;
	public Float2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public Float2() {
		this.x = 1;
		this.y = 1;
	}
	public Float2(Float2 o) {
		this.x = o.x;
		this.y = o.y;
	}
	public static Float2 clone(Float2 o) {
		return new Float2(o.x, o.y);
	}
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public void set(Float2 o) {
		this.x = o.x;
		this.y = o.y;
	}
	
	public Float2 add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	public Float2 add(Float2 o) {
		x += o.x;
		y += o.y;
		return this;
	}
	public static Float2 add(Float2 a, Float2 b) {
		return new Float2(a.x + b.x, a.y + b.y);
	}

	public Float2 sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	public Float2 sub(Float2 o) {
		x -= o.x;
		y -= o.y;
		return this;
	}
	public static Float2 sub(Float2 a, Float2 b) {
		return new Float2(a.x - b.x, a.y - b.y);
	}
	
	public Float2 mult(float x, float y) {
		this.x *= x;
		this.y *= y;
		return this;
	}
	public Float2 mult(float f) {
		this.x *= f;
		this.y *= f;
		return this;
	}
	public Float2 mult(Float2 o) {
		x *= o.x;
		y *= o.y;
		return this;
	}
	public static Float2 mult(Float2 a, Float2 b) {
		return new Float2(a.x * b.x, a.y * b.y);
	}

	public Float2 div(float x, float y) {
		this.x /= x;
		this.y /= y;
		return this;
	}
	public Float2 div(float f) {
		this.x /= f;
		this.y /= f;
		return this;
	}
	public Float2 div(Float2 o) {
		x /= o.x;
		y /= o.y;
		return this;
	}
	public static Float2 div(Float2 a, Float2 b) {
		return new Float2(a.x / b.x, a.y / b.y);
	}
}
