package it.myportfolio.mapper;

import org.springframework.beans.BeanUtils;


import it.myportfolio.dto.WorkDTO;
import it.myportfolio.model.Work;

public class WorkMapper {
	
	 public static Work toWork(WorkDTO workDTO) {
	        Work work = new Work();
	        BeanUtils.copyProperties(workDTO, work);
	        return work;
	    }

	    public static WorkDTO toWorkDTO(Work work) {
	        WorkDTO workDTO = new WorkDTO();
	        BeanUtils.copyProperties(work, workDTO);
	        return workDTO;
	    }

}
