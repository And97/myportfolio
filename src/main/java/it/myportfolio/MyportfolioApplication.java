package it.myportfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"it.myportfolio"})
@EnableJpaRepositories(basePackages  = "it.myportfolio.repository")
@EntityScan("it.myportfolio")
@ComponentScan("it.myportofolio.service")

public class MyportfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyportfolioApplication.class, args);
	}

}
