package it.myportfolio.mapper;
import org.springframework.beans.BeanUtils;

import it.myportfolio.dto.ImageDTO;
import it.myportfolio.model.Image;


public class ImageMapper {

	 public static Image toImage(ImageDTO imageDTO) {
	        Image image = new Image();
	        BeanUtils.copyProperties(imageDTO, image);
	        return image;
	    }

	    public static ImageDTO toImageDTO(Image image) {
	        ImageDTO imageDTO = new ImageDTO();
	        BeanUtils.copyProperties(image, imageDTO);
	        return imageDTO;
	    }
}

