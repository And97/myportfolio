package it.myportfolio.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.myportfolio.dto.ShopableImageDTO;
import it.myportfolio.mapper.Mapper;
import it.myportfolio.model.ShopableImage;
import it.myportfolio.service.ShopableImageService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/shopableimage")
public class ShopableImageController {

	@Autowired
	private ShopableImageService shopableImageService;

	
	// restuitisce tutte le shoapableimage che possono essere acquistate
	//isSold=false
	@GetMapping()
	public ResponseEntity<List<ShopableImageDTO>> getAllShopableImages() {
		Iterable<ShopableImage> shopableImages = shopableImageService.getAllShopableImages();
		List<ShopableImageDTO> shopableImageDTOs = new ArrayList<>();
		for (ShopableImage shopableImage : shopableImages) {
			ShopableImageDTO temp = Mapper.toDTO(ShopableImageDTO.class, shopableImage);
			temp.setID(shopableImage.getId());
			shopableImageDTOs.add(temp);
		}
		return ResponseEntity.ok(shopableImageDTOs);	}

	
	// verificare se una certa shopableImage è acquistabile
	@GetMapping("/issold")
	public ResponseEntity<String> isSold(@RequestParam Long id) {
		ShopableImage shopableImage = shopableImageService.getShopableImageById(id);
		if (shopableImage.isSold()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This Image is sold");
		}
		else {
			return ResponseEntity.status(HttpStatus.OK).body("This Image is not sold");
		}
	}

	
	// caricare nuova shopableImage
	@PostMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ShopableImageDTO> addShopableImage(@RequestBody ShopableImageDTO shopableImageDTO) {
		ShopableImage shopableImage = Mapper.toEntity(ShopableImage.class, shopableImageDTO);
		shopableImage.setSold(false);
		ShopableImage savedShopableImage = shopableImageService.addShopableImage(shopableImage);
		shopableImageDTO.setID(savedShopableImage.getId());
		return new ResponseEntity<>(shopableImageDTO, HttpStatus.CREATED);
	}

	
	// delete shopableImage
	@DeleteMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteImage(@RequestParam Long id) {
		ShopableImage shopableImage = shopableImageService.getShopableImageById(id);
		if (shopableImage == null) {
			return ResponseEntity.notFound().build();
		}

		shopableImageService.deleteImageById(id);
		return ResponseEntity.status(HttpStatus.OK).body("ShopableImage " + id + " delete");
	}

	
	// update shopableImage
	@PutMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ShopableImageDTO> updateShopableImage(@RequestParam Long id,
			@RequestBody ShopableImageDTO shopableImageDTO) {
		ShopableImage updatedShopableImage = Mapper.toEntity(ShopableImage.class, shopableImageDTO);
		ShopableImage savedShopableImage = shopableImageService.updateImage(id, updatedShopableImage);
		if (savedShopableImage == null) {
			return ResponseEntity.notFound().build(); // Image not found
		}
		return ResponseEntity.ok(Mapper.toDTO(ShopableImageDTO.class, savedShopableImage));
	}
}
