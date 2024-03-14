package it.myportofolio.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.myportfolio.model.User;
import it.myportfolio.model.Work;
import it.myportfolio.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);// .orElseThrow(()-> new
																// ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	public User addUser(User user) {
		return userRepository.save(user);
	}

	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}


	public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return null; // User not found
        }

        // Update the existing user with the data from updatedUser
        existingUser.setSurname(updatedUser.getSurname());
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        //existingUser.setPassword(updatedUser.getPassword());
        // Update other fields as needed

        return userRepository.save(existingUser);
    }
	
	public Set<Work> findVisibleWorksByUserId(Long id){
		return userRepository.findVisibleWorksByUserId(id);
	}
	
		
	
}