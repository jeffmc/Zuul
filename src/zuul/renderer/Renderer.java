package zuul.renderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import zuul.math.Int2;
import zuul.scene.Scene;

public class Renderer {
	
	private Rectangle bounds;
	
	private Scene activeScene;
	
//	 TODO: ADD TEXT SUPPORT TO NEW RENDERER!
	public Renderer() {
		activeScene = null;
	}
	
	public void setActiveScene(Scene scene) {
		activeScene = scene;
	}
	
	public Scene getActiveScene() { return activeScene; }
	
	public void drawElements(Graphics g, Color bg, Int2 camera) {
		// Get canvas bounds
		bounds = g.getClipBounds();
		
		// Fill background with background color specified in constructor.
		g.setColor(bg);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		// Draw 4x4 white rect at center of board, mostly for debugging
		g.setColor(Color.white);
		g.fillRect(bounds.width/2-2, bounds.height/2-2, 4, 4);
		
		g.drawString(camera.toString(), 5, bounds.height-5); // Draw camera position in bottom-left corner.
		// Center canvas at camera coords TODO: Add camera zoom
		centerAt(g, camera);
		
		// TODO: RenderScene
		
	}
	
	
	private void drawElement(Graphics g, Renderable r) {
		Material m = r.material;
		Int2 pos = r.transform.position, scale = r.transform.scale;
		Int2 hscale, ulpos;
		switch (r.shape) {
		case BOX:
			hscale = new Int2(scale).div(2);
			ulpos = new Int2(pos).sub(hscale);
			if (m.fill != null) {
				g.setColor(m.fill);
				g.fillRect(ulpos.x, ulpos.y, scale.x, scale.y);
			}
			if (m.stroke != null) {
				g.setColor(m.stroke);
				g.drawRect(ulpos.x, ulpos.y, scale.x, scale.y);
			}
			break;
		case LINE: // Fill not supported for LINE
			if (m.stroke != null) {
				g.setColor(m.stroke);
				g.drawLine(pos.x, pos.y, scale.x, scale.y);
			}
			break;
		case OVAL:
			hscale = new Int2(scale).div(2);
			ulpos = new Int2(pos).sub(hscale);
			if (m.fill != null) {
				g.setColor(m.fill);
				g.fillOval(ulpos.x, ulpos.y, scale.x, scale.y);
			}
			if (m.stroke != null) {
				g.setColor(m.stroke);
				g.drawOval(ulpos.x, ulpos.y, scale.x, scale.y);
			}
			break;
		}
	}
	
//	public void submit(Renderable r) {
//	}
	
	private void centerAt(Graphics g, Int2 center) {
		g.translate(-center.x+bounds.width/2,-center.y+bounds.height/2);
	}
}
