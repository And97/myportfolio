package it.myportfolio.dto;

import java.util.Date;
import java.util.Set;

import it.myportfolio.model.Image;
import it.myportfolio.model.Work;

public class WorkDTO {
	
	private Long ID;
	private String title;
	private String company;
	private Date completionDate;
	private Set<Image> image;
		

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
	public Set<Image> getImage() {
		return image;
	}
	public void setImage(Set<Image> image) {
		this.image = image;
	}
	
	public static WorkDTO fromWork(Work work) {
        WorkDTO dto = new WorkDTO();
        dto.setID(work.getID());
        dto.setTitle(work.getTitle());
        dto.setCompany(work.getCompany());
        dto.setCompletionDate(work.getCompletionDate());
        dto.setImage(work.getImage());
        return dto;
    }

}
