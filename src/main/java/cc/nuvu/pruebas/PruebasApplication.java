package cc.nuvu.pruebas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@EnableConfigurationProperties
@SpringBootApplication
public class PruebasApplication {

    public static void main(String[] args) {
        SpringApplication.run(PruebasApplication.class, args);
    }

}
