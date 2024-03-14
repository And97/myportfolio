package it.myportfolio.controller;

import java.util.HashSet;

import java.util.Set;

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
import org.springframework.web.bind.annotation.RestController;

import it.myportfolio.dto.UserDTO;
import it.myportfolio.dto.UserPersonalDetailsDTO;
import it.myportfolio.dto.WorkDTO;
import it.myportfolio.mapper.Mapper;
import it.myportfolio.mapper.UserMapper;
import it.myportfolio.model.User;
import it.myportfolio.model.Work;
import it.myportofolio.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
		User user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(UserDTO.fromUser(user));
	}

	@PostMapping("/add")
	public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) {
		User user = UserMapper.toUser(userDTO);
		User savedUser = userService.addUser(user);
		userDTO.setId(savedUser.getID());
		return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		User user = userService.getUserById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		userService.deleteUserById(id);
		return ResponseEntity.status(HttpStatus.OK).body("User " + id + " delete");
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserDTO> personalDetails(@PathVariable Long id, @RequestBody UserPersonalDetailsDTO userDTO) {
		User updatedUser = Mapper.toEntity(User.class, userDTO);

		User savedUser = userService.updateUser(id, updatedUser);
		if (savedUser == null) {
			return ResponseEntity.notFound().build(); // User not found
		}
		return ResponseEntity.ok(UserMapper.toUserDTO(savedUser));
	}

	@GetMapping("/{userId}/works")
	public ResponseEntity<Set<WorkDTO>> getVisibleWorksByUserId(@PathVariable Long userId) {
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