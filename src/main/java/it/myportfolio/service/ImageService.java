package it.myportfolio.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.myportfolio.model.ImageProject;
import it.myportfolio.repository.ImageRepository;

@Service
public class ImageService {
	
	@Autowired
	private ImageRepository imageRepository;
	
	public ImageProject getImageById(Long id) {
		return imageRepository.findById(id).orElse(null);// .orElseThrow(()-> new
																// ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
	public ImageProject addImage(ImageProject image) {
		return imageRepository.save(image);
	}
	
	public void deleteImageById(Long id) {
		imageRepository.deleteById(id);
	}

	public ImageProject updateImage(Long id, ImageProject updatedImage) {
        ImageProject existingImage = imageRepository.findById(id).orElse(null);
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
