package it.myportfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.myportfolio.dto.UserDetailsImpl;
import it.myportfolio.model.User;
import it.myportfolio.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	 
	  @Autowired
	  private UserRepository userRepository;

	  @Override
	  @Transactional
	  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    User user = userRepository.findByUsernameAndEnabled(username)
	        .orElseThrow(() -> new UsernameNotFoundException("User Not Found or disable with username: " + username));

	    return UserDetailsImpl.build(user);
	  }
}