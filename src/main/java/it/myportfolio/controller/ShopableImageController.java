package it.myportfolio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@RequestMapping("/shopableimage")
@RestController
public class ShopableImageController {

	@Autowired
	ShopableImageService shopableImageService;

	@GetMapping
	public ResponseEntity<ShopableImageDTO> getShopableImageById(@RequestParam Long id) {
		ShopableImage shopableImage = shopableImageService.getShopableImageById(id);
		if (shopableImage == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(ShopableImageDTO.fromShopableImage(shopableImage));
	}

	@GetMapping("/all")
	public ResponseEntity<List<ShopableImageDTO>> getAllShopableImages() {
		Iterable<ShopableImage> shopableImages = shopableImageService.getAllShopableImages();
		List<ShopableImageDTO> shopableImageDTOs = Mapper.toDTOList(ShopableImageDTO.class,shopableImages);
		return ResponseEntity.ok(shopableImageDTOs);
	}

	@PostMapping("/add")
	public ResponseEntity<ShopableImageDTO> addShopableImage(@RequestBody ShopableImageDTO shopableImageDTO) {
		ShopableImage shopableImage = Mapper.toEntity(ShopableImage.class, shopableImageDTO);
		ShopableImage savedShopableImage = shopableImageService.addShopableImage(shopableImage);
		shopableImageDTO.setID(savedShopableImage.getID());
		return new ResponseEntity<>(shopableImageDTO, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteImage(@PathVariable Long id) {
		ShopableImage shopableImage = shopableImageService.getShopableImageById(id);
		if (shopableImage == null) {
			return ResponseEntity.notFound().build();
		}

		shopableImageService.deleteImageById(id);
		return ResponseEntity.status(HttpStatus.OK).body("ShopableImage " + id + " delete");
	}

	@PutMapping("/{id}")
	public ResponseEntity<ShopableImageDTO> updateShopableImage(@PathVariable Long id, @RequestBody ShopableImageDTO shopableImageDTO) {
		ShopableImage updatedShopableImage = Mapper.toEntity(ShopableImage.class,shopableImageDTO);
		ShopableImage savedShopableImage = shopableImageService.updateImage(id, updatedShopableImage);
		if (savedShopableImage == null) {
			return ResponseEntity.notFound().build(); // Image not found
		}
		return ResponseEntity.ok(Mapper.toDTO(ShopableImageDTO.class,savedShopableImage));
	}
}

