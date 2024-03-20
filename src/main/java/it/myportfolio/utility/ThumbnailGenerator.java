package it.myportfolio.utility;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;



public class ThumbnailGenerator {
	
	public static void makeThumbnail(String photoUrl, String thumbnailUrl) throws IOException {
		
		Path thumbnailPath = Paths.get(thumbnailUrl);
		Path thumbnailBasePath = thumbnailPath.getParent();
	
		File outputFolder = new File(thumbnailBasePath.toString());
       
        if (!outputFolder.exists()) {
        	outputFolder.mkdirs();
        }
        BufferedImage originalImage = ImageIO.read(new File(photoUrl));
      
        int lengthThumbnail = (int) (originalImage.getWidth() * 0.3);
        int widthThumbnail = (int) (originalImage.getHeight() * 0.3);
       
        BufferedImage thumbnail = new BufferedImage(lengthThumbnail, widthThumbnail, BufferedImage.TYPE_INT_RGB);
    
        Graphics2D g2 = thumbnail.createGraphics();
        g2.drawImage(originalImage, 0, 0, lengthThumbnail, widthThumbnail, null);
        g2.drawImage(originalImage.getScaledInstance(widthThumbnail, lengthThumbnail, Image.SCALE_SMOOTH), 0, 0, null);
        g2.dispose();
        
        ImageIO.write(thumbnail, "jpg", new File(thumbnailPath.toString()));
    }
}


