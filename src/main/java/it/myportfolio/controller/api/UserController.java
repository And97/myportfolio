package it.myportfolio.controller.api;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.myportfolio.dto.UserPersonalDetailsDTO;
import it.myportfolio.dto.WorkDTO;
import it.myportfolio.mapper.Mapper;
import it.myportfolio.model.User;
import it.myportfolio.model.Work;
import it.myportfolio.service.UserService;

@RestController
@RequestMapping("api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//restiuisce anagrafica utente 
	@GetMapping("/")
	public ResponseEntity<UserPersonalDetailsDTO> getPersonalDetails(@RequestParam Long userId) {
		User user = userService.getUserById(userId);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(Mapper.toDTO(UserPersonalDetailsDTO.class, user));
	}
	

	//permette aggiornamento dei dati personali di un utente
	@PutMapping("/")
	public ResponseEntity<UserPersonalDetailsDTO> personalDetails(@RequestParam Long userId, @RequestBody UserPersonalDetailsDTO userDTO) {
		User updatedUser = Mapper.toEntity(User.class, userDTO);

		User savedUser = userService.updateUser(userId, updatedUser);
		if (savedUser == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(Mapper.toDTO(UserPersonalDetailsDTO.class, savedUser));
	}

	//restituisce i work per cui un utente ha l'accesso in visualizzazione
	@GetMapping("/work")
	public ResponseEntity<Set<WorkDTO>> getVisibleWorksByUserId(@RequestParam Long userId) {
		Set<Work> works = userService.findVisibleWorksByUserId(userId);
		Set<WorkDTO> workDtos = new HashSet<>();

		for (Work work : works) {
			workDtos.add(WorkDTO.fromWork(work));
		}

		if (workDtos.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(workDtos);
	}

}
