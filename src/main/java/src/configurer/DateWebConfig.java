package src.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Configuration
public class DateWebConfig implements WebMvcConfigurer {

    private static final DateTimeFormatter LOCAL_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private static final ZoneId ZONE = ZoneId.of("Europe/Warsaw");

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(String.class, Instant.class, text -> {
            LocalDateTime ldt = LocalDateTime.parse(text, LOCAL_FORMATTER);
            return ldt.atZone(ZONE).toInstant();
        });

        registry.addConverter(Instant.class, String.class, instant -> {
            LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZONE);
            return LOCAL_FORMATTER.format(ldt);
        });
    }
}