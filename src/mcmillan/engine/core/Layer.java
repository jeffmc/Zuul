package mcmillan.engine.core;

public abstract class Layer {
	
	private String name;
	public String getName() { return name; }
	
	public Layer(String name) {
		this.name = name;
	}
	
	public abstract void onAttach();
	public abstract void onDetach();
	public abstract void onUpdate(Timestep ts);

//	public abstract void OnEvent(ZEvent e);
	public abstract void OnZUIRender(Timestep ts);
	
	@Override
	public String toString() {
		return "Layer: " + name;
	}
	
}
