package it.myportfolio.utility;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import it.myportfolio.model.ImageProject;



public class ThumbnailGenerator {
	
	
	 public static void makeThumbnail(ImageProject image) throws IOException {
	        String photoUrl = image.getURL();
	        String thumbnailUrl = image.getThumbnailURL();
	        String ext = FilenameUtils.getExtension(thumbnailUrl);
	        Path thumbnailPath = Paths.get(thumbnailUrl);
	        
	        Path thumbnailBasePath = thumbnailPath.getParent();
	        File outputFolder = new File(thumbnailBasePath.toString());

	        if (!outputFolder.exists()) {
	            System.out.println("Creating directories for the thumbnail.");
	            outputFolder.mkdirs();
	        }
	        System.out.println(photoUrl);
	        BufferedImage originalImage = ImageIO.read(new File(photoUrl));
	      
	        int widthThumbnail = (int) (originalImage.getWidth() * 0.3);
	        int heightThumbnail = (int) (originalImage.getHeight() * 0.3);
	       
	        BufferedImage thumbnail = new BufferedImage(widthThumbnail, heightThumbnail, BufferedImage.TYPE_INT_RGB);
	    
	        Graphics2D g2 = thumbnail.createGraphics();
	        g2.drawImage(originalImage, 0, 0,widthThumbnail, heightThumbnail, null);
	        g2.drawImage(originalImage.getScaledInstance(widthThumbnail, heightThumbnail, Image.SCALE_DEFAULT), 0, 0, null);
	        g2.dispose();
	        
	        // Add watermark to the thumbnail
	        thumbnail = addTextWatermark(thumbnail);
	        
	        if (thumbnail != null) {
	            ImageIO.write(thumbnail, ext, new File(thumbnailPath.toString()));
	        } else {
	            System.err.println("Failed to add watermark to the thumbnail.");
	        }
	    }

	 
	    public static BufferedImage addTextWatermark(BufferedImage sourceImage) {
	        try {
	            String text = "© MyPortfolio 2024";
	            Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();

	            // initializes necessary graphic properties
	            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8F);
	            g2d.setComposite(alphaChannel);
	            g2d.setColor(Color.RED);
	            g2d.setFont(new Font("Arial", Font.BOLD, 32));
	            FontMetrics fontMetrics = g2d.getFontMetrics();
	            Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);

	            // calculates the coordinate where the String is painted
	            int centerX = (sourceImage.getWidth() - (int) rect.getWidth()) / 2;
	            int centerY = (int) ((int)sourceImage.getHeight() / 1.1F);

	            // paints the textual watermark
	            g2d.drawString(text, centerX, centerY);
	            g2d.dispose();
	            return sourceImage;

	        } catch (Exception ex) {
	            System.err.println(ex);
	            return null;
	        }
	    }
}


