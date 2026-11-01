package br.com.edilson.horacerta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HoraCertaApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoraCertaApplication.class, args);
    }
}