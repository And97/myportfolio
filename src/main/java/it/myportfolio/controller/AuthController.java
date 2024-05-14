package it.myportfolio.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.myportfolio.dto.UserDetailsImpl;
import it.myportfolio.model.ERole;
import it.myportfolio.model.Role;
import it.myportfolio.model.User;
import it.myportfolio.repository.RoleRepository;
import it.myportfolio.repository.UserRepository;
import it.myportfolio.security.jwt.JwtUtils;
import it.myportfolio.security.payload.LoginRequest;
import it.myportfolio.security.payload.MessageResponse;
import it.myportfolio.security.payload.SignupRequest;
import it.myportfolio.security.payload.UpdatePasswordRequest;
import it.myportfolio.security.payload.UserInfoResponse;
import it.myportfolio.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	private UserService userService;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(
				new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {

		// System.out.println(signUpRequest.toString());
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		Set<Role> roles = new HashSet<>();
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()), signUpRequest.getSurname(), signUpRequest.getName());
		Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);

		user.setRoles(roles);
		user.setEnable(true);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

	}

	@PostMapping("/signout")
	public ResponseEntity<?> logoutUser() {
		ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(new MessageResponse("You've been signed out!"));
	}

	@GetMapping("/checkmail")
	public ResponseEntity<?> checkMail(@RequestParam("email") String email) {
		if (userRepository.existsByEmail(email)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		return ResponseEntity.ok(new MessageResponse("OK"));
	}

	@GetMapping("/checkusername")
	public ResponseEntity<?> checkUsername(@RequestParam("username") String username) {
		if (userRepository.existsByUsername(username)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already in use!"));
		}
		return ResponseEntity.ok(new MessageResponse("OK"));
	}

	@PutMapping("/updatepwd")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> authenticateUser(HttpServletRequest request, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			Long id = (Long) jwtUtils.getUserIdFromJwtToken(token);
			String username = jwtUtils.getUserNameFromJwtToken(token);
			Optional<User> optionalUser = userService.getUserById(id);
			if (optionalUser.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
			} else {
				User user = optionalUser.get();
				if (updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getOldPassword())) {
					return ResponseEntity.badRequest()
							.body(new MessageResponse("Error: new password is equal to old password"));
				}

				if (!encoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: bad password");
				}

				jwtUtils.getCleanJwtCookie();

				user.setPassword(encoder.encode(updatePasswordRequest.getNewPassword()));
				userService.updateUser(id, user);

				Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(username, updatePasswordRequest.getNewPassword()));

				SecurityContextHolder.getContext().setAuthentication(authentication);

				UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

				ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

//				List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
//						.collect(Collectors.toList());

				return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
						.body("Password changed");
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		}

	}

}
