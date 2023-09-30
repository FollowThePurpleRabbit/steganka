import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MyImage {
	
	private String filename;
	private int height;
	private int width;
	private BufferedImage bufferedImage;
	private Color[] pixels;
	
	public Color[] getPixels() {
		return pixels;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public MyImage(String filename) throws IOException {
		bufferedImage = ImageIO.read(new File(filename));
		System.out.println(bufferedImage == null);
		height = bufferedImage.getHeight();
		width = bufferedImage.getWidth();
		pixels = getPixels(bufferedImage, height, width);
		
//		System.out.println(pixels[0].toString());
//		System.out.println(height);
//		System.out.println(width);
		
	}
	
	private Color[] getPixels(BufferedImage image, int height, int width) {
		
		int size = height * width;
		Color[] pixels = new Color[size];
		
		
		int currentPixel = 0;
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				pixels[currentPixel] = new Color(image.getRGB(j, i));
				currentPixel++;
			}
		}
		
		return pixels;
		
		
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

	
	
//	public static BufferedImage getBuffFromPixels(Color[] pixs) {
//		
//		 BufferedImage bi = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
//		 for (int i = 0; i < height; i++)
//             for (int j = 0; j < width; j++)
//                 bi.setRGB(j, i, pixs[i*width +j].getRGB());
//		
//		 
//         return bi;
//         
//         
//		
//	}
	

}
