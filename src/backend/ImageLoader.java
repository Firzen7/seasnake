package backend;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageLoader {
	private static List<BufferedImage> originalImages
			= new ArrayList<BufferedImage>();
	private static List<BufferedImage> scaledImages
			= new ArrayList<BufferedImage>();

	private static boolean loaded = false;
	private static int width = 0;
	private static int height = 0;
	
	public static void scaleImages(int width, int height) {
		if(!areScaled(width, height)) {
			scaledImages = new ArrayList<BufferedImage>();
			
			for(BufferedImage el : originalImages) {
				scaledImages.add(scaleImage(el, width, height));
			}
		}
	}
	
	public static void loadImages() {
		if(!loaded) {
			originalImages = new ArrayList<BufferedImage>();
			
			originalImages.add(loadImage("res/body.png"));
			originalImages.add(loadImage("res/head.png"));
			originalImages.add(loadImage("res/coral.png"));
			originalImages.add(loadImage("res/fish.png"));
			originalImages.add(loadImage("res/starfish.png"));
			originalImages.add(loadImage("res/turtle.png"));
			
			if(width == 0 && height == 0) {
				scaleImages(40, 40);
			}
			
			loaded = true;
		}
	}
	
	private static boolean areScaled(int width, int height) {
		return (ImageLoader.width == width && ImageLoader.height == height);
	}
	
	private static BufferedImage scaleImage(BufferedImage image, int width,
			int height) {
		if(image != null) {
			Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			return toBufferedImage(scaled);
		}
		else {
			return null;
		}
	}
	
	// zkopírováno z GEJ (Game Engine for Java)
	private static BufferedImage toBufferedImage(Image img){
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		return bimage;
	}
	
	public static BufferedImage getBodyImage() {
		return scaledImages.get(0);
	}

	public static BufferedImage getHeadImage() {
		return scaledImages.get(1);
	}

	public static BufferedImage getWallImage() {
		return scaledImages.get(2);
	}

	public static BufferedImage getSmallFoodImage() {
		return scaledImages.get(3);
	}

	public static BufferedImage getShrinkerImage() {
		return scaledImages.get(4);
	}

	public static BufferedImage getBigFoodImage() {
		return scaledImages.get(5);
	}
	
	public static BufferedImage loadImage(String filename) {
		filename = Useful.getActualPath() + "/" + filename;
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(filename));
		} catch (IOException e) {

		}
		
		return image;
	}
}
