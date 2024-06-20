package it.myportfolio.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import it.myportfolio.model.ImageProject;
import it.myportfolio.model.Work;

public class DetailsWorkDTO {

	private Long ID;
	private String title;
	private String company;
	private Date completionDate;
	private Set<SimpleImageDTO> images;

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}


	public Set<SimpleImageDTO> getImages() {
		return images;
	}

	public void setImages(Set<SimpleImageDTO> images) {
		this.images = images;
	}

	public static DetailsWorkDTO fromWork(Work work) {
		DetailsWorkDTO dto = new DetailsWorkDTO();
		dto.setID(work.getID());
		dto.setTitle(work.getTitle());
		dto.setCompany(work.getCompany());
		dto.setCompletionDate(work.getCompletionDate());
		Set<ImageProject> images = work.getImage();
		Set<SimpleImageDTO> imagesToDTO =  new HashSet<SimpleImageDTO>();
		for (ImageProject imageProject : images) {
			SimpleImageDTO simpleImageDTO = new SimpleImageDTO();
			simpleImageDTO.setId(imageProject.getId());
			simpleImageDTO.setLabel(imageProject.getLabel());
			simpleImageDTO.setThumbnailURL(imageProject.getThumbnailURL());
			imagesToDTO.add(simpleImageDTO);
		}
		dto.setImages(imagesToDTO);
		return dto;
	}

}
