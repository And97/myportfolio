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
import it.myportfolio.model.User;
import it.myportfolio.model.Work;
import it.myportfolio.security.jwt.JwtUtils;
import it.myportfolio.service.ImageService;
import it.myportfolio.service.UserService;
import it.myportfolio.service.WorkService;
import it.myportfolio.utility.ThumbnailGenerator;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/image")
public class ImageController {

	@Autowired
	ImageService imageService;

	@Autowired
	WorkService workService;

	@Autowired
	private UserService userService;

	@Autowired
	JwtUtils jwtUtils;

//	@GetMapping()
//	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//	public ResponseEntity<byte[]> getImageById(@RequestParam Long id) throws IOException {
//
//		ImageProject image = imageService.getImageById(id);
//		if (image != null) {
//
//			BufferedImage sourceImage = ImageIO.read(new File(image.getURL()));
//
//			// Add the watermark using the addTextWatermark function
//			BufferedImage watermarkedImage = ThumbnailGenerator.addTextWatermark(sourceImage);
//
//			// Converti BufferedImage in array di byte
//			byte[] imageBytes = convertImageToBytes(watermarkedImage);
//
//			return ResponseEntity.status(HttpStatus.OK).header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION)
//					.contentType(MediaType.IMAGE_PNG).body(imageBytes);
//		}
//		return ResponseEntity.notFound().build();
//	}

	@GetMapping()
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<byte[]> getImageById(@RequestParam Long id, HttpServletRequest request) throws IOException {

		ImageProject image = imageService.getImageById(id);
		if (image != null) {
			String token = jwtUtils.getJwtFromCookies(request);
			if (jwtUtils.validateJwtToken(token)) {
				Long userId = (Long) jwtUtils.getUserIdFromJwtToken(token);
				Optional<User> user = userService.getUserById(userId);
				Work work = workService.getWorkByImageId(id);

				if (work.getUsers().contains(user.get())) {
					BufferedImage sourceImage = ImageIO.read(new File(image.getURL()));

					// Add the watermark using the addTextWatermark function
					BufferedImage watermarkedImage = ThumbnailGenerator.addTextWatermark(sourceImage);

					// Converti BufferedImage in array di byte
					byte[] imageBytes = convertImageToBytes(watermarkedImage);

					return ResponseEntity.status(HttpStatus.OK)
							.header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION)
							.contentType(MediaType.IMAGE_PNG).body(imageBytes);
				} else {
					return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
				}

			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		}
		return ResponseEntity.notFound().build();
	}

	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/thumb")
	public ResponseEntity<byte[]> getThumbById(@RequestParam Long id) throws IOException {

		ImageProject image = imageService.getImageById(id);
		if (image != null) {

			BufferedImage sourceImage = ImageIO.read(new File(image.getThumbnailURL()));

			// Converti BufferedImage in array di byte
			byte[] imageBytes = convertImageToBytes(sourceImage);

			return ResponseEntity.status(HttpStatus.OK).header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION)
					.contentType(MediaType.IMAGE_PNG).body(imageBytes);
		}
		return ResponseEntity.notFound().build();

	}

	// Metodo per convertire BufferedImage in byte[]
	private byte[] convertImageToBytes(BufferedImage image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", baos); // Cambia "png" a seconda del tipo di immagine
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
		// System.out.println(workId);
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

	// Elimina un' immagine da un work
	@DeleteMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteImage(@RequestParam Long id) {

		imageService.deleteImageById(id);

		return ResponseEntity.status(HttpStatus.OK).body("Images delete");
	}

	// data un' immagine permette la modifica dei parametri di un'immagine
	@PutMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateImage(@RequestBody ImageDTO imageDTO) {

		ImageProject updatedImage = Mapper.toEntity(ImageProject.class, imageDTO);
		imageService.updateImage(updatedImage.getId(), updatedImage);

		return ResponseEntity.status(HttpStatus.OK).body("Images update");
	}
	
	@GetMapping("/detail")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ImageDTO> detailImage(@RequestParam Long id){
		ImageProject image = imageService.getImageById(id);
		if(image != null) {
			return ResponseEntity.ok(ImageDTO.fromImage(image));
		}
		return ResponseEntity.notFound().build();
	}
	

}
