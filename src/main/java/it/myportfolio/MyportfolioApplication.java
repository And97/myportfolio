package it.myportfolio;



import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import it.myportfolio.utility.ThumbnailGenerator;



@SpringBootApplication
@ComponentScan(basePackages = {"it.myportfolio"})
@EnableJpaRepositories(basePackages  = "it.myportfolio.repository")
@EntityScan("it.myportfolio.model")
@ComponentScan("it.myportofolio.service")

public class MyportfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyportfolioApplication.class, args);
		
		try {
			ThumbnailGenerator.makeThumbnail("C:\\Users\\Andrea\\Pictures\\Screenshots\\ta.jpeg", "C:\\Users\\Andrea\\Pictures\\Screenshots\\thumbnail\\ta.jpeg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
