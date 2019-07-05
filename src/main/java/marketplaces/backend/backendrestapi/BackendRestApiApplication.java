package marketplaces.backend.backendrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class BackendRestApiApplication {

    public static void main(String[] args) { SpringApplication.run( BackendRestApiApplication.class, args); }

}
