package it.myportfolio.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
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

import it.myportfolio.dto.ImageDTO;
import it.myportfolio.mapper.Mapper;
import it.myportfolio.model.ImageProject;
import it.myportfolio.model.Work;
import it.myportfolio.service.ImageService;
import it.myportfolio.service.WorkService;
import it.myportfolio.utility.ThumbnailGenerator;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/image")
public class ImageController {

	@Autowired
	ImageService imageService;

	@Autowired
	WorkService workService;

//	// OK
//	@GetMapping
//	//@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//	public ResponseEntity<ImageDTO> getImageById(@RequestParam Long id) {
//		ImageProject image = imageService.getImageById(id);
//		if (image == null) {
//			return ResponseEntity.notFound().build();
//		}
//		return ResponseEntity.ok(ImageDTO.fromImage(image));
//	}
	//@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	
	@GetMapping()
	public ResponseEntity<byte[]> getImageById(@RequestParam Long id) throws IOException {

		ImageProject image = imageService.getImageById(id);
		if (image != null) {

			BufferedImage sourceImage = ImageIO.read(new File(image.getURL()));

			// Add the watermark using the addTextWatermark function
			BufferedImage watermarkedImage = ThumbnailGenerator.addTextWatermark(sourceImage);

			// Converti BufferedImage in array di byte
	        byte[] imageBytes = convertImageToBytes(watermarkedImage);

	        return ResponseEntity.status(HttpStatus.OK)
	                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION)
	                .contentType(MediaType.IMAGE_PNG)
	                .body(imageBytes);
		}
		return ResponseEntity.notFound().build();

	}
	// Metodo per convertire BufferedImage in byte[]
    private byte[] convertImageToBytes(BufferedImage image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpeg", baos);  // Cambia "png" a seconda del tipo di immagine
        } catch (IOException e) {
            // Gestione dell'errore se necessario
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

	

	// OK
	// permette l'aggiunta di una lista di Image, è necessario specificare su quale
	// Work fa aggiunta
	// controlla e nel caso genere le thumbnail
	@PostMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> addImagesToWork(@RequestBody List<ImageDTO> imagesDTO, @RequestParam Long workId)
			throws IOException {
		//System.out.println(workId);
		if (workId == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Work ID is empty");
		}
		Optional<Work> optionalWork = workService.getWorkById(workId);
		System.out.println(optionalWork.get());
		if (optionalWork.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Work savedWork = optionalWork.get();
		Set<ImageProject> images = savedWork.getImage();
		for (ImageDTO imageDTO : imagesDTO) {
			ImageProject image = Mapper.toEntity(ImageProject.class, imageDTO);

			// genero i thumb, i link thumbnail li genero lato client
			// ThumbnailGenerator.makeThumbnail(image.getURL(), image.getThumbnailURL());

			images.add(image);
			ImageProject savedImage = imageService.addImage(image);
			imageDTO.setId(savedImage.getId());
		}

		savedWork.setImage(images);
		workService.addWork(savedWork);

		return ResponseEntity.status(HttpStatus.OK).body("Images added");
	}

	// Elimina una lista di immagini da un work
	@DeleteMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteImage(@RequestBody List<Long> ImagesId) {
		for (Long id : ImagesId) {
			imageService.deleteImageById(id);
		}
		return ResponseEntity.status(HttpStatus.OK).body("Images delete");
	}

	// data una lista di immagini permette la modifica dei parametri di un'immagine
	@PutMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateImage(@RequestBody List<ImageDTO> imagesDTO) {
		for (ImageDTO imageDTO : imagesDTO) {
			System.out.println(imageDTO.getId());
			ImageProject updatedImage = Mapper.toEntity(ImageProject.class, imageDTO);
			imageService.updateImage(updatedImage.getId(), updatedImage);
		}
		return ResponseEntity.status(HttpStatus.OK).body("Images update");
	}


}
