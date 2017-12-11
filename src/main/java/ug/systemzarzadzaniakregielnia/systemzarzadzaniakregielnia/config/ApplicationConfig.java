package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Created by Lukasz on 2017-12-11.
 */
@Configuration
public class ApplicationConfig {
    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource rrms = new ReloadableResourceBundleMessageSource();
        rrms.setBasename("classpath:messages/messages");
        rrms.setDefaultEncoding("UTF-8");
        return rrms;
    }
}


