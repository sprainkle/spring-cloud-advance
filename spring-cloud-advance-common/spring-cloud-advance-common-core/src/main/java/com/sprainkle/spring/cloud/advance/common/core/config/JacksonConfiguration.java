package com.sprainkle.spring.cloud.advance.common.core.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprainkle.spring.cloud.advance.common.core.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 *
 * </p>
 *
 * @author sprainkle
 * @date 2019-09-10
 */
@Slf4j
@Configuration
public class JacksonConfiguration {
    /**
     * 处理序列化后的1.8的日期时间格式
     *
     * @return
     */
    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer());
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime value,
                              JsonGenerator jsonGenerator,
                              SerializerProvider provider)
                throws IOException {
            if (value == null) {
                jsonGenerator.writeNull();
            } else {
                jsonGenerator.writeNumber(TimeUtil.toMilli(value));
            }
        }
    }

    public class LocalDateSerializer extends JsonSerializer<LocalDate> {

        @Override
        public void serialize(LocalDate value,
                              JsonGenerator jsonGenerator,
                              SerializerProvider provider)
                throws IOException {
            if (value == null) {
                jsonGenerator.writeNull();
            } else {
                jsonGenerator.writeNumber(TimeUtil.toMilli(value));
            }
        }
    }

    public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

        @Override
        public void serialize(LocalTime value,
                              JsonGenerator jsonGenerator,
                              SerializerProvider provider)
                throws IOException {
            if (value == null) {
                jsonGenerator.writeNull();
            } else {
                jsonGenerator.writeNumber(TimeUtil.toMilli(value));
            }
        }
    }

    public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {


        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {
            if (parser.hasTokenId(JsonTokenId.ID_NUMBER_INT)) {
                String string = parser.getText().trim();
                if (string.length() == 0) {
                    return null;
                }

                long timestamp = Long.parseLong(string);
                return TimeUtil.toLocalDateTime(timestamp);
            }
            return null;
        }
    }

    public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser parser, DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {
            if (parser.hasTokenId(JsonTokenId.ID_NUMBER_INT)) {
                String string = parser.getText().trim();
                if (string.length() == 0) {
                    return null;
                }

                long timestamp = Long.parseLong(string);
                return TimeUtil.toLocalDate(timestamp);
            }
            return null;
        }
    }

    public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
        @Override
        public LocalTime deserialize(JsonParser parser, DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {
            if (parser.hasTokenId(JsonTokenId.ID_NUMBER_INT)) {
                String string = parser.getText().trim();
                if (string.length() == 0) {
                    return null;
                }

                long timestamp = Long.parseLong(string);
                return TimeUtil.toLocalTime(timestamp);
            }
            return null;
        }
    }

}
