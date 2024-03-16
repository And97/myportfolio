package it.myportfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.myportfolio.dto.WorkDTO;
import it.myportfolio.mapper.Mapper;
import it.myportfolio.model.Work;
import it.myportofolio.service.WorkService;

@RestController
@RequestMapping("/work")
public class WorkController {

	@Autowired
	WorkService workService;

	@GetMapping()
	public ResponseEntity<WorkDTO> getImageById(@RequestParam Long id) {
		Work work = workService.getWorkById(id);
		if (work == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(WorkDTO.fromWork(work));
	}

	@PostMapping("/add")
	public ResponseEntity<WorkDTO> addImage(@RequestBody WorkDTO workDTO) {
		Work work = Mapper.toEntity(Work.class, workDTO);
		Work savedWork = workService.addWork(work);
		workDTO.setID(savedWork.getID());
		return new ResponseEntity<>(workDTO, HttpStatus.CREATED);
	}

	@DeleteMapping()
	public ResponseEntity<String> deleteImage(@RequestParam Long id) {
		Work work = workService.getWorkById(id);
		if (work == null) {
			return ResponseEntity.notFound().build();
		}

		workService.deleteWorkById(id);
		return ResponseEntity.status(HttpStatus.OK).body("Work " + id + " delete");
	}

	@PutMapping()
	public ResponseEntity<WorkDTO> updateWork(@RequestParam Long id, @RequestBody WorkDTO workDTO) {
		Work updatedWork = Mapper.toEntity(Work.class, workDTO);
		Work savedWork = workService.updateWork(id, updatedWork);
		if (savedWork == null) {
			return ResponseEntity.notFound().build(); // Image not found
		}
		return ResponseEntity.ok(Mapper.toEntity(WorkDTO.class, savedWork));
	}

}
