package com.example.Bookmodule.author.service.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansHolder {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
