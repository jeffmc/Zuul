package mcmillan.editor;

import java.io.File;

public class AssetExplorerWindow {
	
	private static final String indent = " ";
	
	public static void test() {
		printFile(new File("C:\\Users\\Spooderman\\Documents\\"));
	}
	
	public static void printFile(boolean isRoot, File f, String tab) {
		if (isRoot) System.out.println(f.getAbsolutePath() + ":");
		if (f.isDirectory()) {
			if (!isRoot) System.out.println(tab + f.getName() + ":");
			File[] children = f.listFiles();
			if (children != null)
				for (File ff : children)
					printFile(false, ff, tab + indent);
		} else {
			System.out.println(tab + f.getName());
		}
	}
	
	public static void printFile(File f) {
		printFile(true, f, "");
	}
}
