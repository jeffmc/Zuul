package zuul.editor;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import mcmillan.ecs.Component;
import zuul.scene.Scene;
import zuul.scene.TagComponent;

public class SceneViewer {

	private Scene activeScene;
	
	private JFrame frame = new JFrame("Scene");
	private JTree tree = new JTree();
	
	public Scene getActiveScene() { return activeScene; }
	public void setActiveScene(Scene s) { 
		activeScene = s; 
		if (activeScene != null) {
			tree.setModel(new DefaultTreeModel(new SceneTreeNode(activeScene)));
		} else {
			tree.setModel(null);
		}
	}
	
	public SceneViewer(Scene s) {
		Dimension d = new Dimension(500, 300);
		frame.setMinimumSize(d);
		frame.setPreferredSize(d);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(new JScrollPane(tree));
		
		tree.setRootVisible(false);
		setActiveScene(s);
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
	
	public SceneViewer() {
		this(null);
	}
	
	public void dispose() {
		
	}
	
	interface ECSCommonTreeNode extends TreeNode {}
	
	class SceneTreeNode implements ECSCommonTreeNode {

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
	
	class EntityTreeNode implements ECSCommonTreeNode {
		private Scene scene;
		private ECSCommonTreeNode parent;
		private long entityId;
		private TagComponent tc;
		private ArrayList<DefaultMutableTreeNode> components = new ArrayList<>();
		
		class ComponentTreeNode extends DefaultMutableTreeNode {
			private Component c;
			public ComponentTreeNode(Component c) { super(); this.c = c; }
			@Override public String toString() { return c.getClass().getSimpleName() + ": " + c.toString(); }
		}
		
		public EntityTreeNode(ECSCommonTreeNode parent, Scene scene, long entityId, TagComponent tc) {
			this.parent = parent;
			this.scene = scene;
			this.entityId = entityId;
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
	}
	
	/*
	class EntityTreeNode implements ECSCommonTreeNode {
		private Scene scene; 
		private ECSCommonTreeNode parent;
		private long entityId;
		private TagComponent tag;
		
		public EntityTreeNode(ECSCommonTreeNode parent, Scene scene, long entityId, TagComponent tag) {
			this.parent = parent;
			this.scene = scene;
			this.entityId = entityId;
			this.tag = tag;
		}
		
		@Override
		public String toString() {
			return this.tag.tag;
		}
		
		// TODO: Nested entities
		@Override public TreeNode getChildAt(int childIndex) { return null; }
		@Override public int getChildCount() { return 0; }
		@Override public TreeNode getParent() { return parent; }
		@Override public int getIndex(TreeNode node) { return 0; }
		@Override public boolean getAllowsChildren() { return false; }
		@Override public boolean isLeaf() { return true; } 
		@Override public Enumeration<? extends TreeNode> children() { return null; }
	}
	*/
}
