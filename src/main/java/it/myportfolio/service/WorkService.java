package it.myportfolio.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.myportfolio.model.Work;
import it.myportfolio.repository.WorkRepository;

@Service
public class WorkService {
	
	@Autowired
	WorkRepository workRepository;
	
	public Work getWorkById(Long id) {
		return workRepository.findById(id).orElse(null);
	}

	public Work addWork(Work work) {
		return workRepository.save(work);
	}

	public void deleteWorkById(Long id) {
		workRepository.deleteById(id);
		
	}

	public Work updateWork(Long id, Work updatedWork) {
		 	Work existingWork = workRepository.findById(id).orElse(null);
	        if (existingWork == null) {
	            return null;
	        }
	        
	        existingWork.setTitle(updatedWork.getTitle());
	        existingWork.setCompany(updatedWork.getCompany());
	        existingWork.setCompletionDate(updatedWork.getCompletionDate());
	        
	        return workRepository.save(existingWork);
	}
	

}
