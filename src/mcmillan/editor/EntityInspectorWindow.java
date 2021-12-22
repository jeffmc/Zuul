package mcmillan.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import mcmillan.engine.ecs.Component;
import mcmillan.engine.scene.Entity;
import mcmillan.engine.scene.TagComponent;

public class EntityInspectorWindow {

	private static final String TITLE = "Entity Inspector";

	private EditorLayer editorLayer; // Parent editor layer (used for selectedEntity)
	
	private JFrame frame = new JFrame(); // Window
	private JPanel panel = new JPanel(); // Contains all component panels

	private Entity selectedEntity = null;
	
	private Component[] components = new Component[0];
	private ComponentPanel<? extends Component>[] panels = new ComponentPanel<?>[0];
	
	public void setSelectedEntity(Entity e) { 
		selectedEntity = e;
		panel.removeAll();
		if (selectedEntity != null) {
			frame.setTitle(TITLE + " - " + selectedEntity.getComponent(TagComponent.class).tag);
			components = selectedEntity.getComponents().toArray(Component[]::new);
		} else {
			components = new Component[0];
			panels = new ComponentPanel<?>[0];
			frame.setTitle(TITLE);
		}
		setupComponentPanels();
		frame.pack();
		frame.revalidate();
		frame.repaint();
	}
	
	public EntityInspectorWindow(EditorLayer editorLayer, Entity e) {
		this.editorLayer = editorLayer;
		
		// Init frame
		Dimension d = new Dimension(300, 450);
		frame.setMinimumSize(d);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		// Tree setup
		frame.add(new JScrollPane(panel), BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		// Set scene
		setSelectedEntity(e);
		
		// Present frame
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	public void dispose() {
		frame.setVisible(false);
		frame.dispose();
		frame = null;
		panel = null;
		editorLayer.disposedEntityInspector();
	}
	
	private void setupComponentPanels() {
		panel.removeAll();
		if (components.length > 0) {
			panels = new ComponentPanel<?>[components.length];
			for (int i=0;i<components.length;i++) {
				ComponentPanel<Component> cp = new ComponentPanel<Component>(components[i]);
				panels[i] = cp;
				panel.add(panels[i]);
			}
		} else {
			panel.add(new JLabel("Nothing selected."));
			panel.revalidate();
			panel.repaint();
		}
	}
	
	public class ComponentPanel<T extends Component> extends JPanel {
		private T component;
		public T getComponent() { return component; }
		
		private SpringLayout layout = new SpringLayout();
		
		public ComponentPanel(T component) {
			super();
//			this.setLayout(layout);
			this.component = component;
			this.setBorder(BorderFactory.createTitledBorder(component.getClass().getSimpleName()));
			
			for (Field f : component.getClass().getDeclaredFields()) {
				addField(f);
			}
		}
		
		private void addField(Field f) {
			JLabel fieldName = new JLabel(f.getName()); java.awt.Component value;
			this.add(fieldName);
//			layout.putConstraint(SpringLayout.WEST, fieldName, 5, SpringLayout.WEST, this);
			try {
				Class<?> type = f.getType();
				if (type == String.class) {
					value = new JTextField((String)f.get(component)); 
				} else if (type == Color.class) {
					JColorChooser jcc = new JColorChooser((Color)f.get(component));
					value = jcc;
				} else {
					value = new JLabel(f.get(component).toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				value = new JLabel("ERROR!"); 
			}
			this.add(value);
		}
	}
	
}
