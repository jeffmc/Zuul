package zuul.scene;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import mcmillan.ecs.Component;
import mcmillan.ecs.ECS;
import zuul.math.Int2;
import zuul.math.IntTransform;
import zuul.renderer.Material;
import zuul.renderer.RenderCommand;
import zuul.renderer.Renderer;
import zuul.world.Level;
import zuul.world.Path;
import zuul.world.Room;

public class Scene {

	private String label;
	public String getLabel() { return label; }
	
	public ECS ecs;
	
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
	
	public void render(Int2 camera) {
		Renderer.beginFrame();
		Int2 viewport = new Int2(Renderer.viewportWidth(), Renderer.viewportHeight()); // Viewport size
		// Pre-scene
		{
			
			// Fill background with background color specified in constructor.
			Renderer.submit(RenderCommand.setColor(new Color(0,0,0))); // TODO: Fix background parameter
			Renderer.submit(RenderCommand.fillRect(new IntTransform(new Int2(), viewport)));

			// Draw 4x4 white rect at center of board, mostly for debugging
			Renderer.submit(RenderCommand.setColor(Color.WHITE));
			Renderer.submit(RenderCommand.fillRect(
					new IntTransform(viewport.x/2-2, viewport.y/2-2, 4, 4)));
			
			
			// Center canvas at camera coords TODO: Add camera zoom
			Renderer.submit(RenderCommand.setColor(Color.WHITE));
			Renderer.submit(RenderCommand.drawString(camera.toString(), new Int2(5, viewport.y-5))); // Draw camera position in bottom-left corner.
			
			// Camera translation
			Renderer.submit(RenderCommand.centerAt(camera, viewport));
		}
		
		// Scene components
		{
			// BoxRendererComponent
			ECS.View boxes = ecs.view(TransformComponent.class, BoxRendererComponent.class);
			for (Component[] cs : boxes.getComponents()) {
				TransformComponent t = (TransformComponent) cs[0];
				BoxRendererComponent box = (BoxRendererComponent) cs[1];
				
				Renderer.submit(RenderCommand.box(t, box));
				Material m = box.material;
				IntTransform transform = new IntTransform(t.transform);
				transform.position.sub(Int2.div(transform.scale, 2));
				if (m.fill != null) {
					Renderer.submit(RenderCommand.setColor(m.fill));
					Renderer.submit(RenderCommand.fillRect(transform));
				}
				if (m.stroke != null) {
					Renderer.submit(RenderCommand.setColor(m.stroke));
					Renderer.submit(RenderCommand.drawRect(transform));
				}
			}
			// LineRendererComponent
			ECS.View lines = ecs.view(/* TransformComponent.class, */LineRendererComponent.class); // As of now, TransformComponent has no effect on LineRendererComponent.
			for (Component[] cs : lines.getComponents()) {
//				TransformComponent t = (TransformComponent) cs[0];
				LineRendererComponent line = (LineRendererComponent) cs[0];
				Renderer.submit(RenderCommand.line(line));
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
			Entity e = scene.newEntity("Room " + r.getName(), r.getTransform());
			scene.ecs.addComponent(BoxRendererComponent.class, e.ecsID(), r.isSpawnpoint() ? spawnMat : regularMat);
		}
		System.out.println("Path Count: "+in.getPaths().size());
		for (Path p : in.getPaths()) {
			// TODO: Add paths
			Random r = new Random();
			Entity e = scene.newEntity("Path: " + p.getA().getName() + " - "+ p.getB().getName());
			int v = r.nextInt(256);
			scene.ecs.addComponent(LineRendererComponent.class, e.ecsID(), 
					new Color(v, 255-v, 128+v/2), 
					p.getA().getPosition(), p.getB().getPosition());
		}
		return scene;
	}
	
}
