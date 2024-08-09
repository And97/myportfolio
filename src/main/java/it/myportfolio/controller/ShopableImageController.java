package it.myportfolio.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import it.myportfolio.dto.SimpleShopableImageDTO;
import it.myportfolio.mapper.Mapper;
import it.myportfolio.model.ImageProject;
import it.myportfolio.model.ShopableImage;
import it.myportfolio.model.Work;
import it.myportfolio.service.ShopableImageService;
import it.myportfolio.service.WorkService;
import it.myportfolio.utility.ThumbnailGenerator;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/shopableimage")
public class ShopableImageController {

	@Autowired
	private ShopableImageService shopableImageService;

	@Autowired
	private WorkService workService;

	// restuitisce tutte le shoapableimage che possono essere acquistate
	// isSold=false controllo fatto su query repo
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<List<SimpleShopableImageDTO>> getAllShopableImages() {
		Iterable<ShopableImage> shopableImages = shopableImageService.getAllShopableImages();
		List<SimpleShopableImageDTO> shopableImageDTOs = new ArrayList<>();
		for (ShopableImage shopableImage : shopableImages) {
			SimpleShopableImageDTO temp = Mapper.toDTO(SimpleShopableImageDTO.class, shopableImage);
			temp.setID(shopableImage.getId());
			shopableImageDTOs.add(temp);
		}
		return ResponseEntity.ok(shopableImageDTOs);
	}
	
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/thumb")
	public ResponseEntity<byte[]> getThumbShopableImageById(@RequestParam Long id) throws IOException {

		ShopableImage shopableImage = shopableImageService.getShopableImageById(id);
		if (shopableImage != null) {

			BufferedImage sourceImage = ImageIO.read(new File(shopableImage.getThumbnailURL()));

			
			// Converti BufferedImage in array di byte
			byte[] imageBytes = convertImageToBytes(sourceImage);

			return ResponseEntity.status(HttpStatus.OK).header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION)
					.contentType(MediaType.IMAGE_PNG).body(imageBytes);
		}
		return ResponseEntity.notFound().build();

	}

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping()
	public ResponseEntity<byte[]> getShopableImageById(@RequestParam Long id) throws IOException {

		ShopableImage shopableImage = shopableImageService.getShopableImageById(id);
		if (shopableImage != null) {

			BufferedImage sourceImage = ImageIO.read(new File(shopableImage.getURL()));

			// Add the watermark using the addTextWatermark function
			BufferedImage watermarkedImage = ThumbnailGenerator.addTextWatermark(sourceImage);

			// Converti BufferedImage in array di byte
			byte[] imageBytes = convertImageToBytes(watermarkedImage);

			return ResponseEntity.status(HttpStatus.OK).header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION)
					.contentType(MediaType.IMAGE_PNG).body(imageBytes);
		}
		return ResponseEntity.notFound().build();

	}

	// Metodo per convertire BufferedImage in byte[]
	private byte[] convertImageToBytes(BufferedImage image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpeg", baos); // Cambia "png" a seconda del tipo di immagine
		} catch (IOException e) {
			// Gestione dell'errore se necessario
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	// verificare se una certa shopableImage è acquistabile
	@GetMapping("/issold")
	public ResponseEntity<String> isSold(@RequestParam Long id) {
		ShopableImage shopableImage = shopableImageService.getShopableImageById(id);
		if (shopableImage == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shopable image " + id + " not found");
		}
		if (shopableImage.isSold()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("This Image is sold");
		} else {
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

		Work work = workService.getWorkById(1L).get();
		Set<ImageProject> images = work.getImage();
		images.add(shopableImage);
		work.setImage(images);
		workService.addWork(work);

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
