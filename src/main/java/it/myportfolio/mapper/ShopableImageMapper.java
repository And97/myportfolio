package it.myportfolio.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;


import it.myportfolio.dto.ShopableImageDTO;
import it.myportfolio.model.ShopableImage;

public class ShopableImageMapper {
	

	public static ShopableImage toShopableImage(ShopableImageDTO shopableImageDTO) {
        ShopableImage shopableImage = new ShopableImage();
        BeanUtils.copyProperties(shopableImageDTO, shopableImage);
        return shopableImage;
    }

    public static ShopableImageDTO toShopableImageDTO(ShopableImage shopableImage) {
        ShopableImageDTO shopableImageDTO = new ShopableImageDTO();
        BeanUtils.copyProperties(shopableImage, shopableImageDTO);
        return shopableImageDTO;
    }

    public static List<ShopableImageDTO> toDTOList(Iterable<ShopableImage> shopableImages) {
        List<ShopableImageDTO> shopableImageDTOs = new ArrayList<>();
        if (shopableImages != null) {
            for (ShopableImage shopableImage : shopableImages) {
                ShopableImageDTO shopableImageDTO = ShopableImageDTO.fromShopableImage(shopableImage);
                if (shopableImageDTO != null) {
                    shopableImageDTOs.add(shopableImageDTO);
                }
            }
        }
        return shopableImageDTOs;
 
    }
}
