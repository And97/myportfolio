package it.myportofolio.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.myportfolio.model.Image;
import it.myportfolio.repository.ImageRepository;

@Service
public class ImageService {
	
	@Autowired
	ImageRepository imageRepository;
	
	public Image getImageById(Long id) {
		return imageRepository.findById(id).orElse(null);// .orElseThrow(()-> new
																// ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
	public Image addImage(Image image) {
		return imageRepository.save(image);
	}
	
	public void deleteImageById(Long id) {
		imageRepository.deleteById(id);
	}

	public Image updateImage(Long id, Image updatedImage) {
        Image existingImage = imageRepository.findById(id).orElse(null);
        if (existingImage == null) {
            return null; // User not found
        }

        // Update the existing user with the data from updatedUser
        
    
        existingImage.setLabel(updatedImage.getLabel());
        existingImage.setURL(updatedImage.getURL());
        existingImage.setThumbnailURL(updatedImage.getThumbnailURL());

        return imageRepository.save(existingImage);
    }
	


}
