package it.myportfolio.dto;


public class SimpleImageDTO {

	private Long Id;
	private String label;
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


	public String getThumbnailURL() {
		return thumbnailURL;
	}

	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

//	public static SimpleImageDTO fromImage(ImageProject image) {
//		SimpleImageDTO dto = new SimpleImageDTO();
//		dto.setId(image.getId());
//		dto.setLabel(image.getLabel());
//		dto.setURL(image.getURL());
//		dto.setThumbnailURL(image.getThumbnailURL());
//		return dto;
//	}

}
