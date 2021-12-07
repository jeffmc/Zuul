package zuul.scene;

import mcmillan.ecs.ECS;
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
	
	public Entity newEntity(String tag) {
		return new Entity(this, ecs.newEntity(tag));
	}
	
	public void runTest() {
		ecs.runTest();
	}
	
	public static Scene levelToScene(Level in) {
		Scene scene = new Scene(in.getName());
		for (Room r : in.getRooms()) {
			Entity e = scene.newEntity(r.getName());
		}
		scene.runTest();
		
		return scene;
	}
	
}
