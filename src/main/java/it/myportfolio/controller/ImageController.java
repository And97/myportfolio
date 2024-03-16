package it.myportfolio.controller;

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

import it.myportfolio.dto.ImageDTO;
import it.myportfolio.mapper.Mapper;
import it.myportfolio.model.Image;
import it.myportfolio.service.ImageService;

@RestController
@RequestMapping("/image")
public class ImageController {
	
	@Autowired
	ImageService imageService;
	
	@GetMapping
    public ResponseEntity<ImageDTO> getImageById(@RequestParam Long id) {
        Image image = imageService.getImageById(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ImageDTO.fromImage(image));
    }

	  @PostMapping("/add")
	  public ResponseEntity<ImageDTO> addImage(@RequestBody ImageDTO imageDTO) {
	  	  Image image = Mapper.toEntity(Image.class, imageDTO);
	      Image savedImage = imageService.addImage(image);
	      imageDTO.setId(savedImage.getId());
	      return new ResponseEntity<>(imageDTO, HttpStatus.CREATED);
	  }
   
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
	 Image image = imageService.getImageById(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        imageService.deleteImageById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Image " + id + " delete");
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ImageDTO> updateImage(@PathVariable Long id, @RequestBody ImageDTO imageDTO) {
        Image updatedImage = Mapper.toEntity(Image.class, imageDTO);
        Image savedImage = imageService.updateImage(id, updatedImage);
        if (savedImage == null) {
            return ResponseEntity.notFound().build(); // Image not found
        }
        return ResponseEntity.ok(Mapper.toDTO(ImageDTO.class, savedImage));
    }

}


