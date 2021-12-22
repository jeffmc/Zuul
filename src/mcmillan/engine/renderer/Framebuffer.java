package mcmillan.engine.renderer;

import java.awt.image.BufferedImage;

import mcmillan.engine.math.Int2;

public class Framebuffer {
	
	public Int2 size;
	private BufferedImage image;
	
//	private static int initCount = 0;
	
	public Framebuffer(Int2 size) {
//		System.out.println("Framebuffers: " + ++initCount);
		this.size = size;
		updateFramebufferSize();
	}
	
	public void updateFramebufferSize() {
		image = new BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_ARGB);
		image.setAccelerationPriority(1.0f);
	}
	
	public BufferedImage getImage() {
		return image;
	}
}
