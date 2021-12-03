package zuul;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;

import zuul.math.Int2;
import zuul.renderer.Renderer;
import zuul.world.Level;
import zuul.world.Room;

public class Editor {
	private JFrame frame;
	
	private Level activeLevel;
	private LevelCanvas lc;
	
	private Renderer renderer;
	
	private Room selectedRoom;
	
	private static final int EDITOR_SIZE = 768;
	
	public Editor(Level activeLevel) {
		frame = new JFrame("Zuul Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLayout(new GridBagLayout());
		renderer = new Renderer();
		
		makeGui();
		frame.pack();
		
		setActiveLevel(activeLevel);
		selectedRoom = null;
	}
	
	public Editor() {
		this(null);
	}
	
	private void makeGui() {
		Insets pdg = new Insets(3,3,3,3);
		
		GridBagConstraints lcc = new GridBagConstraints();
		lcc.gridx = 0;
		lcc.gridy = 0;
		lcc.gridwidth = 3;
		lcc.gridheight = 3;
		lcc.insets = pdg;
		lc = new LevelCanvas(this, new Dimension(EDITOR_SIZE,EDITOR_SIZE), Color.BLACK, renderer);
		frame.add(lc, lcc);
		
		GridBagConstraints jbc = new GridBagConstraints();
		jbc.gridx = 0;
		jbc.gridy = 3;
		jbc.gridwidth = 1;
		jbc.gridheight = 1;
		jbc.fill = GridBagConstraints.BOTH;
		jbc.insets = pdg;
		JButton jb = new JButton("Test button");
		frame.add(jb, jbc);
	}
	
	public void start() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void setActiveLevel(Level l) {
		lc.setActiveLevel(l);
		activeLevel = l;
	}
	
	public Level getActiveLevel() {
		return activeLevel;
	}

	public Room selectRoom(Room newSelection) {
		if (selectedRoom != null)
			selectedRoom.getRenderable().material.fill = null;
		selectedRoom = newSelection;
		if (selectedRoom != null)
			selectedRoom.getRenderable().material.fill = Color.RED;
		return selectedRoom;
	}
	public Room selectRoom(Int2 worldCoords) {
		return selectRoom(activeLevel.getRoom(worldCoords));
	}
}
