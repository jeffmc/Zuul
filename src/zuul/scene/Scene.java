package zuul.scene;

import java.awt.Color;
import java.awt.Graphics;

import mcmillan.ecs.Component;
import mcmillan.ecs.ECS;
import zuul.SceneEditor;
import zuul.math.Int2;
import zuul.math.IntTransform;
import zuul.renderer.Material;
import zuul.renderer.RenderCommand;
import zuul.renderer.Renderer;
import zuul.world.Level;
import zuul.world.Room;

public class Scene {

	private String label;
	public String getLabel() { return label; }
	
	private ECS ecs;
	
	public Scene(String label) {
		this.label = label;
		ecs = new ECS(label);
	}
	
	public Entity newEntity(String tag, IntTransform t) {
		long entity = ecs.newEntity();
		ecs.addComponent(TagComponent.class, entity, tag == null ? "Entity" : tag.isBlank() ? "Entity" : tag); // Add tag component to entity
		ecs.addComponent(TransformComponent.class, entity, t);
		return new Entity(this, entity);
	}
	public Entity newEntity(String tag) { return newEntity(tag, new IntTransform()); }
	public Entity newEntity() { return newEntity(null, new IntTransform()); }
	
	public void runTest() {
		ECS.View view = ecs.view(TagComponent.class, TransformComponent.class);
		view.print();
	}
	
	public void render(Int2 camera) {
		Renderer.beginFrame();
		Int2 viewport = new Int2(SceneEditor.EDITOR_SIZE, SceneEditor.EDITOR_SIZE); // Viewport size
		// Pre-scene
		{
			
			// Fill background with background color specified in constructor.
			Renderer.submit(RenderCommand.setColor(new Color(0,0,0))); // TODO: Fix background parameter
			Renderer.submit(RenderCommand.fillRect(new IntTransform(new Int2(), viewport)));

			// Draw 4x4 white rect at center of board, mostly for debugging
			Renderer.submit(RenderCommand.setColor(Color.WHITE));
			Renderer.submit(RenderCommand.fillRect(
					new IntTransform(viewport.x/2-2, viewport.y/2-2, 4, 4)));
			
			Renderer.submit(RenderCommand.setColor(Color.WHITE));
			Renderer.submit(RenderCommand.drawString(camera.toString(), new Int2(5, viewport.y-5))); // Draw camera position in bottom-left corner.
			
			Renderer.submit(RenderCommand.setColor(new Color(255,0,0)));
			Renderer.submit(RenderCommand.fillRect(new IntTransform(10, 20, 50, 30)));
			Renderer.submit(RenderCommand.setColor(new Color(0,255,0)));
			Renderer.submit(RenderCommand.drawRect(new IntTransform(10, 20, 50, 30)));
			
			// Center canvas at camera coords TODO: Add camera zoom
			Renderer.submit(RenderCommand.centerAt(camera, viewport));
		}
		
		// Scene components
		{
			// Render BoxRenderComponents
			ECS.View view = ecs.view(TransformComponent.class, BoxRendererComponent.class);
			for (Component[] cs : view.getComponents()) {
				TransformComponent t = (TransformComponent) cs[0];
				BoxRendererComponent box = (BoxRendererComponent) cs[1];
				
				Int2 scale = t.transform.scale, position = t.transform.position;
				Material m = box.material;
				
				Int2 hscale = new Int2(scale).div(2);
				Int2 ulpos = new Int2(position).sub(hscale);
				if (m.fill != null) {
					Renderer.submit(RenderCommand.setColor(m.fill));
					Renderer.submit(RenderCommand.fillRect(new IntTransform(ulpos, scale)));
				}
				if (m.stroke != null) {
					Renderer.submit(RenderCommand.setColor(m.stroke));
					Renderer.submit(RenderCommand.drawRect(new IntTransform(ulpos, scale)));
				}
			}
		}
		Renderer.endFrame();
	}
	
	public void draw(Graphics g) {
		Renderer.drawFrame(g);
	}
	
	public static Scene levelToScene(Level in) {
		Material regularMat = Material.stroke(Color.white), spawnMat = Material.stroke(Color.pink);
		Scene scene = new Scene(in.getName());
		for (Room r : in.getRooms()) {
			Entity e = scene.newEntity(r.getName(), r.getTransform());
			scene.ecs.addComponent(BoxRendererComponent.class, e.ecsID(), r.isSpawnpoint() ? spawnMat : regularMat);
		}
		// TODO: Add paths
		scene.runTest();
		return scene;
	}
	
}
