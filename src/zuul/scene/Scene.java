package zuul.scene;

import java.awt.Color;
import java.awt.Graphics;

import mcmillan.ecs.Component;
import mcmillan.ecs.ECS;
import zuul.math.Int2;
import zuul.math.IntTransform;
import zuul.renderer.Material;
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
	
	public void draw(Graphics g) {
		
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
				g.setColor(m.fill);
				g.fillRect(ulpos.x, ulpos.y, scale.x, scale.y);
			}
			if (m.stroke != null) {
				g.setColor(m.stroke);
				g.drawRect(ulpos.x, ulpos.y, scale.x, scale.y);
			}
		}
	}
	
	public static Scene levelToScene(Level in) {
		Material regularMat = Material.stroke(Color.white), spawnMat = Material.stroke(Color.pink);
		Scene scene = new Scene(in.getName());
		for (Room r : in.getRooms()) {
			Entity e = scene.newEntity(r.getName(), r.getTransform());
			scene.ecs.addComponent(BoxRendererComponent.class, e.ecsID(), r.isSpawnpoint() ? spawnMat : regularMat);
		}
		scene.runTest();
		return scene;
	}
	
}
