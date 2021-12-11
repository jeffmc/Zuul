package mcmillan.engine.math;

import java.util.StringTokenizer;

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
	public IntTransform() {
		this(0,0,1,1);
	}
	public IntTransform(IntTransform o) {
		this();
		this.set(o);
	}
	public void set(int x, int y, int w, int h) {
		position.set(x, y);
		scale.set(w, h);
	}
	public void set(IntTransform o) {
		position.set(o.position);
		scale.set(o.scale);
	}

	public String toString() {
		return "["+ position.toString() + " & " + scale.toString() + "]";
	}
	public static IntTransform parseIntTransform(String src) {
		StringTokenizer tkn = new StringTokenizer(src.trim(), "[]&"); // TODO: Fix with regex
		return new IntTransform(
				Int2.parseInt2(tkn.nextToken()), 
				Int2.parseInt2(tkn.nextToken()));
	}
}
