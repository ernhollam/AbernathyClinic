package com.abernathyclinic.patients.configuration;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.format.DateTimeFormatter;
@Configuration
public class MapperBuilderConfiguration {
    @Bean
    // Create bean with configured object mapper to deserialize and serialize implicitly and explicitly LocalDates
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        String            datePattern = "yyyy-MM-dd";
        DateTimeFormatter dateFormat  = DateTimeFormatter.ofPattern(datePattern);
        return new Jackson2ObjectMapperBuilder()
                .deserializers(new LocalDateDeserializer(dateFormat))
                .serializers(new LocalDateSerializer(dateFormat))
                .featuresToEnable(SerializationFeature.INDENT_OUTPUT)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
