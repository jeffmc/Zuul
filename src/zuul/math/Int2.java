package zuul.math;

public class Int2 {
	public int x,y;
	public Int2(int x,int y) {
		this.x = x;
		this.y = y;
	}
	public Int2() {
		this.x = 1;
		this.y = 1;
	}
	public static Int2 clone(Int2 o) {
		return new Int2(o.x, o.y);
	}

	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void set(Int2 o) {
		this.x = o.x;
		this.y = o.y;
	}
	
	public Int2 add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}
	public Int2 add(Int2 o) {
		x += o.x;
		y += o.y;
		return this;
	}
	public static Int2 add(Int2 a, Int2 b) {
		return new Int2(a.x + b.x, a.y + b.y);
	}

	public Int2 sub(int x, int y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	public Int2 sub(Int2 o) {
		x -= o.x;
		y -= o.y;
		return this;
	}
	public static Int2 sub(Int2 a, Int2 b) {
		return new Int2(a.x - b.x, a.y - b.y);
	}
	
	public Int2 mult(int x, int y) {
		this.x *= x;
		this.y *= y;
		return this;
	}
	public Int2 mult(Int2 o) {
		x *= o.x;
		y *= o.y;
		return this;
	}
	public static Int2 mult(Int2 a, Int2 b) {
		return new Int2(a.x * b.x, a.y * b.y);
	}

	public Int2 div(int x, int y) {
		this.x /= x;
		this.y /= y;
		return this;
	}
	public Int2 div(Int2 o) {
		x /= o.x;
		y /= o.y;
		return this;
	}
	public static Int2 div(Int2 a, Int2 b) {
		return new Int2(a.x / b.x, a.y / b.y);
	}
}
