package it.myportofolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.myportfolio.model.ShopableImage;

import it.myportfolio.repository.ShopableImageRepository;

@Service
public class ShopableImageService {

	@Autowired
	ShopableImageRepository shopableImageRepository;

	public ShopableImage getShopableImageById(Long id) {
		return shopableImageRepository.findById(id).orElse(null);// .orElseThrow(()-> new
																	// ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	public Iterable<ShopableImage> getAllShopableImages() {
		return shopableImageRepository.findAll();
		// ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	public ShopableImage addShopableImage(ShopableImage shopableImage) {
		return shopableImageRepository.save(shopableImage);
	}

	public void deleteImageById(Long id) {
		shopableImageRepository.deleteById(id);
	}

	public ShopableImage updateImage(Long id, ShopableImage updatedShopableImage) {
		ShopableImage existingShopableImage = shopableImageRepository.findById(id).orElse(null);
		if (existingShopableImage == null) {
			return null; // not found
		}

		// Update the existing user with the data from updatedUser

		existingShopableImage.setLabel(updatedShopableImage.getLabel());
		existingShopableImage.setURL(updatedShopableImage.getURL());
		existingShopableImage.setThumbnailURL(updatedShopableImage.getThumbnailURL());
		existingShopableImage.setPrice(updatedShopableImage.getPrice());
		return shopableImageRepository.save(existingShopableImage);
	}

}
