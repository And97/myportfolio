package it.myportfolio.dto;

import it.myportfolio.model.Image;

public class ImageDTO {

	private Long Id;
	private String label;
	private String URL;
	private String thumbnailURL;

	public Long getId() {
		return Id;
	}

	public void setId(Long iD) {
		Id = iD;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	public static ImageDTO fromImage(Image image) {
		ImageDTO dto = new ImageDTO();
		dto.setId(image.getId());
		dto.setLabel(image.getLabel());
		dto.setURL(image.getURL());
		dto.setThumbnailURL(image.getThumbnailURL());
		return dto;
	}

}
