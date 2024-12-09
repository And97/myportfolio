package it.myportfolio.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.myportfolio.dto.DetailsWorkDTO;
import it.myportfolio.dto.WorkDTO;
import it.myportfolio.mapper.Mapper;
import it.myportfolio.model.User;
import it.myportfolio.model.Work;
import it.myportfolio.security.jwt.JwtUtils;
import it.myportfolio.service.UserService;
import it.myportfolio.service.WorkService;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/work")
public class WorkController {

	@Autowired
	WorkService workService;

	@Autowired
	private UserService userService;

	@Autowired
	JwtUtils jwtUtils;

	// fare anche controller per admin che possa fare id utente e avere gli work a
	// qui ha accesso
	// restituisce tutti i work per cui un utente ha l'accesso in visualizzazione
	// senza riferimento alle Image
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@GetMapping("/mywork")
	public ResponseEntity<Set<WorkDTO>> getVisibleWorksByUserId(HttpServletRequest request) {
		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			Long userId = (Long) jwtUtils.getUserIdFromJwtToken(token);
			Set<Work> works = userService.findVisibleWorksByUserId(userId);
			Set<WorkDTO> workDtos = new HashSet<>();

			for (Work work : works) {
				workDtos.add(WorkDTO.fromWork(work));
			}

			if (workDtos.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			return ResponseEntity.ok(workDtos);

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/all")
	public ResponseEntity<Set<WorkDTO>> getAllWorks(HttpServletRequest request) {
		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {

			List<Work> works = workService.getAllWork();
			Set<WorkDTO> workDtos = new HashSet<>();

			for (Work work : works) {
				if (work.getID() != 1 && work.getCompany() != "Shop") {
					workDtos.add(WorkDTO.fromWork(work));
				}
					
			}

			return ResponseEntity.ok(workDtos);

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	// creazione di un nuovo work da parte di un admin
	@PostMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<WorkDTO> addWork(HttpServletRequest request, @RequestBody WorkDTO workDTO) {
		System.out.println("entrato nell'add");
		String token = jwtUtils.getJwtFromCookies(request);
		System.out.println("token");
		if (jwtUtils.validateJwtToken(token)) {

			Long id = (Long) jwtUtils.getUserIdFromJwtToken(token);
			Optional<User> optionalUser = userService.getUserById(id);
			User user = optionalUser.get();

			Work work = Mapper.toEntity(Work.class, workDTO);
			work.getUsers().add(user);
			Work savedWork = workService.addWork(work);
			workDTO.setID(savedWork.getID());

			Set<Work> images = user.getVisibleWorks();
			images.add(work);
			user.setVisibleWorks(images);
			userService.addUser(user);

			return new ResponseEntity<>(workDTO, HttpStatus.CREATED);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	// cancellazione Work da parte di un admin
	@DeleteMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteWork(HttpServletRequest request, @RequestParam Long id) {
		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			Optional<Work> optionalWork = workService.getWorkById(id);
			if (optionalWork.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			workService.deleteWorkById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Work " + id + " delete");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	// modifica Work da Parte di un Admin
	@PutMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<WorkDTO> updateWork(HttpServletRequest request, @RequestBody WorkDTO workDTO,
			@RequestParam Long id) {
		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			Work updatedWork = Mapper.toEntity(Work.class, workDTO);
			updatedWork.setID(id);
			Work savedWork = workService.updateWork(updatedWork.getID(), updatedWork);
			if (savedWork == null) {
				return ResponseEntity.notFound().build(); // Image not found
			}
			return ResponseEntity.ok(Mapper.toEntity(WorkDTO.class, savedWork));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	// restituisce gli id delle image contenute in un work passando l'id di un work
	// ( devo
	// verifica tramite la JOIN QUERY
	// che l'utente ne abbia realmente accesso)
	@GetMapping()
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<DetailsWorkDTO> getWorkByWorkId(HttpServletRequest request, @RequestParam Long id) {

		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			Long user_id = (Long) jwtUtils.getUserIdFromJwtToken(token);
			Work work = workService.getWorkDTOByIdAndUser(id, user_id);
			if (work == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(DetailsWorkDTO.fromWork(work));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

	// la relazione è N-N devo fare come sopra
	// lato view avrò una griglia con tutti i possibili lavori metterò e toglierò la
	// spunta
	@PatchMapping("/visible-works")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> updateVisibleWorks(@RequestParam Long userId, @RequestBody ArrayList<Long> workIds) {
		Optional<User> optionalUser = userService.getUserById(userId);
		if (optionalUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		User user = optionalUser.get();
		Set<Work> visibleWorks = new HashSet<Work>();

		for (Long id : workIds) {
			Optional<Work> optionalWork = workService.getWorkById(id);
			if (optionalWork.isPresent()) {
				Work work = optionalWork.get();
				work.getUsers().add(user);
				workService.addWork(work);
				visibleWorks.add(work);
			}
		}

		user.setVisibleWorks(visibleWorks);
		userService.addUser(user);

		return ResponseEntity.status(HttpStatus.OK).body("Visible works updated successfully");

	}
}
