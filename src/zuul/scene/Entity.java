package zuul.scene;

public class Entity {

	private long ecsID;
	public long ecsID() { return ecsID; }
	private Scene scene;
	public Scene getScene() { return scene; }
	
	public Entity(Scene scene, long ecsID) {
		this.scene = scene;
		this.ecsID = ecsID;
	}
}
