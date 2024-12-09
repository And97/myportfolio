package it.myportfolio.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.myportfolio.dto.CartDTO;
import it.myportfolio.dto.ShopableImageDTO;
import it.myportfolio.mapper.Mapper;
import it.myportfolio.model.Cart;
import it.myportfolio.model.ShopableImage;
import it.myportfolio.model.User;
import it.myportfolio.security.jwt.JwtUtils;
import it.myportfolio.service.CartService;
import it.myportfolio.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private UserService userService;


	@Autowired
	JwtUtils jwtUtils;

	//OK
	// aggiunta di una shopableImage al carrello dell'utente
	// controllare che l'immagine non sia già nel carrello
	@PostMapping("")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> addShopableImageToCart(HttpServletRequest request,
			@RequestParam Long shopableImageId) {

		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			Long id = (Long) jwtUtils.getUserIdFromJwtToken(token);
			Optional<User> optionalUser = userService.getUserById(id);
			User user = optionalUser.get();
            
			if (cartService.check(user, shopableImageId)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image cannot be add to cart");
			}
			
			Cart updatedCart = cartService.addToCart(user, shopableImageId);
            CartDTO cartDTO = new CartDTO();
			cartDTO.setId(updatedCart.getId());
			cartDTO.setUserId(updatedCart.getUser().getId());
			List<ShopableImage> shopableImages = updatedCart.getImages();
			List<ShopableImageDTO> shopableImagesDTOs = new ArrayList<>();
			for (ShopableImage shopableImage : shopableImages) {
				ShopableImageDTO shopableImageDTO = Mapper.toDTO(ShopableImageDTO.class, shopableImage);
				shopableImageDTO.setID(shopableImage.getId());
				shopableImagesDTOs.add(shopableImageDTO);
			}
			cartDTO.setImages(shopableImagesDTOs);
			return ResponseEntity.ok(cartDTO);

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

	// OK
	@GetMapping("")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<CartDTO> showMyCart(HttpServletRequest request) {

		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			Long id = (Long) jwtUtils.getUserIdFromJwtToken(token);
			User user = userService.getUserById(id).get();
			Cart cart = cartService.getMyCart(user);

			CartDTO cartDTO = new CartDTO();
			cartDTO.setId(cart.getId());
			cartDTO.setUserId(cart.getUser().getId());
			List<ShopableImage> shopableImages = cart.getImages();
			List<ShopableImageDTO> shopableImagesDTOs = new ArrayList<>();
			if (shopableImages != null) {
				for (ShopableImage shopableImage : shopableImages) {
					ShopableImageDTO shopableImageDTO = Mapper.toDTO(ShopableImageDTO.class, shopableImage);
					shopableImageDTO.setID(shopableImage.getId());
					shopableImagesDTOs.add(shopableImageDTO);
				}
			}
			cartDTO.setImages(shopableImagesDTOs);
			return ResponseEntity.ok(cartDTO);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

	// rimuovi una shopableImage al carrello dell'utente
	@DeleteMapping("")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<CartDTO> deleteShopableImageToCart(HttpServletRequest request,
			@RequestParam Long shopableImageId) {
		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			Long id = (Long) jwtUtils.getUserIdFromJwtToken(token);
			Optional<User> user = userService.getUserById(id);

			Cart updatedCart = cartService.removeToCart(user.get(), shopableImageId);
		
            CartDTO cartDTO = new CartDTO();
			cartDTO.setId(updatedCart.getId());
			cartDTO.setUserId(updatedCart.getUser().getId());
			List<ShopableImage> shopableImages = updatedCart.getImages();
			List<ShopableImageDTO> shopableImagesDTOs = new ArrayList<>();
			for (ShopableImage shopableImage : shopableImages) {
				ShopableImageDTO shopableImageDTO = Mapper.toDTO(ShopableImageDTO.class, shopableImage);
				shopableImageDTO.setID(shopableImage.getId());
				shopableImagesDTOs.add(shopableImageDTO);
			}
			cartDTO.setImages(shopableImagesDTOs);
			return ResponseEntity.ok(cartDTO);
			
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

	// svuota carrello
	@DeleteMapping("/empty")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> emptyCart(HttpServletRequest request) {
		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			Long id = (Long) jwtUtils.getUserIdFromJwtToken(token);
			Optional<User> user = userService.getUserById(id);
			cartService.emptyCart(user.get());
			return ResponseEntity.status(HttpStatus.OK).body("Cart empty");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
