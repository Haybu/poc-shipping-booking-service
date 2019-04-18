package io.agilehandy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;

/**
 * @author Haytham Mohamed
 **/

@SpringBootApplication
@EnableSchemaRegistryClient
public class PocBookingServiceCmdApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocBookingServiceCmdApplication.class, args);
	}

}
