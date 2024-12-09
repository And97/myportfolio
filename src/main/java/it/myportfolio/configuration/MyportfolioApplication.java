package it.myportfolio.configuration;


import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import it.myportfolio.model.ERole;

import it.myportfolio.model.Role;
import it.myportfolio.model.User;
import it.myportfolio.model.Work;
import it.myportfolio.repository.RoleRepository;
import it.myportfolio.repository.UserRepository;
import it.myportfolio.repository.WorkRepository;


@SpringBootApplication
@ComponentScan(basePackages = {"it.myportfolio.*"})
@EnableJpaRepositories(basePackages  = "it.myportfolio.repository")
@EntityScan("it.myportfolio.model")

public class MyportfolioApplication {

	public static void main(String[] args) throws java.io.IOException {
		SpringApplication.run(MyportfolioApplication.class, args);

		
//		ImageProject a = new ImageProject();
//		a.setId(1L);
//		a.setLabel("test");
//		a.setURL("C:/Users/Andrea/Desktop/ta.jpeg");
//		a.setThumbnailURL("C:/Users/Andrea/Desktop/thumbnail/ta.jpeg");
//		
//		try {
//			ThumbnailGenerator.makeThumbnail(a);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	
	@Component
	public class CommandLineAppStartupRunner implements CommandLineRunner {
		
		@Autowired
	    RoleRepository roleRepository;
		
		@Autowired
	    UserRepository userRepository;
		
		
		@Autowired
	    WorkRepository workRepository;
		
		@Autowired
		PasswordEncoder encoder;
		
		
	    @Override
	    public void run(String...args) throws Exception {
//	        Role userRole = new Role(ERole.ROLE_USER);
//	    	Role adminRole = new Role(ERole.ROLE_ADMIN);
//	    	roleRepository.save(userRole);
//	    	roleRepository.save(adminRole);
//	    		
//	    	User admin = new User("admin", "admin@test.it", encoder.encode("admin"), "admin", "admin");
//	    	Set<Role> adminRoles = new HashSet<>();
//	    	adminRoles.add(adminRole);
//	    	adminRoles.add(userRole);
//	    	admin.setRoles(adminRoles);
//	    	admin.setEnable(true);
//			
//	    	userRepository.save(admin);
//	    		
//	    	User user = new User("user", "user@test.it", encoder.encode("user"), "user", "user");
//	    	Set<Role> userRoles = new HashSet<>();
//	    	userRoles.add(userRole);
//	    	user.setRoles(userRoles);
//	    	user.setEnable(true);
//	    	userRepository.save(user);
//	    	
//	    	Work work = new Work ();
//	    	work.setCompany("Shop");
//	    	work.setCompletionDate(new java.util.Date());
//	    	work.setTitle("Shop");
//	    	
//	    	workRepository.save(work);  	
	       
	    }
	}
	
	
}
