package org.kickmyb.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

// TODO utiliser Spring security pour gérer les comptes
// TODO implémenter le paging pour une liste très longue ????
// TODO compléter détails d'une task
// TODO regarder déploiement sur AWS ou Heroku ou autre


@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class ServerApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException {
		SpringApplication.run(ServerApplication.class, args);
	}

}
