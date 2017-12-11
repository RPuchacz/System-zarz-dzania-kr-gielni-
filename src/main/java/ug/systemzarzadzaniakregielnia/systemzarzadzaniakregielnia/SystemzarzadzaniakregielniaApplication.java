package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SystemzarzadzaniakregielniaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemzarzadzaniakregielniaApplication.class, args);
	}
}
