package mcmillan.engine.zui;

import java.awt.Graphics;

import mcmillan.engine.core.Application;
import mcmillan.engine.core.Layer;
import mcmillan.engine.core.Timestep;
import mcmillan.engine.renderer.Renderer;

public class ZUILayer extends Layer {

	public ZUILayer() {
		super("ZUILayer");
	}

	@Override
	public void onAttach() {

	}

	@Override
	public void onDetach() {

	}

	@Override
	public void onUpdate(Timestep ts) {

	}

	@Override
	public void OnZUIRender(Timestep ts) {

	}
	
	public void begin() {
		Renderer.beginFrame(Application.instance().window().getViewport());
	}
	
	public void end() {
		Renderer.endFrame();
		Graphics g = Application.instance().window().getGraphics();
		Renderer.drawFrame(g);
		g.dispose();
	}

}
