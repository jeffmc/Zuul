package mcmillan.editor;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import mcmillan.engine.ecs.Component;
import zuul.scene.Entity;
import zuul.scene.Scene;
import zuul.scene.TagComponent;

public class SceneExplorerWindow {
	private static final String TITLE = "Scene Viewer";
			
	private EditorLayer editorLayer;
	
	private JFrame frame = new JFrame();
	private JTree tree = new JTree();
	
	private Entity selectedEntity;
	public Entity getSelectedEntity() { return selectedEntity; }
	public void setSelectedEntity(Entity e) {
		selectedEntity = e;
		if (e != null) {
		} else {
			tree.setSelectionPath(null);
		}
	}
	
	private Scene activeScene;
	public Scene getActiveScene() { return activeScene; }
	public void setActiveScene(Scene s) { 
		if (s == null) throw new IllegalArgumentException("New scene is null!");
		activeScene = s; 
		tree.setModel(new DefaultTreeModel(new SceneTreeNode(activeScene)));
		frame.setTitle(TITLE + " - " + activeScene.getLabel());
	}
	
	public SceneExplorerWindow(EditorLayer editorLayer, Scene s) {
		this.editorLayer = editorLayer;
		
		// Init frame
		Dimension d = new Dimension(500, 300);
		frame.setMinimumSize(d);
		frame.setPreferredSize(d);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		
		// Tree setup
		frame.add(new JScrollPane(tree));
		tree.setRootVisible(false);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); // TODO: Add multi-entity selection
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override public void valueChanged(TreeSelectionEvent e) {
				Object leaf = e.getPath().getLastPathComponent();
				if (leaf instanceof EntityTreeNode) {
					EntityTreeNode etn = EntityTreeNode.class.cast(leaf);
					editorLayer.selectEntity(etn.entity);
				}
			}
		});
		
		// Set scene
		setActiveScene(s);
		
		// Present frame
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	public void dispose() {
		frame.setVisible(false);
		frame.dispose();
		frame = null;
		tree = null;
		editorLayer.disposedSceneExplorer();
	}
	
	public interface ECSCommonTreeNode extends TreeNode {}
	public class SceneTreeNode implements ECSCommonTreeNode {
		private Scene scene;
		private ArrayList<EntityTreeNode> entities = new ArrayList<>();
		
		public SceneTreeNode(Scene scene) { // TODO: MAKE THIS OBSERVE THE SCENE
			this.scene = scene;
			for (Entry<Long, Component[]> i : this.scene.ecs.view(TagComponent.class).getResults().entrySet()) {
				entities.add(new EntityTreeNode(this, scene, i.getKey(), (TagComponent) i.getValue()[0]));
			}
		}
		@Override public TreeNode getChildAt(int childIndex) { return entities.get(childIndex); } 
		@Override public int getChildCount() { return entities.size(); }
		@Override public TreeNode getParent() { return null; }
		@Override public int getIndex(TreeNode node) { return entities.indexOf(node); }
		@Override public boolean getAllowsChildren() { return true; }
		@Override public boolean isLeaf() { return false; } 
		@Override public Enumeration<? extends TreeNode> children() { return Collections.enumeration(entities); }
	}
	public class EntityTreeNode implements ECSCommonTreeNode {
		private ECSCommonTreeNode parent;
		private Entity entity;
		private TagComponent tc;
		
		public EntityTreeNode(ECSCommonTreeNode parent, Scene scene, long entityId, TagComponent tc) {
			this.parent = parent;
			this.entity = new Entity(scene, entityId);
			this.tc = tc;
		}
		
		@Override
		public String toString() {
			return this.tc.tag;
		}
		@Override public TreeNode getChildAt(int childIndex) { return null; } 
		@Override public int getChildCount() { return 0; }
		@Override public TreeNode getParent() { return parent; }
		@Override public int getIndex(TreeNode node) { return -1; }
		@Override public boolean getAllowsChildren() { return false; }
		@Override public boolean isLeaf() { return true; } 
		@Override public Enumeration<? extends TreeNode> children() { return null; }
	}
	
	/*public class EntityTreeNode implements ECSCommonTreeNode { // TODO: Implement nested entities in Scene
		private Scene scene;
		private ECSCommonTreeNode parent;
		private long entityId;
		private Entity entity;
		private TagComponent tc;
		private ArrayList<DefaultMutableTreeNode> components = new ArrayList<>();
		
		public class ComponentTreeNode extends DefaultMutableTreeNode {
			private Component c;
			public ComponentTreeNode(Component c) { super(); this.c = c; }
			@Override public String toString() { return c.getClass().getSimpleName() + ": " + c.toString(); }
		}
		
		public EntityTreeNode(ECSCommonTreeNode parent, Scene scene, long entityId, TagComponent tc) {
			this.parent = parent;
			this.scene = scene;
			this.entityId = entityId;
			this.entity = new Entity(scene, entityId);
			this.tc = tc;
			for (Component c : this.scene.ecs.getComponents(this.entityId)) {
				components.add(new ComponentTreeNode(c));
			}
		}
		
		@Override
		public String toString() {
			return this.tc.tag;
		}
		@Override public TreeNode getChildAt(int childIndex) { return components.get(childIndex); } 
		@Override public int getChildCount() { return components.size(); }
		@Override public TreeNode getParent() { return parent; }
		@Override public int getIndex(TreeNode node) { return components.indexOf(node); }
		@Override public boolean getAllowsChildren() { return true; }
		@Override public boolean isLeaf() { return false; } 
		@Override public Enumeration<? extends TreeNode> children() { return Collections.enumeration(components); }
	}*/
}
