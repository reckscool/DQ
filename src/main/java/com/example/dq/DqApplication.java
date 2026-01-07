
package com.example.dq;

import com.example.dq.config.DqConfigSchemaValidator;
import com.example.dq.engine.RuleEngine;
import com.example.dq.model.RuleSet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.InputStream;

@SpringBootApplication
public class DqApplication {

    public static void main(String[] args) {
        SpringApplication.run(DqApplication.class, args);
    }

    @Bean
    CommandLineRunner run(RuleEngine engine,
                          DqConfigSchemaValidator validator) {
        return args -> {
            InputStream config =
                getClass().getResourceAsStream("/dq-config.json");

            validator.validate(config);

            ObjectMapper mapper = new ObjectMapper();
            RuleSet[] rules = mapper.readValue(
                getClass().getResourceAsStream("/dq-config.json"),
                RuleSet[].class
            );

            for (RuleSet rs : rules) {
                engine.execute(rs);
            }
        };
    }
}
