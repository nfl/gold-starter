package com.nfl.graphql;

import com.nfl.graphql.mediator.GraphQLMediator;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;

@Configuration
public class SillyConfig {

    @Bean
    public GraphQLMediator buildMediator() {
        return new GraphQLMediator(loadFromFile("schema_example.json"));
    }

    private String loadFromFile(String fileName) {
        ClassPathResource cpr = new ClassPathResource(fileName);
        try {
            return IOUtils.toString(cpr.getInputStream(), Charset.defaultCharset());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
