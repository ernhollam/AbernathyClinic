package com.abernathyclinic.history.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {
	@Bean
	public OpenAPI MediscreenPatientOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Mediscreen ─ Note API")
						.description(
								"""
										Mediscreen note/recommandation management APIs.
										Source code is available on [GitHub](https://github.com/ernhollam/Mediscreen)
										Mediscreen is a web application which allows doctors and practicians from your healthcare facility to manage patients database and their history with all the recommandations or notes.
										This information is then to be used to generate a report on the risk of a patient to develop diabetes.""")
						.license(new License().name("Creative Commons")
								.url("http://creativecommons.org/licenses/by/4.0/"))
						.contact(
								new Contact()
										.name("ernhollam")
										.url("https://github.com/ernhollam")
						)
				);
	}
}
