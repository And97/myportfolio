package it.myportfolio.controller;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.myportfolio.model.ImageProject;
import it.myportfolio.model.Work;
import it.myportfolio.service.WorkService;
import it.myportfolio.utility.ThumbnailGenerator;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/thumbnail")
public class ThumbnailController {

	@Autowired
	WorkService workService;

	@PostMapping("")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> makeThumbnail(@RequestParam Long id) {

		Optional<Work> optionalWork = workService.getWorkById(id);

		if (optionalWork.isPresent()) {
			Work work = optionalWork.get();
			Set<ImageProject> images = work.getImage();
			for (ImageProject image : images) {
				try {
					ThumbnailGenerator.makeThumbnail(image);
				} catch (IOException e) {
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
				}
			}
			return ResponseEntity.status(HttpStatus.OK).body("Thumbnail for images in work: " + id + " created");

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Work not found");
		}
	}
}