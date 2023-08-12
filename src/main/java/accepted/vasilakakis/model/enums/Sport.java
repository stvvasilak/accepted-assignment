package accepted.vasilakakis.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Sport {
    FOOTBALL(1),
    BASKETBALL(2);

    private final int value;

    Sport(int value) {
        this.value = value;
    }

    @JsonCreator
    public static Sport fromValue(int value) {
        for (Sport b : values()) {
            if (b.value == (value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
