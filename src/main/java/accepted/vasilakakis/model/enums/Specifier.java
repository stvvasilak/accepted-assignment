package accepted.vasilakakis.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Specifier {
    HOME_WIN("HOME_WIN"),
    AWAY_WIN("AWAY_WIN"),
    X("X");

    private final String value;

    Specifier(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
