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
	private Set<Long> imagesId;

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


	public Set<Long> getImages() {
		return imagesId;
	}

	public void setImages(Set<Long> imagesId) {
		this.imagesId = imagesId;
	}

	public static DetailsWorkDTO fromWork(Work work) {
		DetailsWorkDTO dto = new DetailsWorkDTO();
		dto.setID(work.getID());
		dto.setTitle(work.getTitle());
		dto.setCompany(work.getCompany());
		dto.setCompletionDate(work.getCompletionDate());
		Set<ImageProject> images = work.getImage();
		Set<Long> iDimagesToDTO =  new HashSet<Long>();
		for (ImageProject imageProject : images) {
			iDimagesToDTO.add(imageProject.getId());
		}
		return dto;
	}

}
