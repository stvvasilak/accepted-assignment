package accepted.vasilakakis.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class LocalTimeConverter implements AttributeConverter<LocalTime, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public String convertToDatabaseColumn(LocalTime attribute) {
        return attribute != null ? attribute.format(FORMATTER) : null;
    }

    @Override
    public LocalTime convertToEntityAttribute(String dbData) {
        return dbData != null ? LocalTime.parse(dbData, FORMATTER) : null;
    }
}
