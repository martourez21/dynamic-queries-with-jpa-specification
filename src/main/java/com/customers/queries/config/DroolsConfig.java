package com.customers.queries.config;

import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolsConfig {

    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem fileSystem = kieServices.newKieFileSystem();
        fileSystem.write(ResourceFactory.newClassPathResource("rules/withdraw-rules.drl"));
        kieServices.newKieBuilder(fileSystem).buildAll();
        KieRepository repository = kieServices.getRepository();
        KieContainer container = kieServices.newKieContainer(repository.getDefaultReleaseId());
        return container;
    }
}

