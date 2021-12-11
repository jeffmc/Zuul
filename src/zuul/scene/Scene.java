package zuul.scene;

import java.awt.Color;
import java.util.Random;

import mcmillan.engine.ecs.Component;
import mcmillan.engine.ecs.ECSRegistry;
import mcmillan.engine.math.Int2;
import mcmillan.engine.math.IntTransform;
import mcmillan.engine.renderer.Material;
import mcmillan.engine.renderer.RenderCommand;
import mcmillan.engine.renderer.Renderer;
import zuul.world.Level;
import zuul.world.Path;
import zuul.world.Room;

public class Scene {

	private String label;
	public String getLabel() { return label; }
	
	public ECSRegistry ecs;
	
	public Scene(String label) {
		this.label = label;
		ecs = new ECSRegistry(label);
	}
	
	public Entity newEntity(String tag, IntTransform t) {
		long entity = ecs.newEntity();
		ecs.addComponent(TagComponent.class, entity, tag == null ? "Entity" : tag.isBlank() ? "Entity" : tag); // Add tag component to entity
		ecs.addComponent(TransformComponent.class, entity, t);
		return new Entity(this, entity);
	}
	public Entity newEntity(String tag) { return newEntity(tag, new IntTransform()); }
	public Entity newEntity() { return newEntity(null, new IntTransform()); }
	
	public void render(Int2 viewport, Int2 camera) {
		// Pre-scene
		{
			// TODO: Do scene-specific background

			// Draw 4x4 white rect at center of board, mostly for debugging
			Renderer.submit(RenderCommand.setColor(Color.WHITE));
			Renderer.submit(RenderCommand.fillRect(
					new IntTransform(viewport.x/2-2, viewport.y/2-2, 4, 4)));
			
			// Center canvas at camera coords TODO: Add camera zoom
			Renderer.submit(RenderCommand.setColor(Color.WHITE));
			Renderer.submit(RenderCommand.drawString("Viewport: " + viewport.toString() + ", Camera: " + camera.toString(), new Int2(5, viewport.y-5))); // Draw camera position in bottom-left corner.
			
			// Camera translation
			Renderer.submit(RenderCommand.centerAt(camera, viewport));
		}
		
		// Scene components
		{
			// BoxRendererComponent
			ECSRegistry.View boxes = ecs.view(TransformComponent.class, BoxRendererComponent.class);
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
			ECSRegistry.View lines = ecs.view(/* TransformComponent.class, */LineRendererComponent.class); // As of now, TransformComponent has no effect on LineRendererComponent.
			for (Component[] cs : lines.getComponents()) {
//				TransformComponent t = (TransformComponent) cs[0];
				LineRendererComponent line = (LineRendererComponent) cs[0];
				Renderer.submit(RenderCommand.line(line));
			}
		}
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

	public Entity selectEntityAt(Int2 worldCoords) {
		return null; // TODO: Write selection algorithm
	}
	
}
