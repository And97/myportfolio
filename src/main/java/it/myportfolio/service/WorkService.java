package it.myportfolio.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.myportfolio.model.Work;
import it.myportfolio.repository.WorkRepository;

@Service
public class WorkService {
	
	@Autowired
	private WorkRepository workRepository;
	
	public Optional<Work> getWorkById(Long id) {
		return workRepository.findById(id);//.orElse(null);
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
	
	public Work getWorkDTOByIdAndUser(Long workId, Long userId) {
        Optional<Work> optionalWork = workRepository.findWorkByIdAndUserId(workId, userId);

        if (optionalWork.isPresent()) {
            return optionalWork.get();
        } else {
            return null; 
        }
    }
	
	public  List<Work> getAllWork() {;
        return  workRepository.findAll();
    }

}
