package com.example.Authormodule;

import com.example.Authormodule.dao.AuthorDao;
import com.example.Authormodule.entity.Author;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuthorModuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorModuleApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(AuthorDao authorDao) {
		return args -> {
			Author author = Author.builder()
					.name("ktos")
					.build();

			authorDao.insertAuthor(author);
			System.out.println(author.getId());
			System.out.println(authorDao.findAuthor(author.getId()));
		};
	}
}
