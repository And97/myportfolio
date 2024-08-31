package it.myportfolio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.myportfolio.model.Cart;
import it.myportfolio.model.ShopableImage;
import it.myportfolio.model.User;
import it.myportfolio.repository.CartRepository;
import it.myportfolio.repository.ShopableImageRepository;


@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ShopableImageRepository shopableImageRepository;
	
	public Boolean check (User user, Long shopableImageId) {
		boolean result = false;
		Optional <Cart> optionalCart = cartRepository.getCartByUser(user);
		if (optionalCart.isPresent()) {
			List<ShopableImage> images = optionalCart.get().getImages();
			for (ShopableImage shopableImage : images) {
				if(shopableImage.getId() == shopableImageId) {
					result = true;
				}
			}
		}
		return result;
	}
	
	public Cart getMyCart(User user) {
		
		Optional<Cart> optionalCart = cartRepository.getCartByUser(user);
		if (optionalCart.isEmpty()) {
			Cart cartToSave = new Cart();
			cartToSave.setUser(user);
			cartRepository.save(cartToSave);
		}
		return cartRepository.getCartByUser(user).get();
	}

	public Cart addToCart(User user, Long shopableImageId) {

		Optional <Cart> optionalCart = cartRepository.getCartByUser(user);
	
		
		if (optionalCart.isEmpty()) {
			Cart cartToSave = new Cart();
			cartToSave.setUser(user);
			cartRepository.save(cartToSave);
		}
		
		ShopableImage shopableImage =  shopableImageRepository.findById(shopableImageId).get();
	
		//aggiorno lato CART
		Cart DBcart = cartRepository.getCartByUser(user).get();
		List<ShopableImage> images = DBcart.getImages();
		if (images == null) {
			List<ShopableImage> image = new ArrayList<>();
			image.add(shopableImage);
			DBcart.setImages(image);
		}
		else {
			images.add(shopableImage);
			DBcart.setImages(images);
		}
		
		
		//aggiorno lato shopableImage
		List<Cart> SHCarts =  shopableImage.getCart();
		if (SHCarts == null) {
			List<Cart> SHCart = new ArrayList<>();
			SHCart.add(DBcart);
			shopableImage.setCart(SHCart);
		}
		else {
			SHCarts.add(DBcart);
			shopableImage.setCart(SHCarts);
		}
		
		
		shopableImageRepository.save(shopableImage);
		
		return cartRepository.save(DBcart);

	}

	
	public Cart removeToCart(User user, Long shopableImageId) {
		
		Optional<Cart> optionalCart = cartRepository.getCartByUser(user);
		ShopableImage shopableImage = shopableImageRepository.findById(shopableImageId).get();
		Cart cart = optionalCart.get();
		cart.getImages().remove(shopableImage);
	
		shopableImage.getCart().remove(cart);
		shopableImageRepository.save(shopableImage);
		
		
		return cartRepository.save(cart);
	}
	
	public void emptyCart(User user) {
      
		Cart cart = cartRepository.getCartByUser(user).get();
        
		List<ShopableImage> shopableImages = cart.getImages();
		for (ShopableImage shopableImage : shopableImages) {
			shopableImage.getCart().clear();
			shopableImageRepository.save(shopableImage);
		}
		
		cart.getImages().clear();
        cartRepository.save(cart);
       
    }
	
	public void deleteCartByUserId(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
}
