import java.awt.Color;
import java.awt.FocusTraversalPolicy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SecretService {
	
	private String containerFilename = "D:/JavaRainbow/kiss/Images/IMAGE2.bmp";
	private String secretFilename = "D:/JavaRainbow/kiss/Images/SECRETIMAGE2.bmp";
	
	private String resultFileName = "D:/JavaRainbow/kiss/Images/RESULTIMAGE2.bmp";
	private String secretResultFileName = "D:/JavaRainbow/kiss/Images/SECRETRESULT2.bmp";
	
	private int[] key;
	
	private int secretHeight;
	private int secretWidth;
	
	
	private MyImage containerImage;
	private MyImage secretImage;
	
	private MyImage postContainerImage;
	private MyImage postSecretImage;
	
	
	private Color[] containerPixels;
	private Color[] secretPixels;
	
	private Color[] postContainerPixels;
	private Color[] postSecretPixels;
	
	
	
	public void start() throws Exception {
		
		hideImage();
		findImage();
		testQuality();
		
		
		
	}
	
	private void getPix() {
		
		for(int i = 0; i < 100; i++) {
			System.out.println("Secret: " + secretPixels[i]);
			System.out.println("PostSecret: " + postSecretPixels[i]);
		}
		
	}
	
	
	private void hideImage() throws Exception {
		
		containerImage = new MyImage(containerFilename);
		secretImage = new MyImage(secretFilename);
		
		containerPixels = containerImage.getPixels();
		secretPixels = secretImage.getPixels();
		
		secretHeight = secretImage.getHeight();
		secretWidth = secretImage.getWidth();
		
		int maxSpace = (containerPixels.length / secretPixels.length)/3;
		
		if(maxSpace < 1) {
			throw new Exception("Your secret image is too big/container image is too small");
		}
		
		fillKey(maxSpace, secretPixels.length);
		
		System.out.println("Max: " + maxSpace);
		
		//for(int i = 0; i < secretPixels.length; i++) {
		
		int position = 0;
		for(int i = 0; i < secretPixels.length; i++) {
	
			containerPixels[position * 3] = fillPixel(i, 'r', position * 3);
			containerPixels[position * 3 + 1] = fillPixel(i, 'g', position * 3 + 1);
			containerPixels[position * 3 +2] = fillPixel(i, 'b', position * 3 + 2);
			
			position += key[i];
			
		}
	
		BufferedImage image = containerImage.getBufferedImage();
		
		postContainerPixels = containerPixels;
		saveImage(containerPixels, image, resultFileName);
		
	}
	
	private void fillKey(int maxSpace, int length) {
		
		key = new int[length];
		
		for(int i = 0; i < length; i++) {
			key[i] = (int)(Math.random() * maxSpace) + 1;
			
		}
		 
		
	}

	
	private Color fillPixel(int i, char ch, int position) {
		
		int r = containerPixels[position].getRed();
		int g = containerPixels[position].getGreen();
		int b = containerPixels[position].getBlue();
		
		r = r & 252;  // 01011111 01011100   // 11111100
		g = g & 252;
		b = b & 252;
		
		//0&0 = 0   0&1 = 0  1&1 = 1        1&1 = 1  0&0 = 0
				
		
		int secretColor = 0;
		
		switch(ch) {
		case 'r':
			secretColor = secretPixels[i].getRed();
			break;

		case 'g':
			secretColor = secretPixels[i].getGreen();
			break;

		case 'b':
			secretColor = secretPixels[i].getBlue();
			break;
		}
		
		// 11111111

		r = r | (secretColor >> 6 & 3);
		g = g | (secretColor >> 4 & 3);
	    b = b | (secretColor >> 2 & 3);
	  
		return new Color(r, g, b);

	}
	
	
	private void findImage() throws IOException {
		
		postContainerImage = new MyImage(resultFileName);
		postContainerPixels = postContainerImage.getPixels();
		
		BufferedImage secretResult = new BufferedImage(secretWidth, secretHeight, BufferedImage.TYPE_INT_RGB);
		postSecretPixels = new Color[secretWidth * secretHeight];
		
		int position = 0;
		
		for(int i = 0; i < postSecretPixels.length; i++) {
			
			int r = getColor(position * 3);
			int g = getColor(position * 3+1);
			int b = getColor(position * 3+2);
			
			postSecretPixels[i] = new Color(r, g, b); 
			
			position += key[i];
			
		}
		
		saveImage(postSecretPixels, secretResult, secretResultFileName);
		
		
	}
	
	private int getColor(int i) {
		
		int r = postContainerPixels[i].getRed();
		int g = postContainerPixels[i].getGreen();
		int b = postContainerPixels[i].getBlue();
		
		r = r & 3;
		g = g & 3;
		b = b & 3;
		
		r = r << 6;
		r = r | g << 4;
		r = r | b << 2;
	
		
		return r;
	
	}
	
	private void saveImage(Color[] pixels, BufferedImage image, String filename) throws IOException {

		int k = 0;
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
					image.setRGB(j, i, pixels[k].getRGB());
					k++;

			}

		}

		ImageIO.write(image, "bmp", new File(filename));

	}
	
	
	
	private void testQuality() throws IOException {
		
		containerImage = new MyImage(containerFilename);
		postContainerImage = new MyImage(resultFileName);
		secretImage = new MyImage(secretFilename);
		postSecretImage = new MyImage(secretResultFileName);
		
		containerPixels = containerImage.getPixels();
		postContainerPixels = postContainerImage.getPixels();
		secretPixels = secretImage.getPixels();
		postSecretPixels = postSecretImage.getPixels();
		
		getPix();
		
		double sum = 0;
		
		for(int i = 0; i < containerPixels.length; i++) {
			int r = containerPixels[i].getRed() - postContainerPixels[i].getRed();
			int g = containerPixels[i].getGreen() - postContainerPixels[i].getGreen();
			int b = containerPixels[i].getBlue() - postContainerPixels[i].getBlue();
			sum += Math.pow((Math.abs(r) + Math.abs(g) + Math.abs(b)), 2);
		}
		
		sum = sum / containerPixels.length;
		
		
		System.out.println("MSE container): " + sum);
		
		sum = 10 * Math.log10(255*255/sum);
		
		System.out.println("PSNR container): " + sum);
		
		sum = 0;
		
		for(int i = 0; i < secretPixels.length; i++) {
			int r = secretPixels[i].getRed() - postSecretPixels[i].getRed();
     		int g = secretPixels[i].getGreen() - postSecretPixels[i].getGreen();
			int b = secretPixels[i].getBlue() - postSecretPixels[i].getBlue();
			sum += Math.pow((Math.abs(r) + Math.abs(g) + Math.abs(b)), 2);
			
		}
		
		sum = sum / secretPixels.length;
		
		
		System.out.println("MSE secret): " + sum);
		
		sum = 10 * Math.log10(255*255 / sum);

		System.out.println("PSNR secret): " + sum);
		
		
		
		
	}
	

}
