package lu.oop.server;

import lu.oop.server.services.InitTagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication  implements CommandLineRunner {

	private InitTagsService initTagsService;

	@Autowired
	ServerApplication(InitTagsService initTagsService) {
		this.initTagsService = initTagsService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		initTagsService.initTags();
	}
}
