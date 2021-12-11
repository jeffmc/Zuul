package mcmillan.engine.renderer;

import java.awt.Color;

public class Material {
	public Color fill, stroke;
	
	private Material(Color f, Color s) {
		fill = f;
		stroke = s;
	}
	public static Material fill(Color f) {
		return new Material(f, null);
	}
	public static Material stroke(Color s) {
		return new Material(null, s);
	}
	public static Material fillAndStroke(Color f, Color s) {
		return new Material(f, s);
	}
}
