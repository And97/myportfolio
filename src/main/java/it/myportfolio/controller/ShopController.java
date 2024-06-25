package it.myportfolio.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.myportfolio.dto.DetailsSalesOrderDTO;
import it.myportfolio.dto.SalesOrderDTO;
import it.myportfolio.dto.ShopableImageDTO;
import it.myportfolio.mapper.Mapper;
import it.myportfolio.model.Cart;
import it.myportfolio.model.SalesOrder;
import it.myportfolio.model.ShopableImage;
import it.myportfolio.model.User;
import it.myportfolio.security.jwt.JwtUtils;
import it.myportfolio.service.CartService;
import it.myportfolio.service.SalesOrderService;
import it.myportfolio.service.UserService;
import it.myportfolio.utility.BlockchainTransactionService;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/shop")
public class ShopController {

	@Autowired
	private UserService userService;

	@Autowired
	private SalesOrderService salesOrderService;

	@Autowired
	private CartService cartService;

	@Autowired
	JwtUtils jwtUtils;

	// restituisce gli ordini di tutti gli utenti
	@GetMapping("/allorder")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<SalesOrderDTO>> getAllOrder(HttpServletRequest request) {

		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			List<SalesOrder> orders = salesOrderService.getAllOrders();
			List<SalesOrderDTO> salesOrderDTOs = new ArrayList<>();

			for (SalesOrder order : orders) {
				SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
				salesOrderDTO.setId(order.getId());
				salesOrderDTO.setTimestamp(order.getTimestamp());
				salesOrderDTO.setUsername(order.getUser().getUsername());
				salesOrderDTO.setUserID(order.getUser().getId());
				salesOrderDTO.setHash(order.getHash());

				List<ShopableImage> shopableImages = order.getPurchasedImage();
				float totalPrice = 0;
				for (ShopableImage shopableImage : shopableImages) {
					totalPrice = totalPrice + shopableImage.getPrice();
				}
				salesOrderDTO.setPrice(totalPrice);
				salesOrderDTOs.add(salesOrderDTO);
			}

			return ResponseEntity.ok(salesOrderDTOs);

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

	// restituisce gli ordini di un utente
	// modificare mapper
	@GetMapping("/myorder")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<SalesOrderDTO>> getMyOrder(HttpServletRequest request) {

		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			Long userId = (Long) jwtUtils.getUserIdFromJwtToken(token);
			Optional<User> user = userService.getUserById(userId);
			List<SalesOrder> orders = salesOrderService.getOrdersByUser(user.get());
			List<SalesOrderDTO> salesOrderDTOs = new ArrayList<>();

			for (SalesOrder order : orders) {
				SalesOrderDTO salesOrderDTO = new SalesOrderDTO();
				salesOrderDTO.setId(order.getId());
				salesOrderDTO.setTimestamp(order.getTimestamp());
				salesOrderDTO.setUsername(order.getUser().getUsername());
				salesOrderDTO.setUserID(order.getUser().getId());
				salesOrderDTO.setHash(order.getHash());
				List<ShopableImage> shopableImages = order.getPurchasedImage();
				float totalPrice = 0;
				for (ShopableImage shopableImage : shopableImages) {
					totalPrice = totalPrice + shopableImage.getPrice();
				}
				salesOrderDTO.setPrice(totalPrice);
				salesOrderDTOs.add(salesOrderDTO);
			}

			return ResponseEntity.ok(salesOrderDTOs);

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

	// OK
	// recupero cart dell'utente
	// per ogni immagine nel carrello controllo se non è venduta
	// registro l'acquisto
	// svuoto il carrello
	@PostMapping("")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> makeOrder(HttpServletRequest request) {
		String token = jwtUtils.getJwtFromCookies(request);
		Long userId = (Long) jwtUtils.getUserIdFromJwtToken(token);
		Optional<User> optionalUser = userService.getUserById(userId);
		User user = optionalUser.get();

		if (jwtUtils.validateJwtToken(token)) {
			Cart cart = cartService.getMyCart(user);
			if (cart == null || cart.getImages().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart is Empty");
			}

			List<ShopableImage> shopableImages = cart.getImages();
			float price = 0;
			int piece = 0;
			List<ShopableImageDTO> shopableImageDTOs = new ArrayList<>();
			for (ShopableImage shopableImage : shopableImages) {
				ShopableImageDTO shopableImageDTO = Mapper.toDTO(ShopableImageDTO.class, shopableImage);
				shopableImageDTO.setID(shopableImage.getId());
				shopableImageDTOs.add(shopableImageDTO);
				price = price + shopableImage.getPrice();
				piece++;
				shopableImage.setSold(true);
			}

			List<ShopableImage> images = new ArrayList<>();
			List<Long> IID = new ArrayList<>();
			for (ShopableImage shopableImage : shopableImages) {
				images.add(shopableImage);
				IID.add(shopableImage.getId());
			}

			cartService.emptyCart(user);

			SalesOrder order = new SalesOrder();
			order.setTimestamp(new Date());
			order.setUser(user);
			order.setPurchasedImage(images);

			salesOrderService.addSalesOrder(order);

			DetailsSalesOrderDTO detailsSalesOrderDTO = new DetailsSalesOrderDTO();
			detailsSalesOrderDTO.setId(order.getId());
			detailsSalesOrderDTO.setTimestamp(order.getTimestamp());
			detailsSalesOrderDTO.setTotalPrice(price);
			detailsSalesOrderDTO.setPurchaseImage(shopableImageDTOs);
			// orderDTO.setUserID(order.getUser().getId());
			detailsSalesOrderDTO.setUsername(order.getUser().getUsername());
			detailsSalesOrderDTO.setPiece(piece);

			String hash = BlockchainTransactionService.registry_transaction(userId, IID, new java.util.Date());

			int i = 0;
			while (hash == null) {
				if (i == 3) {
					break;
				}
				hash = BlockchainTransactionService.verify_hash(hash);
				i++;
			}

			detailsSalesOrderDTO.setHash(hash);
			return ResponseEntity.ok(detailsSalesOrderDTO);

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

	// OK
	// verifico che l'id utente nel salesOrder e quello dell'utente siano lo stesso
	@GetMapping("/orderdetail")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<DetailsSalesOrderDTO> getOrderById(HttpServletRequest request, @RequestParam Long id) {

		String token = jwtUtils.getJwtFromCookies(request);
		if (jwtUtils.validateJwtToken(token)) {
			Long userId = (Long) jwtUtils.getUserIdFromJwtToken(token);
			Optional<SalesOrder> opionalSalesOrder = salesOrderService.getOrderById(id);
			SalesOrder salesOrder = opionalSalesOrder.get();

			if (salesOrder.getUser().getId() == userId) {

				DetailsSalesOrderDTO detailsSalesOrderDTO = new DetailsSalesOrderDTO();
				detailsSalesOrderDTO.setId(id);
				detailsSalesOrderDTO.setTimestamp(salesOrder.getTimestamp());
				float totalPrice = 0;
				int piece = 0;
				List<ShopableImage> shopableImages = salesOrder.getPurchasedImage();
				List<ShopableImageDTO> shopableImageDTOs = new ArrayList<>();

				for (ShopableImage shopableImage : shopableImages) {
					totalPrice = totalPrice + shopableImage.getPrice();
					piece++;
					ShopableImageDTO shopableImageDTO = Mapper.toDTO(ShopableImageDTO.class, shopableImage);
					shopableImageDTO.setID(shopableImage.getId());
					shopableImageDTOs.add(shopableImageDTO);
				}

				detailsSalesOrderDTO.setUsername(salesOrder.getUser().getUsername());
				detailsSalesOrderDTO.setTotalPrice(totalPrice);
				detailsSalesOrderDTO.setPiece(piece);
				detailsSalesOrderDTO.setPurchaseImage(shopableImageDTOs);
				return ResponseEntity.ok(Mapper.toDTO(DetailsSalesOrderDTO.class, detailsSalesOrderDTO));
			} else {
				return ResponseEntity.notFound().build();
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

	@PostMapping("/verify_hash")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> verifyHash(@RequestParam String hash) {
		String verify = BlockchainTransactionService.verify_hash(hash);
		int i = 0;
		while (verify == null) {
			if (i == 5) {
				break;
			}
			verify = BlockchainTransactionService.verify_hash(hash);
			i++;
		}
		if (verify == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("HASH NOT TRUTHFUL or SERVICE DOWN");
		}

		return ResponseEntity.status(HttpStatus.OK).body(verify);
	}
}
