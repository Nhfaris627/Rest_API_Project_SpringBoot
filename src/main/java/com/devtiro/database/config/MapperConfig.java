package com.devtiro.database.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        // Configure the mapping between BookEntity and BookDto
        mapper.typeMap(com.devtiro.database.domain.entities.BookEntity.class,
                        com.devtiro.database.domain.dto.BookDto.class)
                .addMapping(src -> src.getAuthorEntity(),
                        com.devtiro.database.domain.dto.BookDto::setAuthor);

        mapper.typeMap(com.devtiro.database.domain.dto.BookDto.class,
                        com.devtiro.database.domain.entities.BookEntity.class)
                .addMapping(src -> src.getAuthor(),
                        com.devtiro.database.domain.entities.BookEntity::setAuthorEntity);

        return mapper;
    }
}
