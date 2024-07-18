package it.myportfolio.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.myportfolio.dto.UserDTO;
import it.myportfolio.dto.UserPersonalDetailsDTO;
import it.myportfolio.mapper.Mapper;
import it.myportfolio.model.User;
import it.myportfolio.model.Work;
import it.myportfolio.security.jwt.JwtUtils;
import it.myportfolio.service.CartService;
import it.myportfolio.service.UserService;
import it.myportfolio.service.WorkService;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private CartService cartService;

	@Autowired
	WorkService workService;

	@Autowired
	JwtUtils jwtUtils;

	// restiuisce anagrafica utente
	@GetMapping()
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserPersonalDetailsDTO> getPersonalDetails(HttpServletRequest request) {
		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			Long id = (Long) jwtUtils.getUserIdFromJwtToken(token);
			Optional<User> user = userService.getUserById(id);

			return ResponseEntity.ok(Mapper.toDTO(UserPersonalDetailsDTO.class, user.get()));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	// restiuisce anagrafica utente
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserDTO>> getAllUser(HttpServletRequest request) {
		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			List <User> users= userService.getAllUser();
			List <UserDTO> usersDTO = new ArrayList<>();
			for (User user : users) {
				UserDTO userDTO =	Mapper.toDTO(UserDTO.class, user);
				userDTO.setShopableImage(user.getVisibleWorks());
				usersDTO.add(userDTO);
			}
			return ResponseEntity.ok(usersDTO);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

//	// restiuisce anagrafica utente
//	@GetMapping("/getuser")
//	@PreAuthorize("hasRole('ADMIN')")
//	public ResponseEntity<UserDTO> getPersonalDetailsById(HttpServletRequest request, @RequestParam Long id) {
//		String token = jwtUtils.getJwtFromCookies(request);
//		if (jwtUtils.validateJwtToken(token)) {
//			Optional<User> user = userService.getUserById(id);
//			return ResponseEntity.ok(Mapper.toDTO(UserDTO.class, user.get()));
//		} else {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//		}
//	}

	// permette aggiornamento dei dati personali di un utente
	@PutMapping()
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<UserPersonalDetailsDTO> updatePersonalDetails(HttpServletRequest request,
			@RequestBody UserPersonalDetailsDTO userDTO) {
		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			Long id = (Long) jwtUtils.getUserIdFromJwtToken(token);

			User updatedUser = Mapper.toEntity(User.class, userDTO);
			User savedUser = userService.updateUser(id, updatedUser);

			return ResponseEntity.ok(Mapper.toDTO(UserPersonalDetailsDTO.class, savedUser));
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	// permette la cancellazione di un utente dato un username (disabilita l'utenza
	// per non perdere i suoi dati)
	@DeleteMapping()
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteUserByUsername(HttpServletRequest request, @RequestParam String username) {
		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {

			User user = userService.getUserByUsername(username);
			if (user == null) {
				return ResponseEntity.notFound().build();
			}

			user.setRoles(null);
			user.setVisibleWorks(null);
			user.setEnable(false);
			userService.addUser(user);

			Set<Work> works = userService.findVisibleWorksByUserId(user.getId());

			// rimuovo la visualizzazione dei lavori ad un user e aggiorno il db
			for (Work work : works) {
				work.getUsers().remove(user);
				workService.addWork(work);
			}

			// elimino il carrello
			cartService.deleteCartByUserId(user.getId());

			return ResponseEntity.status(HttpStatus.OK).body("Username " + username + " disable");

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

}
